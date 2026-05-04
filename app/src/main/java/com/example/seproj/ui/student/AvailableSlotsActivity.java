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
import com.example.seproj.ui.common.BottomTaskbar;
import com.example.seproj.ui.common.SlotAdapter;
import com.example.seproj.utils.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays available slots for a selected counselor.
 * Students can book one of the shown slots.
 *
 * Outstanding issues:
 * - Confirmation notifications will be added next.
 * - Intake form link can be added later.
 */
public class AvailableSlotsActivity extends AppCompatActivity {

    private TextView tvTitle;

    private Button btnBack;
    private Button btnHome;
    private RecyclerView rvSlots;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    private SlotAdapter slotAdapter;
    private AppointmentSlotRepository slotRepository;
    private BookingService bookingService;

    private String studentId;
    private String studentName;
    private String counselorId;
    private String counselorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_slots);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        tvTitle = findViewById(R.id.tvSlotsTitle);
        rvSlots = findViewById(R.id.rvSlots);
        progressBar = findViewById(R.id.progressSlots);
        tvEmpty = findViewById(R.id.tvEmptySlots);

        studentId = getIntent().getStringExtra("studentId");
        studentName = getIntent().getStringExtra("studentName");
        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");
        BottomTaskbar.attachStudent(this, studentId, studentName);

        if (counselorName != null && !counselorName.trim().isEmpty()) {
            tvTitle.setText("Available Slots - " + counselorName);
        } else {
            tvTitle.setText("Available Slots");
        }

        slotRepository = new AppointmentSlotRepository();
        bookingService = new BookingService(this);

        setupRecyclerView();
        loadSlots();

        btnBack.setOnClickListener(v -> finish());

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AvailableSlotsActivity.this, StudentHomeActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
            finish();
        });


    }

    private void setupRecyclerView() {
        slotAdapter = new SlotAdapter(new ArrayList<>(), this::showBookingConfirmationDialog);
        rvSlots.setLayoutManager(new LinearLayoutManager(this));
        rvSlots.setAdapter(slotAdapter);
    }

    private void loadSlots() {
        showLoading(true);

        slotRepository.getAvailableSlotsForCounselor(counselorId,
                new FirestoreCallback<List<AppointmentSlot>>() {
                    @Override
                    public void onSuccess(List<AppointmentSlot> result) {
                        showLoading(false);

                        if (result == null || result.isEmpty()) {
                            rvSlots.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText("No slots available.");
                        } else {
                        List<AppointmentSlot> futureSlots = new ArrayList<>();
                        long now = System.currentTimeMillis();

                        for (AppointmentSlot slot : result) {
                            if (slot != null && slot.getStartTimeMillis() > now) {
                                futureSlots.add(slot);
                            }
                        }

                        if (futureSlots.isEmpty()) {
                            rvSlots.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText("No future slots available.");
                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            rvSlots.setVisibility(View.VISIBLE);
                            slotAdapter.updateSlots(futureSlots);
                        }
                    }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        showLoading(false);
                        rvSlots.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText("Failed to load slots.");
                        Toast.makeText(AvailableSlotsActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showBookingConfirmationDialog(AppointmentSlot slot) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Do you want to book this appointment slot?")
                .setPositiveButton("Book", (dialog, which) -> bookSelectedSlot(slot))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void bookSelectedSlot(AppointmentSlot slot) {
        Intent intent = new Intent(AvailableSlotsActivity.this, IntakeFormActivity.class);
        intent.putExtra("slotId", slot.getSlotId());
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        intent.putExtra("slotStartTimeMillis", slot.getStartTimeMillis());
        intent.putExtra("slotEndTimeMillis", slot.getEndTimeMillis());
        startActivity(intent);
    }



    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvSlots.setVisibility(loading ? View.GONE : View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }
}
