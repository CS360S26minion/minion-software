package com.example.seproj.ui.student;

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
import com.example.seproj.model.Counselor;
import com.example.seproj.model.FeedbackForm;
import com.example.seproj.repository.CounselorRepository;
import com.example.seproj.repository.FeedbackRepository;
import com.example.seproj.ui.common.CounselorAdapter;
import com.example.seproj.ui.common.BottomTaskbar;
import com.example.seproj.utils.FirestoreCallback;
import com.example.seproj.ui.student.AvailableSlotsActivity;

import java.util.List;

/**
 * Displays all active counselors for students to browse.
 * Students can tap a counselor to move to the available slots screen.
 *
 * Outstanding issues:
 * - Search/filter support can be added later.
 * - Profile photos can be added later.
 */
public class CounselorListActivity extends AppCompatActivity {

    private TextView tvCounselorListTitle;
    private TextView tvEmptyState;
    private RecyclerView rvCounselors;
    private ProgressBar progressBar;

    private Button btnBack;
    private Button btnHome;

    private CounselorAdapter counselorAdapter;
    private CounselorRepository counselorRepository;
    private FeedbackRepository feedbackRepository;

    private String studentId;
    private String studentName;
    private boolean openSlotsDirectly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_list);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        tvCounselorListTitle = findViewById(R.id.tvCounselorListTitle);
        tvEmptyState = findViewById(R.id.tvCounselorEmptyState);
        rvCounselors = findViewById(R.id.rvCounselors);
        progressBar = findViewById(R.id.progressBarCounselors);

        studentId = getIntent().getStringExtra("studentId");
        studentName = getIntent().getStringExtra("studentName");
        openSlotsDirectly = getIntent().getBooleanExtra("openSlotsDirectly", false);
        BottomTaskbar.attachStudent(this, studentId, studentName);

        counselorRepository = new CounselorRepository();
        feedbackRepository = new FeedbackRepository();

        setupRecyclerView();
        loadCounselors();
        btnBack.setOnClickListener(v -> finish());

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorListActivity.this, StudentHomeActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
            finish();
        });
    }

    private void setupRecyclerView() {
        counselorAdapter = new CounselorAdapter(counselor -> {
            Intent intent = new Intent(CounselorListActivity.this, AvailableSlotsActivity.class);
            intent.putExtra("studentId", studentId);
            intent.putExtra("studentName", studentName);
            intent.putExtra("counselorId", counselor.getCounselorId());
            intent.putExtra("counselorName", counselor.getName());
            intent.putExtra("counselorSpecialization", counselor.getSpecialization());
            startActivity(intent);
        });

        rvCounselors.setLayoutManager(new LinearLayoutManager(this));
        rvCounselors.setAdapter(counselorAdapter);
    }

    private void loadCounselors() {
        showLoading(true);

        counselorRepository.getAllActiveCounselors(new FirestoreCallback<List<Counselor>>() {
            @Override
            public void onSuccess(List<Counselor> result) {
                showLoading(false);

                if (result == null || result.isEmpty()) {
                    rvCounselors.setVisibility(View.GONE);
                    tvEmptyState.setVisibility(View.VISIBLE);
                    tvEmptyState.setText("No active counselors found.");
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    rvCounselors.setVisibility(View.VISIBLE);
                    counselorAdapter.setCounselorList(result);
                    loadCounselorRatings(result);
                }
            }

            @Override
            public void onFailure(Exception e) {
                showLoading(false);
                rvCounselors.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                tvEmptyState.setText("Failed to load counselors.");
                Toast.makeText(CounselorListActivity.this,
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        rvCounselors.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
    }

    private void loadCounselorRatings(List<Counselor> counselors) {
        for (Counselor counselor : counselors) {
            feedbackRepository.getFeedbackForCounselor(counselor.getCounselorId(), new FirestoreCallback<List<FeedbackForm>>() {
                @Override
                public void onSuccess(List<FeedbackForm> result) {
                    int count = 0;
                    int total = 0;
                    if (result != null) {
                        for (FeedbackForm feedback : result) {
                            if (feedback != null && !feedback.isCounselorFeedback() && feedback.getRating() > 0) {
                                count++;
                                total += feedback.getRating();
                            }
                        }
                    }

                    counselor.setRatingCount(count);
                    counselor.setAverageRating(count == 0 ? 0 : total / (double) count);
                    counselorAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Exception e) {
                    // Ratings are supplemental; counselor browsing should still work.
                }
            });
        }
    }
}
