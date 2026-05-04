package com.example.seproj.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.FeedbackForm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for feedback records displayed to administrators.
 * Presents feedback metadata and forwards selected records to detail screens.
 *
 * Outstanding issues:
 * - Feedback grouping and filtering are handled outside the adapter.
 */
public class AdminFeedbackAdapter extends RecyclerView.Adapter<AdminFeedbackAdapter.FeedbackViewHolder> {

    public interface OnFeedbackClickListener {
        void onFeedbackClick(FeedbackForm feedback);
    }

    private final List<FeedbackForm> feedbackList = new ArrayList<>();
    private final OnFeedbackClickListener listener;

    public AdminFeedbackAdapter(OnFeedbackClickListener listener) {
        this.listener = listener;
    }

    public void setFeedbackList(List<FeedbackForm> list) {
        feedbackList.clear();

        if (list != null) {
            feedbackList.addAll(list);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        holder.bind(feedbackList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFeedbackTitle;
        private TextView tvFeedbackDate;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFeedbackTitle = itemView.findViewById(R.id.tvFeedbackTitle);
            tvFeedbackDate = itemView.findViewById(R.id.tvFeedbackDate);
        }

        void bind(FeedbackForm feedback, OnFeedbackClickListener listener) {
            String type = feedback.isCounselorFeedback()
                    ? "Counselor Feedback"
                    : "Student Feedback";

            String counselorName = safe(feedback.getCounselorName(), "Counselor");
            tvFeedbackTitle.setText(type + " - " + counselorName + " - Rating: " + feedback.getRating());

            String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    .format(new Date(feedback.getSubmittedAt()));

            tvFeedbackDate.setText(date);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFeedbackClick(feedback);
                }
            });
        }

        private String safe(String value, String fallback) {
            if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim())) {
                return fallback;
            }
            return value.trim();
        }
    }
}



