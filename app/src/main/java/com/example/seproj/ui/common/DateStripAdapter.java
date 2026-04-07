package com.example.seproj.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateStripAdapter extends RecyclerView.Adapter<DateStripAdapter.DateViewHolder> {

    private List<LocalDate> dates = new ArrayList<>();
    private Set<LocalDate> availableDates = new HashSet<>();
    private LocalDate selectedDate = null;
    private final OnDateSelectedListener listener;

    public interface OnDateSelectedListener {
        void onDateSelected(LocalDate date);
    }

    public DateStripAdapter(OnDateSelectedListener listener) {
        this.listener = listener;
        // Pre-populate upcoming 30 days
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            dates.add(today.plusDays(i));
        }
    }

    public void setAvailableDates(Set<LocalDate> availableDates) {
        this.availableDates = availableDates != null ? availableDates : new HashSet<>();
        notifyDataSetChanged();
    }

    public void setSelectedDate(LocalDate date) {
        LocalDate oldDate = selectedDate;
        this.selectedDate = date;
        if (oldDate != null) notifyItemChanged(dates.indexOf(oldDate));
        if (date != null) notifyItemChanged(dates.indexOf(date));
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_date_pill, parent, false);
        return new DateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        LocalDate date = dates.get(position);
        boolean isSelected = date.equals(selectedDate);
        boolean isAvailable = availableDates.contains(date);

        holder.tvDayAbbr.setText(date.format(DateTimeFormatter.ofPattern("EEE")).toUpperCase());
        holder.tvDateNumber.setText(String.valueOf(date.getDayOfMonth()));

        holder.itemView.setSelected(isSelected);
        holder.itemView.setEnabled(isAvailable);

        if (!isAvailable) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(v -> {
            if (isAvailable && listener != null) {
                listener.onDateSelected(date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayAbbr;
        TextView tvDateNumber;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayAbbr = itemView.findViewById(R.id.tvDayAbbr);
            tvDateNumber = itemView.findViewById(R.id.tvDateNumber);
        }
    }
}
