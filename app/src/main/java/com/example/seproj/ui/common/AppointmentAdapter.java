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
 * RecyclerView adapter for displaying appointments.
 * Can be used in both student and counselor views.
 *
 * Outstanding issues:
 * - Counselor/student names can be added later through lookup joins.
 * - Reschedule action can be added later.
 */
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    public interface OnAppointmentActionListener {
        void onCancelClick(AppointmentSlot slot);
    }

    private final List<AppointmentSlot> appointmentList;
    private final OnAppointmentActionListener listener;
    private final boolean showCancelButton;

    public AppointmentAdapter(OnAppointmentActionListener listener, boolean showCancelButton) {
        this.appointmentList = new ArrayList<>();
        this.listener = listener;
        this.showCancelButton = showCancelButton;
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
        holder.bind(slot, listener, showCancelButton);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAppointmentDate;
        private final TextView tvAppointmentTime;
        private final TextView tvAppointmentStatus;
        private final Button btnCancelAppointment;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
        }

        public void bind(AppointmentSlot slot,
                         OnAppointmentActionListener listener,
                         boolean showCancelButton) {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat =
                    new SimpleDateFormat("hh:mm a", Locale.getDefault());

            String dateText = dateFormat.format(new Date(slot.getStartTimeMillis()));
            String startText = timeFormat.format(new Date(slot.getStartTimeMillis()));
            String endText = timeFormat.format(new Date(slot.getEndTimeMillis()));

            tvAppointmentDate.setText(dateText);
            tvAppointmentTime.setText(startText + " - " + endText);
            tvAppointmentStatus.setText("Status: " + slot.getStatus());

            if (showCancelButton) {
                btnCancelAppointment.setVisibility(View.VISIBLE);
                btnCancelAppointment.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onCancelClick(slot);
                    }
                });
            } else {
                btnCancelAppointment.setVisibility(View.GONE);
                btnCancelAppointment.setOnClickListener(null);
            }
        }
    }
}