package com.example.seproj.ui.student;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.repository.AppointmentSlotRepository;
import com.example.seproj.service.BookingService;
import com.example.seproj.ui.common.AppointmentAdapter;
import com.example.seproj.utils.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all appointments for a student and allows cancellation.
 *
 * Outstanding issues:
 * - Reschedule button can be added next.
 * - Counselor name lookup can be added later for richer display.
 */
public class StudentAppointmentsActivity extends AppCompatActivity {

    private TextView tvAppointmentsTitle;
    private TextView tvEmptyAppointments;
    private RecyclerView rvAppointments;
    private ProgressBar progressBar;
    private Button btnBack;
    private Button btnHome;
    private AppointmentAdapter appointmentAdapter;
    private AppointmentSlotRepository slotRepository;
    private BookingService bookingService;

    private String studentId;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_appointments);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        tvAppointmentsTitle = findViewById(R.id.tvAppointmentsTitle);
        tvEmptyAppointments = findViewById(R.id.tvEmptyAppointments);
        rvAppointments = findViewById(R.id.rvAppointments);
        progressBar = findViewById(R.id.progressAppointments);

        studentId = getIntent().getStringExtra("studentId");
        studentName = getIntent().getStringExtra("studentName");

        if (studentName != null && !studentName.trim().isEmpty()) {
            tvAppointmentsTitle.setText(studentName + "'s Appointments");
        }

        slotRepository = new AppointmentSlotRepository();
        bookingService = new BookingService(this);

        setupRecyclerView();
        loadAppointments();

        btnBack.setOnClickListener(v -> finish());

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAppointmentsActivity.this, StudentHomeActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
            finish();
        });
    }

    private void setupRecyclerView() {
        appointmentAdapter = new AppointmentAdapter(this::showCancelConfirmationDialog, true);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvAppointments.setAdapter(appointmentAdapter);
    }

    private void loadAppointments() {
        showLoading(true);

        slotRepository.getAppointmentsForStudent(studentId, new FirestoreCallback<List<AppointmentSlot>>() {
            @Override
            public void onSuccess(List<AppointmentSlot> result) {
                showLoading(false);

                if (result == null || result.isEmpty()) {
                    rvAppointments.setVisibility(View.GONE);
                    tvEmptyAppointments.setVisibility(View.VISIBLE);
                    tvEmptyAppointments.setText("No appointments found.");
                    return;
                }

                List<AppointmentSlot> bookedAppointments = new ArrayList<>();
                for (AppointmentSlot slot : result) {
                    if (slot != null && AppointmentSlot.STATUS_BOOKED.equals(slot.getStatus())) {
                        bookedAppointments.add(slot);
                    }
                }

                if (bookedAppointments.isEmpty()) {
                    rvAppointments.setVisibility(View.GONE);
                    tvEmptyAppointments.setVisibility(View.VISIBLE);
                    tvEmptyAppointments.setText("No active booked appointments found.");
                } else {
                    tvEmptyAppointments.setVisibility(View.GONE);
                    rvAppointments.setVisibility(View.VISIBLE);
                    appointmentAdapter.setAppointmentList(bookedAppointments);
                }
            }

            @Override
            public void onFailure(Exception e) {
                showLoading(false);
                rvAppointments.setVisibility(View.GONE);
                tvEmptyAppointments.setVisibility(View.VISIBLE);
                tvEmptyAppointments.setText("Failed to load appointments.");
                Toast.makeText(StudentAppointmentsActivity.this,
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showCancelConfirmationDialog(AppointmentSlot slot) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel this appointment?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> cancelAppointment(slot))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelAppointment(AppointmentSlot slot) {
        showLoading(true);

        bookingService.cancelSlot(studentId, slot.getSlotId(), new BookingService.BookingCallback() {
            @Override
            public void onSuccess(String message) {
                showLoading(false);
                Toast.makeText(StudentAppointmentsActivity.this, message, Toast.LENGTH_LONG).show();
                loadAppointments();
            }

            @Override
            public void onFailure(String errorMessage) {
                showLoading(false);
                Toast.makeText(StudentAppointmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                loadAppointments();
            }
        });
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvAppointments.setVisibility(loading ? View.GONE : View.VISIBLE);
        tvEmptyAppointments.setVisibility(View.GONE);
    }
}