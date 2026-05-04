package com.example.seproj.service;

import android.os.Handler;
import android.os.Looper;

import com.example.seproj.BuildConfig;
import com.example.seproj.model.AiInsightSummary;
import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.model.FeedbackForm;
import com.example.seproj.model.IntakeForm;
import com.example.seproj.repository.AiInsightRepository;
import com.example.seproj.repository.FeedbackRepository;
import com.example.seproj.repository.IntakeFormRepository;
import com.example.seproj.utils.FirestoreCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Generates or loads AI summaries for attended past appointments.
 * Combines appointment, intake, and feedback data into a counseling-session overview.
 *
 * Outstanding issues:
 * - Prompt versioning and human review workflow can be added later.
 */
public class AiInsightService {
    public interface InsightCallback {
        void onSuccess(String summary);
        void onFailure(Exception e);
    }

    private final AiInsightRepository insightRepository = new AiInsightRepository();
    private final IntakeFormRepository intakeRepository = new IntakeFormRepository();
    private final FeedbackRepository feedbackRepository = new FeedbackRepository();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void getOrGenerateInsight(AppointmentSlot slot, InsightCallback callback) {
        if (!isEligibleForInsight(slot)) {
            callback.onFailure(new IllegalStateException("AI insights are only available for past attended appointments."));
            return;
        }

        insightRepository.getSummaryBySlotId(slot.getSlotId(), new FirestoreCallback<AiInsightSummary>() {
            @Override
            public void onSuccess(AiInsightSummary result) {
                if (result != null && result.getSummary() != null && !result.getSummary().trim().isEmpty()) {
                    callback.onSuccess(result.getSummary());
                    return;
                }

                loadInputsAndGenerate(slot, callback);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void loadInputsAndGenerate(AppointmentSlot slot, InsightCallback callback) {
        intakeRepository.getIntakeFormBySlotId(slot.getSlotId(), new FirestoreCallback<IntakeForm>() {
            @Override
            public void onSuccess(IntakeForm intakeForm) {
                feedbackRepository.getFeedbackForSlot(slot.getSlotId(), new FirestoreCallback<List<FeedbackForm>>() {
                    @Override
                    public void onSuccess(List<FeedbackForm> feedbackForms) {
                        generateAndSave(slot, intakeForm, feedbackForms, callback);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void generateAndSave(AppointmentSlot slot,
                                 IntakeForm intakeForm,
                                 List<FeedbackForm> feedbackForms,
                                 InsightCallback callback) {
        String apiKey = BuildConfig.AI_INSIGHTS_API_KEY == null ? "" : BuildConfig.AI_INSIGHTS_API_KEY.trim();
        if (apiKey.isEmpty()) {
            callback.onFailure(new IllegalStateException("Missing Gemini API key. Add GEMINI_API_KEY to gradle.properties and rebuild."));
            return;
        }

        executorService.execute(() -> {
            try {
                String prompt = buildPrompt(slot, intakeForm, feedbackForms);
                String summary = callGemini(prompt, apiKey);
                AiInsightSummary insightSummary = new AiInsightSummary(
                        slot.getSlotId(),
                        summary,
                        System.currentTimeMillis()
                );

                insightRepository.saveSummary(insightSummary)
                        .addOnSuccessListener(unused -> callback.onSuccess(summary))
                        .addOnFailureListener(callback::onFailure);
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

    private boolean isEligibleForInsight(AppointmentSlot slot) {
        return slot != null
                && slot.getEndTimeMillis() < System.currentTimeMillis()
                && !AppointmentSlot.STATUS_NO_SHOW.equals(slot.getStatus());
    }

    private String buildPrompt(AppointmentSlot slot,
                               IntakeForm intakeForm,
                               List<FeedbackForm> feedbackForms) {
        FeedbackForm studentFeedback = null;
        FeedbackForm counselorFeedback = null;

        if (feedbackForms != null) {
            for (FeedbackForm feedback : feedbackForms) {
                if (feedback == null) {
                    continue;
                }
                if (feedback.isCounselorFeedback()) {
                    counselorFeedback = feedback;
                } else {
                    studentFeedback = feedback;
                }
            }
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are generating an AI insight summary for a university counselling app.\n");
        prompt.append("Use only the provided intake and feedback data. Do not invent facts, diagnoses, or treatment plans.\n");
        prompt.append("Write a concise, professional overview of what likely happened in the meeting.\n");
        prompt.append("Include: main themes, student goals/concerns, feedback from both sides, and suggested follow-up focus if supported by the data.\n");
        prompt.append("Do not include technical IDs. Do not use Markdown symbols such as **, #, or code formatting.\n");
        prompt.append("Use short section headings and readable bullet points where helpful.\n");
        prompt.append("If data is missing, say what is missing briefly.\n\n");

        prompt.append("Appointment:\n");
        prompt.append("Slot ID: ").append(safe(slot.getSlotId())).append('\n');
        prompt.append("Date/time: ").append(formatRange(slot.getStartTimeMillis(), slot.getEndTimeMillis())).append('\n');
        prompt.append("Status: attended\n\n");

        prompt.append("Student intake form:\n");
        if (intakeForm == null) {
            prompt.append("No intake form found.\n\n");
        } else {
            prompt.append("Student name: ").append(safe(intakeForm.getStudentName())).append('\n');
            prompt.append("Counselor name: ").append(safe(intakeForm.getCounselorName())).append('\n');
            prompt.append("Mood: ").append(safe(intakeForm.getMood())).append('\n');
            prompt.append("Goals: ").append(safe(intakeForm.getGoals())).append('\n');
            prompt.append("Concerns: ").append(safe(intakeForm.getConcerns())).append("\n\n");
        }

        prompt.append("Student feedback:\n");
        appendFeedback(prompt, studentFeedback);
        prompt.append("\nCounselor feedback:\n");
        appendFeedback(prompt, counselorFeedback);

        return prompt.toString();
    }

    private void appendFeedback(StringBuilder prompt, FeedbackForm feedback) {
        if (feedback == null) {
            prompt.append("No feedback found.\n");
            return;
        }

        prompt.append("Rating: ").append(feedback.getRating()).append("/5\n");
        prompt.append("Comment: ").append(safe(feedback.getComment())).append('\n');
    }

    private String callGemini(String prompt, String apiKey) throws Exception {
        String model = BuildConfig.AI_INSIGHTS_MODEL == null || BuildConfig.AI_INSIGHTS_MODEL.trim().isEmpty()
                ? "gemini-2.5-flash"
                : BuildConfig.AI_INSIGHTS_MODEL.trim();
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("x-goog-api-key", apiKey);
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);

        JSONObject textPart = new JSONObject().put("text", prompt);
        JSONObject content = new JSONObject()
                .put("role", "user")
                .put("parts", new JSONArray().put(textPart));
        JSONObject generationConfig = new JSONObject()
                .put("temperature", 0.2)
                .put("maxOutputTokens", 800);
        JSONObject body = new JSONObject()
                .put("contents", new JSONArray().put(content))
                .put("generationConfig", generationConfig);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(body.toString());
        }

        int responseCode = connection.getResponseCode();
        boolean success = responseCode >= 200 && responseCode < 300;
        String response = readResponse(connection, success);
        connection.disconnect();

        if (!success) {
            throw new IllegalStateException("Gemini request failed (" + responseCode + "): " + response);
        }

        return extractText(response);
    }

    private String readResponse(HttpURLConnection connection, boolean success) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                success ? connection.getInputStream() : connection.getErrorStream(),
                StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private String extractText(String response) throws Exception {
        JSONObject json = new JSONObject(response);
        JSONArray candidates = json.optJSONArray("candidates");
        if (candidates == null || candidates.length() == 0) {
            throw new IllegalStateException("Gemini returned no summary.");
        }

        JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
        if (content == null) {
            throw new IllegalStateException("Gemini returned an empty response.");
        }

        JSONArray parts = content.optJSONArray("parts");
        if (parts == null || parts.length() == 0) {
            throw new IllegalStateException("Gemini returned no text.");
        }

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < parts.length(); i++) {
            String text = parts.getJSONObject(i).optString("text", "");
            if (!text.trim().isEmpty()) {
                if (summary.length() > 0) {
                    summary.append("\n\n");
                }
                summary.append(text.trim());
            }
        }

        if (summary.length() == 0) {
            throw new IllegalStateException("Gemini returned no text.");
        }

        return summary.toString();
    }

    private String formatRange(long startMillis, long endMillis) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return format.format(new Date(startMillis)) + " - " + timeFormat.format(new Date(endMillis));
    }

    private String safe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Not provided";
        }
        return value.trim();
    }
}



