package com.example.seproj.service;

import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.model.Availability;
import com.example.seproj.repository.AppointmentSlotRepository;
import com.example.seproj.repository.AvailabilityRepository;
import com.example.seproj.utils.FirestoreCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Service class for counselor availability logic.
 * Saves recurring weekly availability and generates appointment slots.
 *
 * Current design:
 * - Generates 30-minute slots
 * - Generates slots for the next 4 matching weekdays
 *
 * Outstanding issues:
 * - Duplicate slot prevention can be made stricter later.
 * - Blocked periods and exceptions can be added later.
 * - Advanced recurrence rules are not included yet.
 */
public class AvailabilityService {

    public interface AvailabilityActionCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    private static final int SLOT_DURATION_MINUTES = 30;
    private static final int WEEKS_TO_GENERATE = 4;

    private final AvailabilityRepository availabilityRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;

    public AvailabilityService() {
        availabilityRepository = new AvailabilityRepository();
        appointmentSlotRepository = new AppointmentSlotRepository();
    }

    public void saveAvailabilityAndGenerateSlots(Availability availability,
                                                 AvailabilityActionCallback callback) {
        availabilityRepository.addAvailability(availability, new FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                List<AppointmentSlot> generatedSlots = generateSlotsFromAvailability(availability);

                if (generatedSlots.isEmpty()) {
                    callback.onFailure("No slots could be generated from this availability.");
                    return;
                }

                saveGeneratedSlots(generatedSlots, 0, callback);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure("Failed to save availability: " + e.getMessage());
            }
        });
    }

    private void saveGeneratedSlots(List<AppointmentSlot> slots,
                                    int index,
                                    AvailabilityActionCallback callback) {
        if (index >= slots.size()) {
            callback.onSuccess("Availability saved and slots generated successfully.");
            return;
        }

        AppointmentSlot slot = slots.get(index);
        appointmentSlotRepository.addSlot(slot, new FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                saveGeneratedSlots(slots, index + 1, callback);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure("Failed while saving generated slots: " + e.getMessage());
            }
        });
    }

    /**
     * Generates appointment slots for the next few occurrences of the chosen weekday.
     */
    private List<AppointmentSlot> generateSlotsFromAvailability(Availability availability) {
        List<AppointmentSlot> generatedSlots = new ArrayList<>();

        int dayOfWeek = availability.getDayOfWeek();
        String startTime = availability.getStartTime();
        String endTime = availability.getEndTime();

        int startHour = parseHour(startTime);
        int startMinute = parseMinute(startTime);
        int endHour = parseHour(endTime);
        int endMinute = parseMinute(endTime);

        if (startHour < 0 || startMinute < 0 || endHour < 0 || endMinute < 0) {
            return generatedSlots;
        }

        Calendar baseDate = Calendar.getInstance();

        for (int weekOffset = 0; weekOffset < WEEKS_TO_GENERATE; weekOffset++) {
            Calendar targetDate = getNextWeekdayOccurrence(dayOfWeek, weekOffset);

            Calendar slotStart = (Calendar) targetDate.clone();
            slotStart.set(Calendar.HOUR_OF_DAY, startHour);
            slotStart.set(Calendar.MINUTE, startMinute);
            slotStart.set(Calendar.SECOND, 0);
            slotStart.set(Calendar.MILLISECOND, 0);

            Calendar availabilityEnd = (Calendar) targetDate.clone();
            availabilityEnd.set(Calendar.HOUR_OF_DAY, endHour);
            availabilityEnd.set(Calendar.MINUTE, endMinute);
            availabilityEnd.set(Calendar.SECOND, 0);
            availabilityEnd.set(Calendar.MILLISECOND, 0);

            while (slotStart.before(availabilityEnd)) {
                Calendar slotEnd = (Calendar) slotStart.clone();
                slotEnd.add(Calendar.MINUTE, SLOT_DURATION_MINUTES);

                if (slotEnd.after(availabilityEnd)) {
                    break;
                }

                AppointmentSlot slot = new AppointmentSlot(
                        UUID.randomUUID().toString(),
                        availability.getCounselorId(),
                        null,
                        slotStart.getTimeInMillis(),
                        slotEnd.getTimeInMillis(),
                        AppointmentSlot.STATUS_AVAILABLE,
                        false,
                        false
                );

                generatedSlots.add(slot);
                slotStart = slotEnd;
            }
        }

        return generatedSlots;
    }

    /**
     * Returns the next occurrence of the given weekday, plus a week offset.
     * dayOfWeek follows Calendar constants:
     * 1 = Sunday, 2 = Monday, ... 7 = Saturday
     */
    private Calendar getNextWeekdayOccurrence(int dayOfWeek, int additionalWeeks) {
        Calendar cal = Calendar.getInstance();

        while (cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        cal.add(Calendar.WEEK_OF_YEAR, additionalWeeks);
        return cal;
    }

    private int parseHour(String time) {
        try {
            String[] parts = time.split(":");
            return Integer.parseInt(parts[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    private int parseMinute(String time) {
        try {
            String[] parts = time.split(":");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return -1;
        }
    }
}