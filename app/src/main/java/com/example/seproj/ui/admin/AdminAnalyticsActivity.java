package com.example.seproj.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.repository.AdminRepository;
import com.example.seproj.utils.FirestoreCallback;

import java.util.List;
import java.util.Locale;

/**
 * Displays high-level appointment analytics for administrators.
 * Shows totals, no-shows, cancellations, and a visual breakdown for quick operational review.
 *
 * Outstanding issues:
 * - Analytics are snapshot-based and not yet filterable by counselor or date range.
 */
public class AdminAnalyticsActivity extends AppCompatActivity {

    private TextView tvTotalAppointments;
    private TextView tvNoShows;
    private TextView tvCancelled;
    private TextView tvNoShowRate;
    private TextView tvBookedBarLabel;
    private TextView tvNoShowBarLabel;
    private TextView tvCancelledBarLabel;
    private View barBooked;
    private View barNoShows;
    private View barCancelled;

    private AdminRepository adminRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics);

        tvTotalAppointments = findViewById(R.id.tvTotalAppointments);
        tvNoShows = findViewById(R.id.tvNoShows);
        tvCancelled = findViewById(R.id.tvCancelled);
        tvNoShowRate = findViewById(R.id.tvNoShowRate);
        tvBookedBarLabel = findViewById(R.id.tvBookedBarLabel);
        tvNoShowBarLabel = findViewById(R.id.tvNoShowBarLabel);
        tvCancelledBarLabel = findViewById(R.id.tvCancelledBarLabel);
        barBooked = findViewById(R.id.barBooked);
        barNoShows = findViewById(R.id.barNoShows);
        barCancelled = findViewById(R.id.barCancelled);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        adminRepository = new AdminRepository();

        loadAnalytics();
    }

    /**
     *
     */
    public AdminAnalyticsActivity() {
        super();
    }

    private void loadAnalytics() {
        adminRepository.getAllAppointmentSlots(new FirestoreCallback<List<AppointmentSlot>>() {
            @Override
            public void onSuccess(List<AppointmentSlot> result) {
                int totalAppointments = 0;
                int noShows = 0;
                int cancelled = 0;
                int bookedOrAttended = 0;

                if (result != null) {
                    for (AppointmentSlot slot : result) {
                        if (slot == null || slot.getStatus() == null) continue;

                        String status = slot.getStatus();

                        if (AppointmentSlot.STATUS_BOOKED.equals(status)
                                || AppointmentSlot.STATUS_NO_SHOW.equals(status)
                                || AppointmentSlot.STATUS_CANCELLED.equals(status)) {
                            totalAppointments++;
                        }

                        if (AppointmentSlot.STATUS_BOOKED.equals(status)) {
                            bookedOrAttended++;
                        }

                        if (AppointmentSlot.STATUS_NO_SHOW.equals(status)) {
                            noShows++;
                        }

                        if (AppointmentSlot.STATUS_CANCELLED.equals(status)) {
                            cancelled++;
                        }
                    }
                }

                double noShowRate = totalAppointments == 0
                        ? 0
                        : (noShows * 100.0) / totalAppointments;

                tvTotalAppointments.setText("Total Appointments: " + totalAppointments);
                tvNoShows.setText("No-Shows: " + noShows);
                tvCancelled.setText("Cancelled: " + cancelled);
                tvNoShowRate.setText(String.format(Locale.getDefault(),
                        "No-Show Rate: %.1f%%", noShowRate));
                updateBars(totalAppointments, bookedOrAttended, noShows, cancelled);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminAnalyticsActivity.this,
                        "Failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateBars(int totalAppointments, int bookedOrAttended, int noShows, int cancelled) {
        tvBookedBarLabel.setText("Booked / Attended: " + bookedOrAttended);
        tvNoShowBarLabel.setText("No-Shows: " + noShows);
        tvCancelledBarLabel.setText("Cancelled: " + cancelled);

        int safeTotal = Math.max(totalAppointments, 1);
        setBarWeight(barBooked, bookedOrAttended / (float) safeTotal);
        setBarWeight(barNoShows, noShows / (float) safeTotal);
        setBarWeight(barCancelled, cancelled / (float) safeTotal);
    }

    private void setBarWeight(View bar, float fraction) {
        bar.post(() -> {
            View parent = (View) bar.getParent();
            int trackWidth = parent.getWidth();
            int minWidth = fraction > 0 ? Math.max(8, Math.round(trackWidth * fraction)) : 1;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) bar.getLayoutParams();
            params.width = minWidth;
            bar.setLayoutParams(params);
        });
    }
}



