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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for available appointment slots.
 */
public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder> {

    public interface OnSlotClickListener {
        void onBookClick(AppointmentSlot slot);
    }

    private List<AppointmentSlot> slotList;
    private final OnSlotClickListener listener;

    public SlotAdapter(List<AppointmentSlot> slotList, OnSlotClickListener listener) {
        this.slotList = slotList;
        this.listener = listener;
    }

    public void updateSlots(List<AppointmentSlot> newSlots) {
        this.slotList = newSlots;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slot, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        AppointmentSlot slot = slotList.get(position);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat =
                new SimpleDateFormat("hh:mm a", Locale.getDefault());

        String dateText = dateFormat.format(new Date(slot.getStartTimeMillis()));
        String startText = timeFormat.format(new Date(slot.getStartTimeMillis()));
        String endText = timeFormat.format(new Date(slot.getEndTimeMillis()));

        holder.tvSlotDate.setText(dateText);
        holder.tvSlotTime.setText(startText + " - " + endText);

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(slot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return slotList != null ? slotList.size() : 0;
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotDate;
        TextView tvSlotTime;
        Button btnBook;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotDate = itemView.findViewById(R.id.tvSlotDate);
            tvSlotTime = itemView.findViewById(R.id.tvSlotTime);
            btnBook = itemView.findViewById(R.id.btnBookSlot);
        }
    }
}