package com.example.seproj.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.ui.common.LoginActivity;
import com.example.seproj.ui.common.NotificationsActivity;
import com.example.seproj.ui.student.CounselorListActivity;

/**
 * Student home screen for the counseling clinic app.
 * This acts as the main navigation hub for student-side features
 * included in the halfway checkpoint.
 *
 * Supported flows:
 * - View counselor list
 * - View available slots
 * - View/manage appointments
 *
 * Outstanding issues:
 * - Upcoming appointment summary can be added later.
 * - Feedback and intake form screens can be added in later milestones.
 */
public class StudentHomeActivity extends AppCompatActivity {

    private TextView tvWelcomeStudent;
    private Button btnBrowseCounselors;
    private Button btnViewAvailableSlots;
    private Button btnMyAppointments;
    private Button btnLogout;
    private Button btnStudentNotifications;

    private String studentId;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        tvWelcomeStudent = findViewById(R.id.tvWelcomeStudent);
        btnBrowseCounselors = findViewById(R.id.btnBrowseCounselors);
        btnViewAvailableSlots = findViewById(R.id.btnViewAvailableSlots);
        btnMyAppointments = findViewById(R.id.btnMyAppointments);
        btnLogout = findViewById(R.id.btnStudentLogout);
        btnStudentNotifications = findViewById(R.id.btnStudentNotifications);
        studentId = getIntent().getStringExtra("studentId");
        studentName = getIntent().getStringExtra("studentName");

        if (studentName != null && !studentName.trim().isEmpty()) {
            tvWelcomeStudent.setText("Welcome, " + studentName);
        } else {
            tvWelcomeStudent.setText("Welcome, Student");
        }

        btnBrowseCounselors.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomeActivity.this, CounselorListActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
        });

        btnViewAvailableSlots.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomeActivity.this, CounselorListActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            intent.putExtra("openSlotsDirectly", true);
            startActivity(intent);
        });

        btnMyAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomeActivity.this, StudentAppointmentsActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnStudentNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(StudentHomeActivity.this, NotificationsActivity.class);
            intent.putExtra("recipientId", studentId);
            intent.putExtra("recipientRole", "student");
            intent.putExtra("displayName", studentName);
            startActivity(intent);
        });
    }
}