package com.example.seproj.ui.counselor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.model.IntakeForm;
import com.example.seproj.model.Student;
import com.example.seproj.repository.IntakeFormRepository;
import com.example.seproj.repository.StudentRepository;
import com.example.seproj.ui.common.BottomTaskbar;
import com.example.seproj.utils.FirestoreCallback;

/**
 * Counselor-facing read-only view of a student pre-session intake form.
 * Shows the same core intake questions students answered before booking.
 *
 * Outstanding issues:
 * - Empty-state recovery is basic if an intake form is missing.
 */
public class ViewIntakeFormActivity extends AppCompatActivity {

    private TextView tvIntakeDetails;
    private TextView tvMoodAnswer;
    private TextView tvGoalsAnswer;
    private TextView tvConcernsAnswer;
    private IntakeFormRepository repo;
    private StudentRepository studentRepository;
    private String slotId;
    private String counselorId;
    private String counselorName;

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_intake_form);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        tvMoodAnswer = findViewById(R.id.tvMoodAnswer);
        tvGoalsAnswer = findViewById(R.id.tvGoalsAnswer);
        tvConcernsAnswer = findViewById(R.id.tvConcernsAnswer);
        repo = new IntakeFormRepository();
        studentRepository = new StudentRepository();

        slotId = getIntent().getStringExtra("slotId");
        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");
        BottomTaskbar.attachCounselor(this, counselorId, counselorName);

        repo.getIntakeFormBySlotId(slotId, new FirestoreCallback<IntakeForm>() {
            @Override
            public void onSuccess(IntakeForm form) {
                if (form == null) {
                    tvMoodAnswer.setText("No intake form submitted.");
                    tvGoalsAnswer.setText("No intake form submitted.");
                    tvConcernsAnswer.setText("No intake form submitted.");
                    return;
                }

                String studentName = safe(form.getStudentName(), "");
                if (!studentName.isEmpty()) {
                    renderIntake(form, studentName);
                    return;
                }

                String studentId = safe(form.getStudentId(), "");
                if (studentId.isEmpty()) {
                    renderIntake(form, "Student");
                    return;
                }

                studentRepository.getStudentById(studentId, new FirestoreCallback<Student>() {
                    @Override
                    public void onSuccess(Student result) {
                        renderIntake(form, result == null ? "Student" : safe(result.getName(), "Student"));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        renderIntake(form, "Student");
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ViewIntakeFormActivity.this,
                        "Failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void renderIntake(IntakeForm form, String studentName) {
        tvMoodAnswer.setText(safe(form.getMood(), "Not provided"));
        tvGoalsAnswer.setText(safe(form.getGoals(), "Not provided"));
        tvConcernsAnswer.setText(safe(form.getConcerns(), "Not provided"));
    }

    private String safe(String value, String fallback) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim())) {
            return fallback;
        }
        return value.trim();
    }
}



