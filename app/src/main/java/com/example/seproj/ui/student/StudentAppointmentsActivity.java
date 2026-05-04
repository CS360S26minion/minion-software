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
import com.example.seproj.model.Counselor;
import com.example.seproj.model.FeedbackForm;
import com.example.seproj.repository.AppointmentSlotRepository;
import com.example.seproj.repository.CounselorRepository;
import com.example.seproj.repository.FeedbackRepository;
import com.example.seproj.service.BookingService;
import com.example.seproj.ui.common.AiInsightActivity;
import com.example.seproj.ui.common.AppointmentAdapter;
import com.example.seproj.ui.common.BottomTaskbar;
import com.example.seproj.utils.FirestoreCallback;
import com.example.seproj.ui.student.FeedbackFormActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
//    private RecyclerView rvAppointments;
    private ProgressBar progressBar;
    private Button btnBack;
    private Button btnHome;
//    private AppointmentAdapter appointmentAdapter;
    private AppointmentSlotRepository slotRepository;
    private BookingService bookingService;

    private RecyclerView rvUpcomingAppointments;
    private RecyclerView rvPastAppointments;

    private AppointmentAdapter upcomingAdapter;
    private AppointmentAdapter pastAdapter;
    private CounselorRepository counselorRepository;
    private FeedbackRepository feedbackRepository;

    private String studentId;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_appointments);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        tvAppointmentsTitle = findViewById(R.id.tvAppointmentsTitle);
        rvUpcomingAppointments = findViewById(R.id.rvUpcomingAppointments);
        rvPastAppointments = findViewById(R.id.rvPastAppointments);
        tvEmptyAppointments = findViewById(R.id.tvEmptyAppointments);
//        rvAppointments = findViewById(R.id.rvAppointments);
        progressBar = findViewById(R.id.progressAppointments);

        studentId = getIntent().getStringExtra("studentId");
        studentName = getIntent().getStringExtra("studentName");
        BottomTaskbar.attachStudent(this, studentId, studentName);

        if (studentName != null && !studentName.trim().isEmpty()) {
            tvAppointmentsTitle.setText(studentName + "'s Appointments");
        }

        slotRepository = new AppointmentSlotRepository();
        counselorRepository = new CounselorRepository();
        feedbackRepository = new FeedbackRepository();
        bookingService = new BookingService(this);

        setupRecyclerView();
//        loadAppointments();

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
        upcomingAdapter = new AppointmentAdapter(
                this::showCancelConfirmationDialog,
                slot -> openFeedbackForm(slot),
                null,
                this::openAiInsight,
                true,
                false
        );

        pastAdapter = new AppointmentAdapter(
                null,
                slot -> openFeedbackForm(slot),
                null,
                this::openAiInsight,
                false,
                false
        );


        rvUpcomingAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvUpcomingAppointments.setAdapter(upcomingAdapter);

        rvPastAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvPastAppointments.setAdapter(pastAdapter);
    }



    private void openFeedbackForm(AppointmentSlot slot) {
        Intent intent = new Intent(StudentAppointmentsActivity.this, FeedbackFormActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("studentId", studentId);
        intent.putExtra("counselorId", slot.getCounselorId());
        intent.putExtra("isCounselorFeedback", false);
        intent.putExtra("studentName", studentName);
        intent.putExtra("slotStartTimeMillis", slot.getStartTimeMillis());
        intent.putExtra("slotEndTimeMillis", slot.getEndTimeMillis());
        startActivity(intent);
    }

    private void openAiInsight(AppointmentSlot slot) {
        Intent intent = new Intent(StudentAppointmentsActivity.this, AiInsightActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("sourceRole", "student");
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (slotRepository != null && upcomingAdapter != null && pastAdapter != null) {
            loadAppointments();
        }
    }

    private void loadAppointments() {
        showLoading(true);

        slotRepository.getAppointmentsForStudent(studentId, new FirestoreCallback<List<AppointmentSlot>>() {
            @Override
            public void onSuccess(List<AppointmentSlot> result) {
                showLoading(false);

                if (result == null || result.isEmpty()) {
                    rvUpcomingAppointments.setVisibility(View.GONE);
                    rvPastAppointments.setVisibility(View.GONE);
                    tvEmptyAppointments.setVisibility(View.VISIBLE);
                    tvEmptyAppointments.setText("No appointments found.");
                    return;
                }

                hydrateStudentFeedbackFlags(result);
            }

            @Override
            public void onFailure(Exception e) {
                showLoading(false);
                rvUpcomingAppointments.setVisibility(View.GONE);
                rvPastAppointments.setVisibility(View.GONE);
                tvEmptyAppointments.setVisibility(View.VISIBLE);
                tvEmptyAppointments.setText("Failed to load appointments.");
                Toast.makeText(StudentAppointmentsActivity.this,
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hydrateStudentFeedbackFlags(List<AppointmentSlot> appointments) {
        feedbackRepository.getStudentFeedbackForStudent(studentId, new FirestoreCallback<List<FeedbackForm>>() {
            @Override
            public void onSuccess(List<FeedbackForm> feedbackForms) {
                Set<String> feedbackSlotIds = new HashSet<>();
                if (feedbackForms != null) {
                    for (FeedbackForm feedback : feedbackForms) {
                        if (feedback != null && feedback.getSlotId() != null) {
                            feedbackSlotIds.add(feedback.getSlotId());
                        }
                    }
                }

                for (AppointmentSlot slot : appointments) {
                    if (slot != null && feedbackSlotIds.contains(slot.getSlotId())) {
                        slot.setStudentFeedbackSubmitted(true);
                    }
                }

                renderAppointments(appointments);
                hydrateCounselorNames(appointments);
            }

            @Override
            public void onFailure(Exception e) {
                renderAppointments(appointments);
                hydrateCounselorNames(appointments);
            }
        });
    }

    private void renderAppointments(List<AppointmentSlot> appointments) {
        long now = System.currentTimeMillis();

        List<AppointmentSlot> upcomingAppointments = new ArrayList<>();
        List<AppointmentSlot> pastAppointments = new ArrayList<>();

        for (AppointmentSlot slot : appointments) {
            if (slot == null) continue;

            boolean isPast = slot.getEndTimeMillis() < now;

            if (isPast) {
                if (!"no_show".equalsIgnoreCase(slot.getStatus())) {
                    slot.setStatus("attended");
                }
                pastAppointments.add(slot);
            } else if (AppointmentSlot.STATUS_BOOKED.equals(slot.getStatus())) {
                upcomingAppointments.add(slot);
            }
        }

        upcomingAdapter.setAppointmentList(upcomingAppointments);
        pastAdapter.setAppointmentList(pastAppointments);

        rvUpcomingAppointments.setVisibility(
                upcomingAppointments.isEmpty() ? View.GONE : View.VISIBLE
        );

        rvPastAppointments.setVisibility(
                pastAppointments.isEmpty() ? View.GONE : View.VISIBLE
        );

        if (upcomingAppointments.isEmpty() && pastAppointments.isEmpty()) {
            tvEmptyAppointments.setVisibility(View.VISIBLE);
            tvEmptyAppointments.setText("No appointment history found.");
        } else {
            tvEmptyAppointments.setVisibility(View.GONE);
        }
    }

    private void hydrateCounselorNames(List<AppointmentSlot> appointments) {
        Map<String, String> nameCache = new HashMap<>();

        for (AppointmentSlot slot : appointments) {
            if (slot == null || slot.getCounselorId() == null || slot.getCounselorId().trim().isEmpty()) {
                continue;
            }

            String counselorId = slot.getCounselorId();
            if (nameCache.containsKey(counselorId)) {
                slot.setCounselorName(nameCache.get(counselorId));
                renderAppointments(appointments);
                continue;
            }

            counselorRepository.getCounselorById(counselorId, new FirestoreCallback<Counselor>() {
                @Override
                public void onSuccess(Counselor result) {
                    if (result != null && result.getName() != null && !result.getName().trim().isEmpty()) {
                        nameCache.put(counselorId, result.getName().trim());
                        for (AppointmentSlot appointment : appointments) {
                            if (appointment != null && counselorId.equals(appointment.getCounselorId())) {
                                appointment.setCounselorName(result.getName().trim());
                            }
                        }
                        renderAppointments(appointments);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    // Counselor names improve display only; appointments remain usable without them.
                }
            });
        }
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

        rvUpcomingAppointments.setVisibility(loading ? View.GONE : View.VISIBLE);
        rvPastAppointments.setVisibility(loading ? View.GONE : View.VISIBLE);

        if (tvEmptyAppointments != null) {
            tvEmptyAppointments.setVisibility(View.GONE);
        }
    }
}
