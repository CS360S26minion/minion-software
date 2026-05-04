package com.example.seproj.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.Counselor;
import com.example.seproj.repository.AdminRepository;
import com.example.seproj.ui.common.AdminCounselorAdapter;
import com.example.seproj.utils.FirestoreCallback;

import java.util.List;
import java.util.UUID;

/**
 * Allows administrators to create, review, and delete counselor profiles.
 * This screen manages counselor login credentials and profile metadata shown to students.
 *
 * Outstanding issues:
 * - Editing existing counselor records is limited compared with creation and deletion.
 */
public class AdminCounselorManagementActivity extends AppCompatActivity {

    private EditText etCounselorName;
    private EditText etCounselorEmail;
    private EditText etCounselorSpecialty;
    private EditText etCounselorPassword;
    private Button btnAddCounselor;
    private RecyclerView rvCounselors;

    private AdminRepository adminRepository;
    private AdminCounselorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_counselor_management);

        etCounselorName = findViewById(R.id.etCounselorName);
        etCounselorEmail = findViewById(R.id.etCounselorEmail);
        etCounselorSpecialty = findViewById(R.id.etCounselorSpecialty);
        etCounselorPassword = findViewById(R.id.etCounselorPassword);
        btnAddCounselor = findViewById(R.id.btnAddCounselor);
        rvCounselors = findViewById(R.id.rvAdminCounselors);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        adminRepository = new AdminRepository();

        adapter = new AdminCounselorAdapter(this::confirmDeleteCounselor);
        rvCounselors.setLayoutManager(new LinearLayoutManager(this));
        rvCounselors.setAdapter(adapter);

        btnAddCounselor.setOnClickListener(v -> addCounselor());

        loadCounselors();
    }

    private void addCounselor() {
        String name = etCounselorName.getText().toString().trim();
        String email = etCounselorEmail.getText().toString().trim();
        String specialty = etCounselorSpecialty.getText().toString().trim();
        String password = etCounselorPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(specialty) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String counselorId = UUID.randomUUID().toString();

        Counselor counselor = new Counselor(
                counselorId,
                name,
                email,
                specialty,
                "",
                true
        );
        counselor.setPassword(password);

        adminRepository.addOrUpdateCounselor(counselor)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Counselor added", Toast.LENGTH_SHORT).show();
                    etCounselorName.setText("");
                    etCounselorEmail.setText("");
                    etCounselorSpecialty.setText("");
                    etCounselorPassword.setText("");
                    loadCounselors();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void loadCounselors() {
        adminRepository.getAllCounselors(new FirestoreCallback<List<Counselor>>() {
            @Override
            public void onSuccess(List<Counselor> result) {
                adapter.setCounselors(result);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminCounselorManagementActivity.this,
                        "Failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmDeleteCounselor(Counselor counselor) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Counselor")
                .setMessage("Delete " + counselor.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    adminRepository.deleteCounselor(counselor.getCounselorId())
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Counselor deleted", Toast.LENGTH_SHORT).show();
                                loadCounselors();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}



