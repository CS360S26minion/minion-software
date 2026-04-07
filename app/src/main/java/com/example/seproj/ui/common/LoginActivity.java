package com.example.seproj.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
    private AutoCompleteTextView actvRole;
    private Button btnLogin;
    private Button btnGoToSignup;

    private StudentRepository studentRepository;
    private CounselorRepository counselorRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        actvRole = findViewById(R.id.actvLoginRole);
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
        String[] roles = {"student", "counselor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roles
        );
        actvRole.setAdapter(adapter);
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String role = actvRole.getText().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(role)) {
            actvRole.setError("Role is required");
            actvRole.requestFocus();
            return;
        }

        if (role.equals("student")) {
            loginStudent(email);
        } else if (role.equals("counselor")) {
            loginCounselor(email);
        } else {
            Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginStudent(String email) {
        studentRepository.getStudentByEmail(email, new FirestoreCallback<Student>() {
            @Override
            public void onSuccess(Student result) {
                if (result != null) {
                    Intent intent = new Intent(LoginActivity.this, StudentHomeActivity.class);
                    intent.putExtra("studentId", result.getStudentId());
                    intent.putExtra("studentName", result.getName());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Student not registered. Please sign up.",
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

    private void loginCounselor(String email) {
        counselorRepository.getCounselorByEmail(email, new FirestoreCallback<Counselor>() {
            @Override
            public void onSuccess(Counselor result) {
                if (result != null) {
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