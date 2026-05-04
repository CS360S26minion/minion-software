package com.example.seproj.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.AppointmentSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for appointment cards across student and counselor screens.
 * Configures action buttons for cancellation, feedback, intake forms, no-shows, and AI insight access.
 *
 * Outstanding issues:
 * - The adapter carries role-specific branching and could be split if card behavior grows.
 */
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    public interface OnAppointmentActionListener {
        void onAction(AppointmentSlot slot);
    }

    private final List<AppointmentSlot> appointmentList = new ArrayList<>();
    private final boolean isCounselorMode;
    private final OnAppointmentActionListener cancelListener;
    private final OnAppointmentActionListener feedbackListener;
    private final boolean showCancelButton;

    private final OnAppointmentActionListener intakeListener;
    private final OnAppointmentActionListener aiInsightListener;
    


    public AppointmentAdapter(OnAppointmentActionListener cancelListener,
                              OnAppointmentActionListener feedbackListener,
                              OnAppointmentActionListener intakeListener,
                              OnAppointmentActionListener aiInsightListener,
                              boolean showCancelButton,
                              boolean isCounselorMode) {
        this.cancelListener = cancelListener;
        this.feedbackListener = feedbackListener;
        this.intakeListener = intakeListener;
        this.aiInsightListener = aiInsightListener;
        this.showCancelButton = showCancelButton;
        this.isCounselorMode = isCounselorMode;
    }

    public void setAppointmentList(List<AppointmentSlot> appointments) {
        appointmentList.clear();

        if (appointments != null) {
            appointmentList.addAll(appointments);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentSlot slot = appointmentList.get(position);
//        holder.bind(slot, cancelListener, feedbackListener, showCancelButton);
        holder.bind(slot, cancelListener, feedbackListener, intakeListener, aiInsightListener, showCancelButton, isCounselorMode);
    }



    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvAppointmentDate;
        private final TextView tvAppointmentTime;
        private final TextView tvAppointmentCounselor;
        private final TextView tvAppointmentStatus;
        private final Button btnCancelAppointment;
        private final Button btnFeedback;

        private final Button btnViewIntake;
        private final Button btnAiInsight;

        AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentCounselor = itemView.findViewById(R.id.tvAppointmentCounselor);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
            btnFeedback = itemView.findViewById(R.id.btnFeedback);
            btnViewIntake = itemView.findViewById(R.id.btnViewIntake);
            btnAiInsight = itemView.findViewById(R.id.btnAiInsight);
        }

        void bind(AppointmentSlot slot,
                  OnAppointmentActionListener cancelListener,
                  OnAppointmentActionListener feedbackListener,
                  OnAppointmentActionListener intakeListener,
                  OnAppointmentActionListener aiInsightListener,
                  boolean showCancelButton,
                  boolean isCounselorMode) {

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat =
                    new SimpleDateFormat("hh:mm a", Locale.getDefault());

            String dateText = dateFormat.format(new Date(slot.getStartTimeMillis()));
            String startText = timeFormat.format(new Date(slot.getStartTimeMillis()));
            String endText = timeFormat.format(new Date(slot.getEndTimeMillis()));

            tvAppointmentDate.setText(dateText);
            tvAppointmentTime.setText(startText + " - " + endText);
            String counselorName = slot.getCounselorName();
            if (!isCounselorMode && counselorName != null && !counselorName.trim().isEmpty()) {
                tvAppointmentCounselor.setVisibility(View.VISIBLE);
                tvAppointmentCounselor.setText("Counselor: " + counselorName.trim());
            } else {
                tvAppointmentCounselor.setVisibility(View.GONE);
            }
            long now = System.currentTimeMillis();
            String displayStatus;

            if (slot.getEndTimeMillis() < now) {
                if (slot.isNoShow()) {
                    displayStatus = "No-show";
                } else {
                    displayStatus = "Attended";
                }
            } else {
                displayStatus = slot.getStatus();
            }

            tvAppointmentStatus.setText("Status: " + displayStatus);


            if (showCancelButton && cancelListener != null) {

                if (isCounselorMode) {
                    boolean hasStarted = slot.getStartTimeMillis() <= now;
                    boolean isBooked = AppointmentSlot.STATUS_BOOKED.equals(slot.getStatus());
                    boolean isAvailableFuture = AppointmentSlot.STATUS_AVAILABLE.equals(slot.getStatus())
                            && slot.getStartTimeMillis() > now;

                    if (isBooked || isAvailableFuture) {
                        btnCancelAppointment.setVisibility(View.VISIBLE);
                        if (isAvailableFuture) {
                            btnCancelAppointment.setText("Cancel Slot");
                        } else {
                            btnCancelAppointment.setText(hasStarted ? "Mark No-Show" : "Cancel Appointment");
                        }
                        btnCancelAppointment.setOnClickListener(v -> cancelListener.onAction(slot));
                    } else {
                        btnCancelAppointment.setVisibility(View.GONE);
                        btnCancelAppointment.setOnClickListener(null);
                    }

                } else {
                    btnCancelAppointment.setVisibility(View.VISIBLE);
                    btnCancelAppointment.setText("Cancel");
                    btnCancelAppointment.setOnClickListener(v -> cancelListener.onAction(slot));
                }

            } else {
                btnCancelAppointment.setVisibility(View.GONE);
                btnCancelAppointment.setOnClickListener(null);
            }

            if (feedbackListener != null) {
                boolean hasPassed = slot.getEndTimeMillis() < System.currentTimeMillis();
                boolean isNoShow = AppointmentSlot.STATUS_NO_SHOW.equals(slot.getStatus());

                boolean alreadySubmitted = isCounselorMode
                        ? slot.isCounselorFeedbackSubmitted()
                        : slot.isStudentFeedbackSubmitted();

                boolean shouldShowFeedback;

                if (isCounselorMode) {
                    shouldShowFeedback = hasPassed && !alreadySubmitted;
                } else {
                    shouldShowFeedback = hasPassed && !alreadySubmitted && !isNoShow;
                }

                btnFeedback.setVisibility(shouldShowFeedback ? View.VISIBLE : View.GONE);

                if (shouldShowFeedback) {
                    btnFeedback.setOnClickListener(v -> feedbackListener.onAction(slot));
                } else {
                    btnFeedback.setOnClickListener(null);
                }

            } else {
                btnFeedback.setVisibility(View.GONE);
                btnFeedback.setOnClickListener(null);
            }
            if (isCounselorMode && intakeListener != null) {
                btnViewIntake.setVisibility(View.VISIBLE);
                btnViewIntake.setOnClickListener(v -> intakeListener.onAction(slot));
            } else {
                btnViewIntake.setVisibility(View.GONE);
                btnViewIntake.setOnClickListener(null);
            }

            boolean shouldShowAiInsight = aiInsightListener != null
                    && slot.getEndTimeMillis() < now
                    && !AppointmentSlot.STATUS_NO_SHOW.equals(slot.getStatus());

            btnAiInsight.setVisibility(shouldShowAiInsight ? View.VISIBLE : View.GONE);
            if (shouldShowAiInsight) {
                btnAiInsight.setOnClickListener(v -> aiInsightListener.onAction(slot));
            } else {
                btnAiInsight.setOnClickListener(null);
            }
        }
    }
}



