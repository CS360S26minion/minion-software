package com.example.seproj.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.Counselor;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying counselor cards.
 * Used by the student-side counselor browsing screen.
 */
public class CounselorAdapter extends RecyclerView.Adapter<CounselorAdapter.CounselorViewHolder> {

    public interface OnCounselorClickListener {
        void onCounselorClick(Counselor counselor);
    }

    private final List<Counselor> counselorList;
    private final OnCounselorClickListener listener;

    public CounselorAdapter(OnCounselorClickListener listener) {
        this.counselorList = new ArrayList<>();
        this.listener = listener;
    }

    public void setCounselorList(List<Counselor> counselors) {
        counselorList.clear();
        if (counselors != null) {
            counselorList.addAll(counselors);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CounselorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_counselor, parent, false);
        return new CounselorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounselorViewHolder holder, int position) {
        Counselor counselor = counselorList.get(position);
        holder.bind(counselor, listener);
    }

    @Override
    public int getItemCount() {
        return counselorList.size();
    }

    static class CounselorViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCounselorName;
        private final TextView tvCounselorSpecialization;
        private final TextView tvCounselorBio;
        private final TextView tvCounselorStatus;
        private final TextView tvCounselorRating;
        private final TextView tvCounselorAvatar;

        public CounselorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCounselorName = itemView.findViewById(R.id.tvCounselorName);
            tvCounselorSpecialization = itemView.findViewById(R.id.tvCounselorSpecialization);
            tvCounselorBio = itemView.findViewById(R.id.tvCounselorBio);
            tvCounselorStatus = itemView.findViewById(R.id.tvCounselorStatus);
            tvCounselorRating = itemView.findViewById(R.id.tvCounselorRating);
            tvCounselorAvatar = itemView.findViewById(R.id.tvCounselorAvatar);
        }

        public void bind(Counselor counselor, OnCounselorClickListener listener) {
            tvCounselorName.setText(counselor.getName());
            String name = counselor.getName() == null ? "" : counselor.getName().trim();
            tvCounselorAvatar.setText(name.isEmpty() ? "G" : name.substring(0, 1).toUpperCase());
            tvCounselorSpecialization.setText("Specialization: " + counselor.getSpecialization());

            String bio = counselor.getBio();
            if (bio == null || bio.trim().isEmpty()) {
                tvCounselorBio.setText("No bio provided.");
            } else {
                tvCounselorBio.setText(bio);
            }

            tvCounselorStatus.setText(counselor.isActive() ? "Available Counselor" : "Inactive");
            if (counselor.getRatingCount() > 0) {
                tvCounselorRating.setText(String.format(
                        java.util.Locale.getDefault(),
                        "Rating: %.1f/5 (%d)",
                        counselor.getAverageRating(),
                        counselor.getRatingCount()
                ));
            } else {
                tvCounselorRating.setText("Rating: Not rated yet");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCounselorClick(counselor);
                }
            });
        }
    }
}
