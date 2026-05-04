package com.example.seproj.ui.counselor;

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
import com.example.seproj.ui.common.AiInsightActivity;
import com.example.seproj.ui.common.AppointmentAdapter;
import com.example.seproj.ui.common.BottomTaskbar;
import com.example.seproj.ui.student.FeedbackFormActivity;
import com.example.seproj.utils.FirestoreCallback;
import com.example.seproj.ui.counselor.MarkNoShowActivity;
import com.example.seproj.ui.student.FeedbackFormActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all booked appointments for a counselor.
 *
 * Outstanding issues:
 * - Student name lookup can be added later.
 * - No-show marking can be added later.
 * - Appointment notes/intake summary can be added later.
 */
public class CounselorAppointmentsActivity extends AppCompatActivity {

    private TextView tvCounselorAppointmentsTitle;
    private TextView tvEmptyCounselorAppointments;
    private RecyclerView rvCounselorAppointments;
    private RecyclerView rvCounselorAvailableSlots;
    private RecyclerView rvCounselorPastAppointments;
    private ProgressBar progressCounselorAppointments;
    private Button btnBack;
    private Button btnHome;
    private AppointmentAdapter appointmentAdapter;
    private AppointmentAdapter availableSlotsAdapter;
    private AppointmentAdapter pastAppointmentsAdapter;
    private AppointmentSlotRepository slotRepository;
    private BookingService bookingService;

    private String counselorId;
    private String counselorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_appointments);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        tvCounselorAppointmentsTitle = findViewById(R.id.tvCounselorAppointmentsTitle);
        tvEmptyCounselorAppointments = findViewById(R.id.tvEmptyCounselorAppointments);
        rvCounselorAppointments = findViewById(R.id.rvCounselorAppointments);
        rvCounselorAvailableSlots = findViewById(R.id.rvCounselorAvailableSlots);
        rvCounselorPastAppointments = findViewById(R.id.rvCounselorPastAppointments);
        progressCounselorAppointments = findViewById(R.id.progressCounselorAppointments);

        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");
        BottomTaskbar.attachCounselor(this, counselorId, counselorName);

        if (counselorName != null && !counselorName.trim().isEmpty()) {
            tvCounselorAppointmentsTitle.setText(counselorName + "'s Appointments");
        } else {
            tvCounselorAppointmentsTitle.setText("Counselor Appointments");
        }

        slotRepository = new AppointmentSlotRepository();
        bookingService = new BookingService(this);

        setupRecyclerView();
        loadCounselorAppointments();
        btnBack.setOnClickListener(v -> finish());

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorAppointmentsActivity.this, CounselorHomeActivity.class);
            intent.putExtra("counselorId", counselorId);
            intent.putExtra("counselorName", counselorName);
            startActivity(intent);
            finish();
        });
    }

    private void setupRecyclerView() {
        appointmentAdapter = new AppointmentAdapter(
                this::handleCounselorMainAction,
                this::openFeedbackForm,
                this::openIntakeForm,
                this::openAiInsight,
                true,
                true
        );
        availableSlotsAdapter = new AppointmentAdapter(
                this::handleCounselorMainAction,
                null,
                null,
                null,
                true,
                true
        );
        pastAppointmentsAdapter = new AppointmentAdapter(
                this::handleCounselorMainAction,
                this::openFeedbackForm,
                this::openIntakeForm,
                this::openAiInsight,
                true,
                true
        );
        rvCounselorAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvCounselorAppointments.setAdapter(appointmentAdapter);
        rvCounselorAvailableSlots.setLayoutManager(new LinearLayoutManager(this));
        rvCounselorAvailableSlots.setAdapter(availableSlotsAdapter);
        rvCounselorPastAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvCounselorPastAppointments.setAdapter(pastAppointmentsAdapter);
    }

    private void handleCounselorMainAction(AppointmentSlot slot) {
        long now = System.currentTimeMillis();

        if (slot.getStartTimeMillis() <= now) {
            openMarkNoShowScreen(slot);
        } else {
            cancelCounselorAppointment(slot);
        }
    }

    private void cancelCounselorAppointment(AppointmentSlot slot) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel this appointment?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    bookingService.cancelSlotByCounselor(counselorId, slot.getSlotId(), new BookingService.BookingCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(CounselorAppointmentsActivity.this,
                                    message,
                                    Toast.LENGTH_SHORT).show();
                            loadCounselorAppointments();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(CounselorAppointmentsActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .setNegativeButton("No", null)
                .show();
    }


    private void openFeedbackForm(AppointmentSlot slot) {
        Intent intent = new Intent(this, FeedbackFormActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("studentId", slot.getStudentId());
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        intent.putExtra("slotStartTimeMillis", slot.getStartTimeMillis());
        intent.putExtra("slotEndTimeMillis", slot.getEndTimeMillis());
        intent.putExtra("isCounselorFeedback", true);
        startActivity(intent);
    }

    private void openIntakeForm(AppointmentSlot slot) {
        Intent intent = new Intent(this, ViewIntakeFormActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        startActivity(intent);
    }

    private void openAiInsight(AppointmentSlot slot) {
        Intent intent = new Intent(this, AiInsightActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("sourceRole", "counselor");
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCounselorAppointments();
    }

    private void loadCounselorAppointments() {
        showLoading(true);

        slotRepository.getAppointmentsForCounselor(
                counselorId,
                new FirestoreCallback<List<AppointmentSlot>>() {
                    @Override
                    public void onSuccess(List<AppointmentSlot> result) {
                        showLoading(false);

                        if (result == null || result.isEmpty()) {
                            rvCounselorAppointments.setVisibility(View.GONE);
                            rvCounselorAvailableSlots.setVisibility(View.GONE);
                            rvCounselorPastAppointments.setVisibility(View.GONE);
                            tvEmptyCounselorAppointments.setVisibility(View.VISIBLE);
                            tvEmptyCounselorAppointments.setText("No appointments or slots found.");
                        } else {
                            long now = System.currentTimeMillis();
                            List<AppointmentSlot> upcomingBooked = new ArrayList<>();
                            List<AppointmentSlot> availableFuture = new ArrayList<>();
                            List<AppointmentSlot> pastAppointments = new ArrayList<>();

                            for (AppointmentSlot slot : result) {
                                if (slot == null || AppointmentSlot.STATUS_CANCELLED.equals(slot.getStatus())) {
                                    continue;
                                }

                                boolean isPast = slot.getEndTimeMillis() < now;
                                if (isPast && !AppointmentSlot.STATUS_AVAILABLE.equals(slot.getStatus())) {
                                    pastAppointments.add(slot);
                                } else if (AppointmentSlot.STATUS_AVAILABLE.equals(slot.getStatus())
                                        && slot.getStartTimeMillis() > now) {
                                    availableFuture.add(slot);
                                } else if (AppointmentSlot.STATUS_BOOKED.equals(slot.getStatus())) {
                                    upcomingBooked.add(slot);
                                }
                            }

                            appointmentAdapter.setAppointmentList(upcomingBooked);
                            availableSlotsAdapter.setAppointmentList(availableFuture);
                            pastAppointmentsAdapter.setAppointmentList(pastAppointments);

                            rvCounselorAppointments.setVisibility(upcomingBooked.isEmpty() ? View.GONE : View.VISIBLE);
                            rvCounselorAvailableSlots.setVisibility(availableFuture.isEmpty() ? View.GONE : View.VISIBLE);
                            rvCounselorPastAppointments.setVisibility(pastAppointments.isEmpty() ? View.GONE : View.VISIBLE);

                            boolean allEmpty = upcomingBooked.isEmpty() && availableFuture.isEmpty() && pastAppointments.isEmpty();
                            tvEmptyCounselorAppointments.setVisibility(allEmpty ? View.VISIBLE : View.GONE);
                            tvEmptyCounselorAppointments.setText("No active appointments or future slots found.");
                            if (allEmpty) {
                                return;
                            }

                            tvEmptyCounselorAppointments.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        showLoading(false);
                        rvCounselorAppointments.setVisibility(View.GONE);
                        rvCounselorAvailableSlots.setVisibility(View.GONE);
                        rvCounselorPastAppointments.setVisibility(View.GONE);
                        tvEmptyCounselorAppointments.setVisibility(View.VISIBLE);
                        tvEmptyCounselorAppointments.setText("Failed to load counselor appointments.");
                        Toast.makeText(CounselorAppointmentsActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void openMarkNoShowScreen(AppointmentSlot slot) {
        Intent intent = new Intent(this, MarkNoShowActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("studentId", slot.getStudentId());
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        startActivity(intent);
    }

    private void showLoading(boolean loading) {
        progressCounselorAppointments.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvCounselorAppointments.setVisibility(loading ? View.GONE : View.VISIBLE);
        rvCounselorAvailableSlots.setVisibility(loading ? View.GONE : View.VISIBLE);
        rvCounselorPastAppointments.setVisibility(loading ? View.GONE : View.VISIBLE);
        tvEmptyCounselorAppointments.setVisibility(View.GONE);
    }
}
