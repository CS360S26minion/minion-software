package com.example.seproj.ui.counselor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.ui.common.LoginActivity;
import com.example.seproj.ui.common.NotificationsActivity;
import com.example.seproj.ui.counselor.CounselorAppointmentsActivity;
import com.example.seproj.ui.counselor.SetAvailabilityActivity;

/**
 * Counselor home screen for the counseling clinic app.
 * This acts as the main navigation hub for counselor-side features
 * included in the halfway checkpoint.
 *
 * Supported flows:
 * - Set availability
 * - View booked appointments
 *
 * Outstanding issues:
 * - Notification center can be added later.
 * - No-show marking and analytics are future features.
 */
public class CounselorHomeActivity extends AppCompatActivity {

    private TextView tvWelcomeCounselor;
    private Button btnSetAvailability;
    private Button btnViewAppointments;
    private Button btnLogout;
    private Button btnCounselorNotifications;

    private String counselorId;
    private String counselorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_home);

        tvWelcomeCounselor = findViewById(R.id.tvWelcomeCounselor);
        btnSetAvailability = findViewById(R.id.btnSetAvailability);
        btnViewAppointments = findViewById(R.id.btnViewCounselorAppointments);
        btnLogout = findViewById(R.id.btnCounselorLogout);
        btnCounselorNotifications = findViewById(R.id.btnCounselorNotifications);
        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");

        if (counselorName != null && !counselorName.trim().isEmpty()) {
            tvWelcomeCounselor.setText("Welcome, " + counselorName);
        } else {
            tvWelcomeCounselor.setText("Welcome, Counselor");
        }

        btnSetAvailability.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorHomeActivity.this, SetAvailabilityActivity.class);
            intent.putExtra("counselorId", counselorId);
            intent.putExtra("counselorName", counselorName);
            startActivity(intent);
        });

        btnViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorHomeActivity.this, CounselorAppointmentsActivity.class);
            intent.putExtra("counselorId", counselorId);
            intent.putExtra("counselorName", counselorName);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnCounselorNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorHomeActivity.this, NotificationsActivity.class);
            intent.putExtra("recipientId", counselorId);
            intent.putExtra("recipientRole", "counselor");
            intent.putExtra("displayName", counselorName);
            startActivity(intent);
        });
    }
}