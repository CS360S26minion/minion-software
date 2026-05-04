package com.example.seproj.service;

import android.os.Handler;
import android.os.Looper;

import com.example.seproj.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Connects the student AI chatbot UI to the configured Gemini model.
 * Builds a counselor-style support prompt and returns conversational responses.
 *
 * Outstanding issues:
 * - Conversation history is not persisted between app launches.
 */
public class AiChatbotService {
    public interface ChatCallback {
        /**

         * Called when a chatbot response is successfully received.

         *

         * @param reply the generated chatbot reply

         */
        void onSuccess(String reply);
        /**

         * Called when an error occurs during chatbot processing.

         *

         * @param e the exception encountered

         */
        void onFailure(Exception e);
    }

    private static final String SYSTEM_PROMPT =
            "You are a supportive AI counselor inside a university counselling app. " +
            "Help students talk through stress, anxiety, academic pressure, relationships, motivation, loneliness, and everyday wellbeing. " +
            "Use a warm, calm, non-judgmental tone. Ask gentle follow-up questions when helpful. " +
            "Offer practical coping strategies such as grounding, reflection, planning, communication tips, and encouragement to seek support. " +
            "Do not diagnose, prescribe medication, or claim to replace a licensed counselor. " +
            "If a student mentions self-harm, suicide, abuse, immediate danger, or a medical emergency, respond with empathy and urge them to contact emergency services, a trusted person, campus support, or their counselor immediately. " +
            "Keep replies concise, student-friendly, and easy to read.";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<ChatTurn> history = new ArrayList<>();
    /**

     * Sends a user message to the chatbot and retrieves a response.

     *

     * <p>This method:

     * <ul>

     *     <li>Adds the user message to conversation history</li>

     *     <li>Executes the API request in a background thread</li>

     *     <li>Returns the response on the main thread via callback</li>

     * </ul>

     * </p>

     *

     * @param message  the user's input message

     * @param callback callback to receive success or failure result

     */
    public void sendMessage(String message, ChatCallback callback) {
        String apiKey = BuildConfig.AI_INSIGHTS_API_KEY == null ? "" : BuildConfig.AI_INSIGHTS_API_KEY.trim();
        if (apiKey.isEmpty()) {
            callback.onFailure(new IllegalStateException("Missing Gemini API key. Add GEMINI_API_KEY to gradle.properties and rebuild."));
            return;
        }

        history.add(new ChatTurn("user", message));

        executorService.execute(() -> {
            try {
                String reply = callGemini(apiKey);
                history.add(new ChatTurn("model", reply));
                mainHandler.post(() -> callback.onSuccess(reply));
            } catch (Exception e) {
                if (!history.isEmpty()) {
                    history.remove(history.size() - 1);
                }
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }
    /**

     * Calls the Gemini API with the current conversation context.

     *

     * @param apiKey the API key for authentication

     * @return generated chatbot response text

     * @throws Exception if the API request fails

     */
    private String callGemini(String apiKey) throws Exception {
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

        JSONObject body = new JSONObject()
                .put("systemInstruction", new JSONObject()
                        .put("parts", new JSONArray()
                                .put(new JSONObject().put("text", SYSTEM_PROMPT))))
                .put("contents", buildContents())
                .put("generationConfig", new JSONObject()
                        .put("temperature", 0.5)
                        .put("maxOutputTokens", 700));

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(body.toString());
        }

        int responseCode = connection.getResponseCode();
        boolean success = responseCode >= 200 && responseCode < 300;
        String response = readResponse(connection, success);
        connection.disconnect();

        if (!success) {
            throw new IllegalStateException("Chatbot request failed (" + responseCode + "): " + response);
        }

        return extractText(response);
    }
    /**

     * Builds the conversation history payload for the API request.

     *

     * <p>Limits history to the most recent messages to reduce payload size.</p>

     *

     * @return JSON array of conversation turns

     */
    private JSONArray buildContents() throws Exception {
        JSONArray contents = new JSONArray();
        int start = Math.max(0, history.size() - 12);

        for (int i = start; i < history.size(); i++) {
            ChatTurn turn = history.get(i);
            contents.put(new JSONObject()
                    .put("role", turn.role)
                    .put("parts", new JSONArray()
                            .put(new JSONObject().put("text", turn.text))));
        }

        return contents;
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
            throw new IllegalStateException("The chatbot did not return a response.");
        }

        JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
        if (content == null) {
            throw new IllegalStateException("The chatbot returned an empty response.");
        }

        JSONArray parts = content.optJSONArray("parts");
        if (parts == null || parts.length() == 0) {
            throw new IllegalStateException("The chatbot returned no text.");
        }

        StringBuilder reply = new StringBuilder();
        for (int i = 0; i < parts.length(); i++) {
            String text = parts.getJSONObject(i).optString("text", "");
            if (!text.trim().isEmpty()) {
                if (reply.length() > 0) {
                    reply.append("\n\n");
                }
                reply.append(text.trim());
            }
        }

        if (reply.length() == 0) {
            throw new IllegalStateException("The chatbot returned no text.");
        }

        return reply.toString();
    }

    private static class ChatTurn {
        private final String role;
        private final String text;

        private ChatTurn(String role, String text) {
            this.role = role;
            this.text = text;
        }
    }
}



