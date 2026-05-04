package com.example.seproj.ui.common;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
    private EditText etPassword;
    private Spinner spinnerRole;
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
        etPassword = findViewById(R.id.etSignupPassword);
        spinnerRole = findViewById(R.id.spinnerSignupRole);
        etSpecialization = findViewById(R.id.etCounselorSpecialization);
        etBio = findViewById(R.id.etCounselorBio);
        btnSignup = findViewById(R.id.btnSignup);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        studentRepository = new StudentRepository();
        counselorRepository = new CounselorRepository();

        setupRoleDropdown();

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCounselorFieldsVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSignup.setOnClickListener(v -> attemptSignup());

        btnBackToLogin.setOnClickListener(v -> finish());

        updateCounselorFieldsVisibility();
    }

    private void setupRoleDropdown() {
        String[] roles = {"student", "counselor"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private void updateCounselorFieldsVisibility() {
        String role = spinnerRole.getSelectedItem().toString().trim().toLowerCase();
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
        String role = spinnerRole.getSelectedItem().toString().trim().toLowerCase();
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


        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 5) {
            etPassword.setError("Password must be at least 5 characters");
            etPassword.requestFocus();
            return;
        }

        if ("student".equals(role)) {
            signupStudent(name, email, password);
        } else if ("counselor".equals(role)) {
            if (TextUtils.isEmpty(specialization)) {
                etSpecialization.setError("Specialization is required");
                etSpecialization.requestFocus();
                return;
            }
            signupCounselor(name, email, specialization, bio, password);
        } else {
            Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show();
        }
    }

    private void signupStudent(String name, String email, String password) {
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
                student.setPassword(password);

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

    private void signupCounselor(String name, String email, String specialization, String bio, String password) {
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
                counselor.setPassword(password);

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
