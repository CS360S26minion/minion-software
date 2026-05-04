package com.example.seproj.ui.common;

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
import com.example.seproj.model.AppNotification;
import com.example.seproj.repository.NotificationRepository;
import com.example.seproj.ui.counselor.CounselorHomeActivity;
import com.example.seproj.ui.student.StudentHomeActivity;
import com.example.seproj.utils.FirestoreCallback;

import java.util.List;

/**
 * Displays all notifications for the logged-in student or counselor.
 */
public class NotificationsActivity extends AppCompatActivity {

    private TextView tvNotificationsTitle;
    private TextView tvEmptyNotifications;
    private RecyclerView rvNotifications;
    private ProgressBar progressNotifications;
    private Button btnBack;
    private Button btnHome;
    private Button btnClearNotifications;
    private NotificationAdapter notificationAdapter;
    private NotificationRepository notificationRepository;

    private String recipientId;
    private String recipientRole;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnClearNotifications = findViewById(R.id.btnClearNotifications);
        tvNotificationsTitle = findViewById(R.id.tvNotificationsTitle);
        tvEmptyNotifications = findViewById(R.id.tvEmptyNotifications);
        rvNotifications = findViewById(R.id.rvNotifications);
        progressNotifications = findViewById(R.id.progressNotifications);

        recipientId = getIntent().getStringExtra("recipientId");
        recipientRole = getIntent().getStringExtra("recipientRole");
        displayName = getIntent().getStringExtra("displayName");
        if ("student".equals(recipientRole)) {
            BottomTaskbar.attachStudent(this, recipientId, displayName);
        } else if ("counselor".equals(recipientRole)) {
            BottomTaskbar.attachCounselor(this, recipientId, displayName);
        }

        if (displayName != null && !displayName.trim().isEmpty()) {
            tvNotificationsTitle.setText(displayName + "'s Notifications");
        }

        notificationRepository = new NotificationRepository();

        setupRecyclerView();
        loadNotifications();

        btnBack.setOnClickListener(v -> finish());

        btnHome.setOnClickListener(v -> {
            Intent intent;

            if ("student".equals(recipientRole)) {
                intent = new Intent(NotificationsActivity.this, StudentHomeActivity.class);
                intent.putExtra("studentId", recipientId);
                intent.putExtra("studentName", displayName);
            } else {
                intent = new Intent(NotificationsActivity.this, CounselorHomeActivity.class);
                intent.putExtra("counselorId", recipientId);
                intent.putExtra("counselorName", displayName);
            }

            startActivity(intent);
            finish();
        });

        btnClearNotifications.setOnClickListener(v -> clearNotifications());
    }

    private void setupRecyclerView() {
        notificationAdapter = new NotificationAdapter();
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(notificationAdapter);
    }

    private void loadNotifications() {
        showLoading(true);

        notificationRepository.getNotificationsForRecipient(recipientId,
                new FirestoreCallback<List<AppNotification>>() {
                    @Override
                    public void onSuccess(List<AppNotification> result) {
                        showLoading(false);

                        if (result == null || result.isEmpty()) {
                            rvNotifications.setVisibility(View.GONE);
                            tvEmptyNotifications.setVisibility(View.VISIBLE);
                            tvEmptyNotifications.setText("No notifications found.");
                        } else {
                            tvEmptyNotifications.setVisibility(View.GONE);
                            rvNotifications.setVisibility(View.VISIBLE);
                            notificationAdapter.setNotificationList(result);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        showLoading(false);
                        rvNotifications.setVisibility(View.GONE);
                        tvEmptyNotifications.setVisibility(View.VISIBLE);
                        tvEmptyNotifications.setText("Failed to load notifications.");
                        Toast.makeText(NotificationsActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showLoading(boolean loading) {
        progressNotifications.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvNotifications.setVisibility(loading ? View.GONE : View.VISIBLE);
        tvEmptyNotifications.setVisibility(View.GONE);
    }

    private void clearNotifications() {
        showLoading(true);
        notificationRepository.clearNotificationsForRecipient(recipientId, new FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                showLoading(false);
                notificationAdapter.setNotificationList(null);
                rvNotifications.setVisibility(View.GONE);
                tvEmptyNotifications.setVisibility(View.VISIBLE);
                tvEmptyNotifications.setText("No notifications found.");
                Toast.makeText(NotificationsActivity.this, "Notifications cleared", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                showLoading(false);
                Toast.makeText(NotificationsActivity.this,
                        "Failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                loadNotifications();
            }
        });
    }
}
