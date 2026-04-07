package com.example.seproj.ui.counselor;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seproj.R;
import com.example.seproj.model.Availability;
import com.example.seproj.service.AvailabilityService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Allows a counselor to define recurring weekly availability
 * and automatically generate appointment slots.
 *
 * Outstanding issues:
 * - Blocked periods are not included yet.
 * - Existing generated slots are not deduplicated yet.
 */
public class SetAvailabilityActivity extends AppCompatActivity {

    private TextView tvSetAvailabilityTitle;
    private AutoCompleteTextView actvDayOfWeek;
    private TextView tvSelectedStartTime;
    private TextView tvSelectedEndTime;
    private Button btnPickStartTime;
    private Button btnPickEndTime;
    private Button btnSaveAvailability;
    private Button btnBack;
    private Button btnHome;
    private AvailabilityService availabilityService;

    private String counselorId;
    private String counselorName;

    private String selectedStartTime;
    private String selectedEndTime;

    private final Map<String, Integer> dayMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_availability);
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        tvSetAvailabilityTitle = findViewById(R.id.tvSetAvailabilityTitle);
        actvDayOfWeek = findViewById(R.id.actvDayOfWeek);
        tvSelectedStartTime = findViewById(R.id.tvSelectedStartTime);
        tvSelectedEndTime = findViewById(R.id.tvSelectedEndTime);
        btnPickStartTime = findViewById(R.id.btnPickStartTime);
        btnPickEndTime = findViewById(R.id.btnPickEndTime);
        btnSaveAvailability = findViewById(R.id.btnSaveAvailability);

        counselorId = getIntent().getStringExtra("counselorId");
        counselorName = getIntent().getStringExtra("counselorName");

        if (counselorName != null && !counselorName.trim().isEmpty()) {
            tvSetAvailabilityTitle.setText("Set Availability - " + counselorName);
        }

        availabilityService = new AvailabilityService();

        setupDayDropdown();
        setupDayMap();

        btnPickStartTime.setOnClickListener(v -> showTimePicker(true));
        btnPickEndTime.setOnClickListener(v -> showTimePicker(false));
        btnSaveAvailability.setOnClickListener(v -> saveAvailability());
        btnBack.setOnClickListener(v -> finish());

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(SetAvailabilityActivity.this, CounselorHomeActivity.class);
            intent.putExtra("counselorId", counselorId);
            intent.putExtra("counselorName", counselorName);
            startActivity(intent);
            finish();
        });

    }

    private void setupDayDropdown() {
        String[] days = {
                "Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                days
        );
        actvDayOfWeek.setAdapter(adapter);
    }

    private void setupDayMap() {
        dayMap.put("Sunday", Calendar.SUNDAY);
        dayMap.put("Monday", Calendar.MONDAY);
        dayMap.put("Tuesday", Calendar.TUESDAY);
        dayMap.put("Wednesday", Calendar.WEDNESDAY);
        dayMap.put("Thursday", Calendar.THURSDAY);
        dayMap.put("Friday", Calendar.FRIDAY);
        dayMap.put("Saturday", Calendar.SATURDAY);
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String formatted = String.format(Locale.getDefault(),
                            "%02d:%02d", selectedHour, selectedMinute);

                    if (isStartTime) {
                        selectedStartTime = formatted;
                        tvSelectedStartTime.setText("Start Time: " + formatted);
                    } else {
                        selectedEndTime = formatted;
                        tvSelectedEndTime.setText("End Time: " + formatted);
                    }
                },
                hour,
                minute,
                false
        );

        dialog.show();
    }

    private void saveAvailability() {
        String selectedDay = actvDayOfWeek.getText().toString().trim();

        if (TextUtils.isEmpty(selectedDay)) {
            actvDayOfWeek.setError("Please select a day");
            actvDayOfWeek.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(selectedStartTime)) {
            Toast.makeText(this, "Please choose a start time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedEndTime)) {
            Toast.makeText(this, "Please choose an end time", Toast.LENGTH_SHORT).show();
            return;
        }

        int dayOfWeek = dayMap.getOrDefault(selectedDay, -1);
        if (dayOfWeek == -1) {
            Toast.makeText(this, "Invalid day selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isEndAfterStart(selectedStartTime, selectedEndTime)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_LONG).show();
            return;
        }

        Availability availability = new Availability(
                UUID.randomUUID().toString(),
                counselorId,
                dayOfWeek,
                selectedStartTime,
                selectedEndTime,
                false
        );

        btnSaveAvailability.setEnabled(false);

        availabilityService.saveAvailabilityAndGenerateSlots(
                availability,
                new AvailabilityService.AvailabilityActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        btnSaveAvailability.setEnabled(true);
                        Toast.makeText(SetAvailabilityActivity.this, message, Toast.LENGTH_LONG).show();

                        actvDayOfWeek.setText("");
                        selectedStartTime = null;
                        selectedEndTime = null;
                        tvSelectedStartTime.setText("Start Time: Not Selected");
                        tvSelectedEndTime.setText("End Time: Not Selected");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        btnSaveAvailability.setEnabled(true);
                        Toast.makeText(SetAvailabilityActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private boolean isEndAfterStart(String start, String end) {
        try {
            String[] startParts = start.split(":");
            String[] endParts = end.split(":");

            int startMinutes = Integer.parseInt(startParts[0]) * 60 + Integer.parseInt(startParts[1]);
            int endMinutes = Integer.parseInt(endParts[0]) * 60 + Integer.parseInt(endParts[1]);

            return endMinutes > startMinutes;
        } catch (Exception e) {
            return false;
        }
    }
}