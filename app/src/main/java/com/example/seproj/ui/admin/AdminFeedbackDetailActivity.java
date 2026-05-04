package com.example.seproj.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Displays the full details for a single feedback submission.
 * Used by admins to inspect rating, comment, appointment timing, and participant names.
 *
 * Outstanding issues:
 * - Admin response or escalation actions are not implemented yet.
 */
public class AdminFeedbackDetailActivity extends AppCompatActivity {

    private Button btnBack;
    private TextView tvFeedbackDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback_detail);

        btnBack = findViewById(R.id.btnBack);
        tvFeedbackDetails = findViewById(R.id.tvFeedbackDetails);

        btnBack.setOnClickListener(v -> finish());

        String studentName = getIntent().getStringExtra("studentName");
        String counselorName = getIntent().getStringExtra("counselorName");
        int rating = getIntent().getIntExtra("rating", 0);
        String comment = getIntent().getStringExtra("comment");
        long submittedAt = getIntent().getLongExtra("submittedAt", 0);
        long slotStart = getIntent().getLongExtra("slotStartTimeMillis", 0);
        long slotEnd = getIntent().getLongExtra("slotEndTimeMillis", 0);
        boolean isCounselorFeedback = getIntent().getBooleanExtra("isCounselorFeedback", false);

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

        studentName = safe(studentName, "Student");
        counselorName = safe(counselorName, "Counselor");
        comment = safe(comment, "No comment provided.");

        String details =
                "Type: " + (isCounselorFeedback ? "Counselor Feedback" : "Student Feedback") + "\n\n" +
                        "Student: " + studentName + "\n" +
                        "Counselor: " + counselorName + "\n\n" +
                        "Appointment: " + format.format(new Date(slotStart)) +
                        " - " + format.format(new Date(slotEnd)) + "\n\n" +
                        "Rating: " + rating + "\n\n" +
                        "Comment:\n" + comment + "\n\n" +
                        "Submitted: " + format.format(new Date(submittedAt));

        tvFeedbackDetails.setText(details);
    }

    private String safe(String value, String fallback) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim())) {
            return fallback;
        }
        return value.trim();
    }
}



