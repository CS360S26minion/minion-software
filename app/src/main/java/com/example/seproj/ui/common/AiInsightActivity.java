package com.example.seproj.ui.common;

import android.os.Bundle;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.repository.AppointmentSlotRepository;
import com.example.seproj.service.AiInsightService;
import com.example.seproj.utils.FirestoreCallback;

/**
 * Displays an AI-generated overview for an attended appointment.
 * Loads the appointment context and renders a readable counseling-session summary.
 *
 * Outstanding issues:
 * - Regeneration controls are limited to failure states.
 */
public class AiInsightActivity extends AppCompatActivity {
    private TextView tvInsightStatus;
    private TextView tvInsightSummary;
    private ProgressBar progressAiInsight;
    private Button btnBack;
    private Button btnRegenerateInsight;

    private AppointmentSlotRepository slotRepository;
    private AiInsightService aiInsightService;
    private AppointmentSlot currentSlot;
    private String slotId;
    private String sourceRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_insight);

        tvInsightStatus = findViewById(R.id.tvInsightStatus);
        tvInsightSummary = findViewById(R.id.tvInsightSummary);
        progressAiInsight = findViewById(R.id.progressAiInsight);
        btnBack = findViewById(R.id.btnBack);
        btnRegenerateInsight = findViewById(R.id.btnRegenerateInsight);

        slotRepository = new AppointmentSlotRepository();
        aiInsightService = new AiInsightService();
        slotId = getIntent().getStringExtra("slotId");
        sourceRole = getIntent().getStringExtra("sourceRole");
        attachTaskbarForSourceRole();

        btnBack.setOnClickListener(v -> finish());
        btnRegenerateInsight.setOnClickListener(v -> {
            if (currentSlot != null) {
                generateInsight(currentSlot);
            }
        });

        if (slotId == null || slotId.trim().isEmpty()) {
            showError("Missing appointment details.");
            return;
        }

        loadAppointment();
    }

    private void attachTaskbarForSourceRole() {
        if ("student".equals(sourceRole)) {
            BottomTaskbar.attachStudent(
                    this,
                    getIntent().getStringExtra("studentId"),
                    getIntent().getStringExtra("studentName")
            );
        } else if ("counselor".equals(sourceRole)) {
            BottomTaskbar.attachCounselor(
                    this,
                    getIntent().getStringExtra("counselorId"),
                    getIntent().getStringExtra("counselorName")
            );
        }
    }

    private void loadAppointment() {
        showLoading(true, "Loading appointment...");

        slotRepository.getSlotById(slotId, new FirestoreCallback<AppointmentSlot>() {
            @Override
            public void onSuccess(AppointmentSlot result) {
                if (result == null) {
                    showError("Appointment not found.");
                    return;
                }

                currentSlot = result;
                generateInsight(result);
            }

            @Override
            public void onFailure(Exception e) {
                showError("Failed to load appointment: " + e.getMessage());
            }
        });
    }

    private void generateInsight(AppointmentSlot slot) {
        showLoading(true, "Generating AI insight...");

        aiInsightService.getOrGenerateInsight(slot, new AiInsightService.InsightCallback() {
            @Override
            public void onSuccess(String summary) {
                showLoading(false, "AI-generated overview");
                tvInsightSummary.setText(formatInsightSummary(summary));
                btnRegenerateInsight.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e) {
                showError(e.getMessage());
            }
        });
    }

    private void showLoading(boolean loading, String status) {
        progressAiInsight.setVisibility(loading ? View.VISIBLE : View.GONE);
        tvInsightStatus.setText(status);
        btnRegenerateInsight.setEnabled(!loading);
        if (loading) {
            tvInsightSummary.setText("");
            btnRegenerateInsight.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        progressAiInsight.setVisibility(View.GONE);
        tvInsightStatus.setText("Could not generate insight");
        tvInsightSummary.setText(message == null ? "Something went wrong." : message);
        btnRegenerateInsight.setVisibility(currentSlot == null ? View.GONE : View.VISIBLE);
        btnRegenerateInsight.setEnabled(true);
        Toast.makeText(this, tvInsightSummary.getText().toString(), Toast.LENGTH_LONG).show();
    }

    private SpannableStringBuilder formatInsightSummary(String rawSummary) {
        SpannableStringBuilder formatted = new SpannableStringBuilder();
        if (rawSummary == null || rawSummary.trim().isEmpty()) {
            formatted.append("No summary was generated.");
            return formatted;
        }

        String[] lines = rawSummary.replace("\r", "").split("\n");
        boolean skipNextTechnicalId = false;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                appendBlankLine(formatted);
                continue;
            }

            line = line.replace("**", "");
            line = line.replace("__", "");

            if (line.toLowerCase().contains("appointment slot id")) {
                skipNextTechnicalId = true;
                continue;
            }

            if (skipNextTechnicalId) {
                skipNextTechnicalId = false;
                if (line.matches("[a-fA-F0-9\\-]{16,}")) {
                    continue;
                }
            }

            if (line.equalsIgnoreCase("AI Insight Summary: Counselling Session")
                    || line.equalsIgnoreCase("AI Insight Summary: Counseling Session")) {
                continue;
            }

            if (line.startsWith("* ")) {
                line = "â€¢ " + line.substring(2).trim();
            } else if (line.startsWith("- ")) {
                line = "â€¢ " + line.substring(2).trim();
            }

            int start = formatted.length();
            formatted.append(line).append("\n");
            if (isSectionHeading(line)) {
                formatted.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start,
                        start + line.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return formatted;
    }

    private boolean isSectionHeading(String line) {
        return line.endsWith(":") && line.length() <= 42 && !line.startsWith("â€¢");
    }

    private void appendBlankLine(SpannableStringBuilder formatted) {
        int length = formatted.length();
        if (length == 0) {
            return;
        }
        String current = formatted.toString();
        if (!current.endsWith("\n\n")) {
            formatted.append("\n");
        }
    }
}



