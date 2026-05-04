package com.example.seproj.ui.counselor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.service.SupportFeatureService;
import com.example.seproj.ui.common.BottomTaskbar;

/**
 * Counselor workflow for marking a past appointment as a student no-show.
 * Confirms the action and delegates attendance update logic to support services.
 *
 * Outstanding issues:
 * - Undo and dispute handling are not implemented.
 */
public class MarkNoShowActivity extends AppCompatActivity {

    private TextView tvNoShowInfo;
    private Button btnMarkNoShow;
    private SupportFeatureService supportService;

    private String slotId;
    private String studentId;
    private String counselorId;
    private String counselorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_no_show);

        supportService = new SupportFeatureService();

        slotId = getIntent().getStringExtra("slotId");
        studentId = getIntent().getStringExtra("studentId");
        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");
        BottomTaskbar.attachCounselor(this, counselorId, counselorName);

        tvNoShowInfo = findViewById(R.id.tvNoShowInfo);
        btnMarkNoShow = findViewById(R.id.btnConfirmNoShow);
        Button btnBack = findViewById(R.id.btnBack);

        tvNoShowInfo.setText("Mark appointment " + slotId + " as no-show?");

        btnMarkNoShow.setOnClickListener(v -> markNoShow());
        btnBack.setOnClickListener(v -> finish());
    }

    private void markNoShow() {
        supportService.markNoShow(slotId, studentId, counselorId)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Appointment marked as no-show", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}



