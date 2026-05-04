package com.example.seproj.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.ui.common.LoginActivity;

/**
 * Main navigation hub for administrator workflows.
 * Links to analytics, counselor management, and logout from the admin workspace.
 *
 * Outstanding issues:
 * - Admin dashboard cards are static and can be expanded with live stats.
 */
public class AdminHomeActivity extends AppCompatActivity {

    private Button btnAnalytics;
    private Button btnManageCounselors;
    private Button btnAdminLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnAnalytics = findViewById(R.id.btnAnalytics);
        btnManageCounselors = findViewById(R.id.btnManageCounselors);
        btnAdminLogout = findViewById(R.id.btnAdminLogout);

        btnAnalytics.setOnClickListener(v ->
                startActivity(new Intent(this, AdminAnalyticsActivity.class))
        );

        btnManageCounselors.setOnClickListener(v ->
                startActivity(new Intent(this, AdminCounselorManagementActivity.class))
        );

        btnAdminLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}



