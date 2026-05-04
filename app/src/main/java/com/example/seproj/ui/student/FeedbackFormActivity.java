package com.example.seproj.ui.student;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.repository.CounselorRepository;
import com.example.seproj.repository.StudentRepository;
import com.example.seproj.service.SupportFeatureService;
import com.example.seproj.ui.common.BottomTaskbar;
import com.example.seproj.utils.FirestoreCallback;

/**
 * Collects post-session feedback from either a student or counselor.
 * Stores ratings and comments once an attended appointment is eligible for review.
 *
 * Outstanding issues:
 * - Feedback questions are limited to rating and one comment box.
 */
public class FeedbackFormActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etComment;
    private Button btnSubmit;
    private SupportFeatureService supportService;

    private String slotId;
    private String counselorId;

    private String studentId;
    private String studentName;
    private String counselorName;
    private long slotStartTimeMillis;
    private long slotEndTimeMillis;
    private boolean isCounselorFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        supportService = new SupportFeatureService();


        slotId = getIntent().getStringExtra("slotId");
        counselorId = getIntent().getStringExtra("counselorId");
        isCounselorFeedback = getIntent().getBooleanExtra("isCounselorFeedback", false);
        studentId = getIntent().getStringExtra("studentId");
        studentName = getIntent().getStringExtra("studentName");
        counselorName = getIntent().getStringExtra("counselorName");
        slotStartTimeMillis = getIntent().getLongExtra("slotStartTimeMillis", 0);
        slotEndTimeMillis = getIntent().getLongExtra("slotEndTimeMillis", 0);
        isCounselorFeedback = getIntent().getBooleanExtra("isCounselorFeedback", false);
        if (isCounselorFeedback) {
            BottomTaskbar.attachCounselor(this, counselorId, counselorName);
        } else {
            BottomTaskbar.attachStudent(this, studentId, studentName);
        }
        ratingBar = findViewById(R.id.ratingBarFeedback);
        etComment = findViewById(R.id.etFeedbackComment);
        btnSubmit = findViewById(R.id.btnSubmitFeedback);
        Button btnBack = findViewById(R.id.btnBack);

        btnSubmit.setOnClickListener(v -> submitFeedback());
        btnBack.setOnClickListener(v -> finish());
        hydrateMissingNames();
    }

    private void hydrateMissingNames() {
        if ((counselorName == null || counselorName.trim().isEmpty())
                && counselorId != null && !counselorId.trim().isEmpty()) {
            new CounselorRepository().getCounselorById(counselorId, new FirestoreCallback<com.example.seproj.model.Counselor>() {
                @Override
                public void onSuccess(com.example.seproj.model.Counselor result) {
                    if (result != null && result.getName() != null) {
                        counselorName = result.getName();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    // Feedback can still be submitted; display code falls back gracefully.
                }
            });
        }

        if ((studentName == null || studentName.trim().isEmpty())
                && studentId != null && !studentId.trim().isEmpty()) {
            new StudentRepository().getStudentById(studentId, new FirestoreCallback<com.example.seproj.model.Student>() {
                @Override
                public void onSuccess(com.example.seproj.model.Student result) {
                    if (result != null && result.getName() != null) {
                        studentName = result.getName();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    // Feedback can still be submitted; display code falls back gracefully.
                }
            });
        }
    }

    private void submitFeedback() {
        int rating = (int) ratingBar.getRating();
        String comment = etComment.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSubmit.setEnabled(false);
        supportService.submitFeedback(
                slotId,
                studentId,
                safeName(studentName, "Student"),
                counselorId,
                safeName(counselorName, "Counselor"),
                slotStartTimeMillis,
                slotEndTimeMillis,
                rating,
                comment,
                isCounselorFeedback
        ).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            btnSubmit.setEnabled(true);
            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private String safeName(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}



