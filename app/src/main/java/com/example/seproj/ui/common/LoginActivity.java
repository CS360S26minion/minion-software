package com.example.seproj.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.model.Counselor;
import com.example.seproj.model.Student;
import com.example.seproj.repository.CounselorRepository;
import com.example.seproj.repository.StudentRepository;
import com.example.seproj.ui.counselor.CounselorHomeActivity;
import com.example.seproj.ui.student.StudentHomeActivity;
import com.example.seproj.utils.FirestoreCallback;
import com.example.seproj.ui.common.SignupActivity;
import com.example.seproj.ui.admin.AdminHomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Login screen for students and counselors.
 * Users enter an email and choose a role. If an account exists
 * in the corresponding Firestore collection, they are routed
 * to the appropriate home screen.
 *
 * Outstanding issues:
 * - Real authentication is not implemented yet.
 * - Passwords are intentionally omitted for prototype simplicity.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Spinner spinnerRole;
    private Button btnLogin;
    private Button btnGoToSignup;

    private StudentRepository studentRepository;
    private CounselorRepository counselorRepository;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        spinnerRole = findViewById(R.id.spinnerLoginRole);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignup = findViewById(R.id.btnGoToSignup);

        studentRepository = new StudentRepository();
        counselorRepository = new CounselorRepository();

        setupRoleDropdown();

        btnLogin.setOnClickListener(v -> attemptLogin());

        btnGoToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void setupRoleDropdown() {
        String[] roles = {"student", "counselor", "admin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }



        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (role.equals("student")) {
            loginStudent(email, password);
        } else if (role.equals("counselor")) {
            loginCounselor(email, password);
        } else if (role.equals("admin")) {
            loginAdmin(email, password);
        } else {
            Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginAdmin(String email, String password) {
        db.collection("admins")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        String storedPassword = snapshot.getDocuments().get(0).getString("password");

                        if (!password.equals(storedPassword)) {
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String adminId = snapshot.getDocuments().get(0).getId();
                        String adminName = snapshot.getDocuments().get(0).getString("name");

                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        intent.putExtra("adminId", adminId);
                        intent.putExtra("adminName", adminName);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Admin not registered.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginStudent(String email, String password) {
        studentRepository.getStudentByEmail(email, new FirestoreCallback<Student>() {
            @Override
            public void onSuccess(Student result) {
                if (result != null) {
                    if (!password.equals(result.getPassword())) {
                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(LoginActivity.this, StudentHomeActivity.class);
                    intent.putExtra("studentId", result.getStudentId());
                    intent.putExtra("studentName", result.getName());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Student not registered. Please sign up.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginCounselor(String email, String password) {
        counselorRepository.getCounselorByEmail(email, new FirestoreCallback<Counselor>() {
            @Override
            public void onSuccess(Counselor result) {
                if (result != null) {

                    if (!password.equals(result.getPassword())) {
                        Toast.makeText(LoginActivity.this,
                                "Incorrect password",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(LoginActivity.this, CounselorHomeActivity.class);
                    intent.putExtra("counselorId", result.getCounselorId());
                    intent.putExtra("counselorName", result.getName());
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Counselor not registered. Please sign up.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(LoginActivity.this,
                        "Login failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}