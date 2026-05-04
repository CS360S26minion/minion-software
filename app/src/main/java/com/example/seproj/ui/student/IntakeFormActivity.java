package com.example.seproj.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.service.BookingService;
import com.example.seproj.service.SupportFeatureService;
import com.example.seproj.ui.common.BottomTaskbar;

/**
 * Collects pre-session intake information before confirming a booking.
 * Saves student context and then books the selected appointment slot.
 *
 * Outstanding issues:
 * - Validation is minimal and answers are free text only.
 */
public class IntakeFormActivity extends AppCompatActivity {

    private EditText etMood, etGoals, etConcerns;
    private Button btnSubmit;
    private SupportFeatureService supportService;
    private BookingService bookingService;

    private String slotId;
    private String studentId;
    private String counselorId;

    private String studentName;
    private String counselorName;
    private long slotStartTimeMillis;
    private long slotEndTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form);



        supportService = new SupportFeatureService();
        bookingService = new BookingService(this);

        slotId = getIntent().getStringExtra("slotId");
        studentId = getIntent().getStringExtra("studentId");
        counselorId = getIntent().getStringExtra("counselorId");
        studentName = getIntent().getStringExtra("studentName");
        counselorName = getIntent().getStringExtra("counselorName");
        slotStartTimeMillis = getIntent().getLongExtra("slotStartTimeMillis", 0);
        slotEndTimeMillis = getIntent().getLongExtra("slotEndTimeMillis", 0);
        BottomTaskbar.attachStudent(this, studentId, studentName);

        etMood = findViewById(R.id.etMood);
        etGoals = findViewById(R.id.etGoals);
        etConcerns = findViewById(R.id.etConcerns);
        btnSubmit = findViewById(R.id.btnSubmitIntake);
        Button btnBack = findViewById(R.id.btnBack);

        btnSubmit.setOnClickListener(v -> submitForm());
        btnBack.setOnClickListener(v -> finish());
    }

    private void submitForm() {
        String mood = etMood.getText().toString().trim();
        String goals = etGoals.getText().toString().trim();
        String concerns = etConcerns.getText().toString().trim();

        if (mood.isEmpty() || goals.isEmpty()) {
            Toast.makeText(this, "Please fill mood and goals", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSubmit.setEnabled(false);

        supportService.submitIntakeForm(
                        slotId,
                        studentId,
                        studentName,
                        counselorId,
                        counselorName,
                        slotStartTimeMillis,
                        slotEndTimeMillis,
                        mood,
                        goals,
                        concerns
                )
                .addOnSuccessListener(unused -> bookSlotAfterIntake())
                .addOnFailureListener(e -> {
                    btnSubmit.setEnabled(true);
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void bookSlotAfterIntake() {
        bookingService.bookSlot(studentId, slotId, new BookingService.BookingCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(IntakeFormActivity.this,
                        "Intake submitted and appointment booked!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(IntakeFormActivity.this, StudentAppointmentsActivity.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("studentName", studentName);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                btnSubmit.setEnabled(true);
                Toast.makeText(IntakeFormActivity.this,
                        "Intake saved, but booking failed: " + errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}



