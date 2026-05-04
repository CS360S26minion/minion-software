package com.example.seproj.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.FeedbackForm;
import com.example.seproj.repository.FeedbackRepository;
import com.example.seproj.ui.common.AdminFeedbackAdapter;
import com.example.seproj.utils.FirestoreCallback;

import java.util.List;

/**
 * Shows admin-facing lists of counselor feedback records.
 * Allows administrators to review feedback trends and open detailed feedback views.
 *
 * Outstanding issues:
 * - Search and date filtering are not implemented yet.
 */
public class AdminCounselorFeedbackListActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rvFeedback;
    private FeedbackRepository feedbackRepository;
    private AdminFeedbackAdapter adapter;

    private String counselorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_counselor_feedback_list);

        btnBack = findViewById(R.id.btnBack);
        rvFeedback = findViewById(R.id.rvFeedback);

        counselorId = getIntent().getStringExtra("counselorId");

        feedbackRepository = new FeedbackRepository();

        adapter = new AdminFeedbackAdapter(feedback -> {
            Intent intent = new Intent(this, AdminFeedbackDetailActivity.class);
            intent.putExtra("studentName", feedback.getStudentName());
            intent.putExtra("counselorName", feedback.getCounselorName());
            intent.putExtra("rating", feedback.getRating());
            intent.putExtra("comment", feedback.getComment());
            intent.putExtra("submittedAt", feedback.getSubmittedAt());
            intent.putExtra("slotStartTimeMillis", feedback.getSlotStartTimeMillis());
            intent.putExtra("slotEndTimeMillis", feedback.getSlotEndTimeMillis());
            intent.putExtra("isCounselorFeedback", feedback.isCounselorFeedback());
            startActivity(intent);
        });

        rvFeedback.setLayoutManager(new LinearLayoutManager(this));
        rvFeedback.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        loadFeedback();
    }

    private void loadFeedback() {
        feedbackRepository.getFeedbackForCounselor(counselorId, new FirestoreCallback<List<FeedbackForm>>() {
            @Override
            public void onSuccess(List<FeedbackForm> result) {
                adapter.setFeedbackList(result);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminCounselorFeedbackListActivity.this,
                        "Failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}


