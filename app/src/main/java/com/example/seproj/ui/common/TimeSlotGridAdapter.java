package com.example.seproj.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.AppointmentSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSlotGridAdapter extends RecyclerView.Adapter<TimeSlotGridAdapter.SlotViewHolder> {

    private List<AppointmentSlot> slots = new ArrayList<>();
    private AppointmentSlot selectedSlot = null;
    private final OnSlotSelectedListener listener;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public interface OnSlotSelectedListener {
        void onSlotSelected(AppointmentSlot slot);
    }

    public TimeSlotGridAdapter(OnSlotSelectedListener listener) {
        this.listener = listener;
    }

    public void setSlots(List<AppointmentSlot> newSlots) {
        this.slots = newSlots != null ? newSlots : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSelectedSlot(AppointmentSlot slot) {
        AppointmentSlot oldSlot = this.selectedSlot;
        this.selectedSlot = slot;
        if (oldSlot != null) notifyItemChanged(slots.indexOf(oldSlot));
        if (slot != null) notifyItemChanged(slots.indexOf(slot));
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_time_slot, parent, false);
        return new SlotViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        AppointmentSlot slot = slots.get(position);
        boolean isSelected = slot.equals(selectedSlot);
        // Assuming there isn't a direct isBooked in AppointmentSlot but we fallback to enabled
        boolean isAvailable = true; 

        String timeText = timeFormat.format(new Date(slot.getStartTimeMillis()));
        holder.tvTimeLabel.setText(timeText);
        
        holder.tvTimeLabel.setSelected(isSelected);
        holder.tvTimeLabel.setEnabled(isAvailable);

        if (isAvailable) {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSlotSelected(slot);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeLabel;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeLabel = (TextView) itemView;
        }
    }
}
