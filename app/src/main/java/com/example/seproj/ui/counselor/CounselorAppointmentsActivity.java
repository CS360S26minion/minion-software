package com.example.seproj.ui.counselor;

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
import com.example.seproj.ui.common.AppointmentAdapter;
import com.example.seproj.utils.FirestoreCallback;

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
    private ProgressBar progressCounselorAppointments;
    private Button btnBack;
    private Button btnHome;
    private AppointmentAdapter appointmentAdapter;
    private AppointmentSlotRepository slotRepository;

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
        progressCounselorAppointments = findViewById(R.id.progressCounselorAppointments);

        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");

        if (counselorName != null && !counselorName.trim().isEmpty()) {
            tvCounselorAppointmentsTitle.setText(counselorName + "'s Appointments");
        } else {
            tvCounselorAppointmentsTitle.setText("Counselor Appointments");
        }

        slotRepository = new AppointmentSlotRepository();

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
        appointmentAdapter = new AppointmentAdapter(null, false);
        rvCounselorAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvCounselorAppointments.setAdapter(appointmentAdapter);
    }

    private void loadCounselorAppointments() {
        showLoading(true);

        slotRepository.getBookedAppointmentsForCounselor(
                counselorId,
                new FirestoreCallback<List<AppointmentSlot>>() {
                    @Override
                    public void onSuccess(List<AppointmentSlot> result) {
                        showLoading(false);

                        if (result == null || result.isEmpty()) {
                            rvCounselorAppointments.setVisibility(View.GONE);
                            tvEmptyCounselorAppointments.setVisibility(View.VISIBLE);
                            tvEmptyCounselorAppointments.setText("No booked appointments found.");
                        } else {
                            tvEmptyCounselorAppointments.setVisibility(View.GONE);
                            rvCounselorAppointments.setVisibility(View.VISIBLE);
                            appointmentAdapter.setAppointmentList(result);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        showLoading(false);
                        rvCounselorAppointments.setVisibility(View.GONE);
                        tvEmptyCounselorAppointments.setVisibility(View.VISIBLE);
                        tvEmptyCounselorAppointments.setText("Failed to load counselor appointments.");
                        Toast.makeText(CounselorAppointmentsActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void showLoading(boolean loading) {
        progressCounselorAppointments.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvCounselorAppointments.setVisibility(loading ? View.GONE : View.VISIBLE);
        tvEmptyCounselorAppointments.setVisibility(View.GONE);
    }
}