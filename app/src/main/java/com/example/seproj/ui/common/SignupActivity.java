package com.example.seproj.ui.common;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.example.seproj.utils.FirestoreCallback;

import java.util.UUID;

/**
 * Signup screen for new users.
 * Users can register as either student or counselor.
 *
 * Outstanding issues:
 * - Password-based authentication is not added yet.
 * - Duplicate account handling can be improved later.
 */
public class SignupActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private AutoCompleteTextView actvRole;
    private EditText etSpecialization;
    private EditText etBio;
    private Button btnSignup;
    private Button btnBackToLogin;

    private StudentRepository studentRepository;
    private CounselorRepository counselorRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etSignupName);
        etEmail = findViewById(R.id.etSignupEmail);
        actvRole = findViewById(R.id.actvSignupRole);
        etSpecialization = findViewById(R.id.etCounselorSpecialization);
        etBio = findViewById(R.id.etCounselorBio);
        btnSignup = findViewById(R.id.btnSignup);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        studentRepository = new StudentRepository();
        counselorRepository = new CounselorRepository();

        setupRoleDropdown();

        actvRole.setOnItemClickListener((parent, view, position, id) -> updateCounselorFieldsVisibility());

        btnSignup.setOnClickListener(v -> attemptSignup());

        btnBackToLogin.setOnClickListener(v -> finish());

        updateCounselorFieldsVisibility();
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

    private void updateCounselorFieldsVisibility() {
        String role = actvRole.getText().toString().trim().toLowerCase();
        if ("counselor".equals(role)) {
            etSpecialization.setVisibility(View.VISIBLE);
            etBio.setVisibility(View.VISIBLE);
        } else {
            etSpecialization.setVisibility(View.GONE);
            etBio.setVisibility(View.GONE);
        }
    }

    private void attemptSignup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String role = actvRole.getText().toString().trim().toLowerCase();
        String specialization = etSpecialization.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

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

        if ("student".equals(role)) {
            signupStudent(name, email);
        } else if ("counselor".equals(role)) {
            if (TextUtils.isEmpty(specialization)) {
                etSpecialization.setError("Specialization is required");
                etSpecialization.requestFocus();
                return;
            }
            signupCounselor(name, email, specialization, bio);
        } else {
            Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show();
        }
    }

    private void signupStudent(String name, String email) {
        studentRepository.getStudentByEmail(email, new FirestoreCallback<Student>() {
            @Override
            public void onSuccess(Student existingStudent) {
                if (existingStudent != null) {
                    Toast.makeText(SignupActivity.this,
                            "A student account with this email already exists.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String studentId = UUID.randomUUID().toString();
                Student student = new Student(studentId, name, email, null);

                studentRepository.addStudent(student, new FirestoreCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(SignupActivity.this,
                                "Student signup successful. Please log in.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SignupActivity.this,
                                "Signup failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SignupActivity.this,
                        "Signup failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signupCounselor(String name, String email, String specialization, String bio) {
        counselorRepository.getCounselorByEmail(email, new FirestoreCallback<Counselor>() {
            @Override
            public void onSuccess(Counselor existingCounselor) {
                if (existingCounselor != null) {
                    Toast.makeText(SignupActivity.this,
                            "A counselor account with this email already exists.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String counselorId = UUID.randomUUID().toString();
                Counselor counselor = new Counselor(
                        counselorId,
                        name,
                        email,
                        specialization,
                        bio,
                        true
                );

                counselorRepository.addCounselor(counselor, new FirestoreCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(SignupActivity.this,
                                "Counselor signup successful. Please log in.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SignupActivity.this,
                                "Signup failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SignupActivity.this,
                        "Signup failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
