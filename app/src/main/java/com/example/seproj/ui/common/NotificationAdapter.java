package com.example.seproj.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seproj.R;
import com.example.seproj.model.AppNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for displaying in-app notifications.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<AppNotification> notificationList = new ArrayList<>();

    public void setNotificationList(List<AppNotification> notifications) {
        notificationList.clear();
        if (notifications != null) {
            notificationList.addAll(notifications);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNotificationTitle;
        private final TextView tvNotificationMessage;
        private final TextView tvNotificationType;
        private final TextView tvNotificationTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationType = itemView.findViewById(R.id.tvNotificationType);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
        }

        public void bind(AppNotification notification) {
            tvNotificationTitle.setText(notification.getTitle());
            tvNotificationMessage.setText(notification.getMessage());
            tvNotificationType.setText("Type: " + notification.getType());

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault());
            tvNotificationTime.setText(sdf.format(new Date(notification.getCreatedAtMillis())));
        }
    }
}