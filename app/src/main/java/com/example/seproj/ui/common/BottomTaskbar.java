package com.example.seproj.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seproj.R;
import com.example.seproj.ui.counselor.CounselorAppointmentsActivity;
import com.example.seproj.ui.counselor.CounselorHomeActivity;
import com.example.seproj.ui.counselor.SetAvailabilityActivity;
import com.example.seproj.ui.student.CounselorListActivity;
import com.example.seproj.ui.student.StudentAppointmentsActivity;
import com.example.seproj.ui.student.StudentHomeActivity;

/**
 * Programmatically attaches role-specific bottom navigation to student and counselor screens.
 * Keeps common navigation consistent without duplicating XML in every layout.
 *
 * Outstanding issues:
 * - Selected-state highlighting is not yet tied to the current activity.
 */
public final class BottomTaskbar {
    private static final int TASKBAR_TAG = 0x7100BABA;
    private static final int CONTENT_PADDING_TAG = 0x7100BABB;

    private BottomTaskbar() {
    }

    public static void attachStudent(Activity activity, String studentId, String studentName) {
        NavItem[] items = new NavItem[]{
                new NavItem("Home", "\uD83C\uDFE0", "#FDEAE6", () -> openStudentHome(activity, studentId, studentName)),
                new NavItem("Book", "\uD83D\uDCC5", "#EBF5F0", () -> openCounselors(activity, studentId, studentName)),
                new NavItem("Appts", "\u2705", "#EEF3FA", () -> openStudentAppointments(activity, studentId, studentName)),
                new NavItem("Alerts", "\uD83D\uDD14", "#FEF4E3", () -> openStudentNotifications(activity, studentId, studentName)),
                new NavItem("Chat", "\uD83D\uDCAC", "#FDEAE6", () -> openChat(activity, studentId, studentName))
        };
        attach(activity, items);
    }

    public static void attachCounselor(Activity activity, String counselorId, String counselorName) {
        NavItem[] items = new NavItem[]{
                new NavItem("Home", "\uD83C\uDFE0", "#FDEAE6", () -> openCounselorHome(activity, counselorId, counselorName)),
                new NavItem("Appts", "\uD83D\uDCCB", "#EEF3FA", () -> openCounselorAppointments(activity, counselorId, counselorName)),
                new NavItem("Hours", "\uD83D\uDD52", "#EBF5F0", () -> openSetHours(activity, counselorId, counselorName)),
                new NavItem("Alerts", "\uD83D\uDD14", "#FEF4E3", () -> openCounselorNotifications(activity, counselorId, counselorName))
        };
        attach(activity, items);
    }

    private static void attach(Activity activity, NavItem[] items) {
        FrameLayout content = activity.findViewById(android.R.id.content);
        if (content == null || content.findViewWithTag(TASKBAR_TAG) != null) {
            return;
        }

        View root = content.getChildCount() > 0 ? content.getChildAt(0) : null;
        if (root != null && root.getTag(CONTENT_PADDING_TAG) == null) {
            root.setPadding(
                    root.getPaddingLeft(),
                    root.getPaddingTop(),
                    root.getPaddingRight(),
                    root.getPaddingBottom() + dp(activity, 98)
            );
            root.setTag(CONTENT_PADDING_TAG);
        }

        LinearLayout bar = new LinearLayout(activity);
        bar.setTag(TASKBAR_TAG);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER);
        bar.setPadding(dp(activity, 12), dp(activity, 8), dp(activity, 12), dp(activity, 8));
        bar.setBackground(makeBarBackground(activity));
        bar.setElevation(dp(activity, 12));

        FrameLayout.LayoutParams barParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(activity, 82),
                Gravity.BOTTOM
        );
        barParams.setMargins(dp(activity, 22), 0, dp(activity, 22), dp(activity, 16));
        content.addView(bar, barParams);

        for (NavItem item : items) {
            bar.addView(makeButton(activity, item));
        }
    }

    private static TextView makeButton(Activity activity, NavItem item) {
        TextView button = new TextView(activity);
        button.setText(item.icon + "\n" + item.label);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(11);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(activity.getColor(R.color.text_primary));
        button.setSingleLine(false);
        button.setMaxLines(2);
        button.setLineSpacing(2, 1);
        button.setBackground(makeButtonBackground(activity, item.backgroundColor));
        button.setPadding(dp(activity, 4), 0, dp(activity, 4), 0);
        button.setOnClickListener(v -> item.action.run());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
        );
        params.setMargins(dp(activity, 4), 0, dp(activity, 4), 0);
        button.setLayoutParams(params);
        return button;
    }

    private static GradientDrawable makeBarBackground(Activity activity) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(activity.getColor(R.color.surface));
        drawable.setCornerRadius(dp(activity, 28));
        drawable.setStroke(dp(activity, 1), activity.getColor(R.color.divider));
        return drawable;
    }

    private static GradientDrawable makeButtonBackground(Activity activity, String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(color));
        drawable.setCornerRadius(dp(activity, 20));
        return drawable;
    }

    private static void openStudentHome(Activity activity, String studentId, String studentName) {
        Intent intent = new Intent(activity, StudentHomeActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    private static void openCounselors(Activity activity, String studentId, String studentName) {
        Intent intent = new Intent(activity, CounselorListActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        activity.startActivity(intent);
    }

    private static void openStudentAppointments(Activity activity, String studentId, String studentName) {
        Intent intent = new Intent(activity, StudentAppointmentsActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        activity.startActivity(intent);
    }

    private static void openChat(Activity activity, String studentId, String studentName) {
        Intent intent = new Intent(activity, AiChatbotActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        activity.startActivity(intent);
    }

    private static void openStudentNotifications(Activity activity, String studentId, String studentName) {
        Intent intent = new Intent(activity, NotificationsActivity.class);
        intent.putExtra("recipientId", studentId);
        intent.putExtra("recipientRole", "student");
        intent.putExtra("displayName", studentName);
        activity.startActivity(intent);
    }

    private static void openCounselorHome(Activity activity, String counselorId, String counselorName) {
        Intent intent = new Intent(activity, CounselorHomeActivity.class);
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    private static void openCounselorAppointments(Activity activity, String counselorId, String counselorName) {
        Intent intent = new Intent(activity, CounselorAppointmentsActivity.class);
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        activity.startActivity(intent);
    }

    private static void openSetHours(Activity activity, String counselorId, String counselorName) {
        Intent intent = new Intent(activity, SetAvailabilityActivity.class);
        intent.putExtra("counselorId", counselorId);
        intent.putExtra("counselorName", counselorName);
        activity.startActivity(intent);
    }

    private static void openCounselorNotifications(Activity activity, String counselorId, String counselorName) {
        Intent intent = new Intent(activity, NotificationsActivity.class);
        intent.putExtra("recipientId", counselorId);
        intent.putExtra("recipientRole", "counselor");
        intent.putExtra("displayName", counselorName);
        activity.startActivity(intent);
    }

    private static int dp(Activity activity, int value) {
        return (int) (value * activity.getResources().getDisplayMetrics().density);
    }

    private static final class NavItem {
        final String label;
        final String icon;
        final String backgroundColor;
        final Runnable action;

        NavItem(String label, String icon, String backgroundColor, Runnable action) {
            this.label = label;
            this.icon = icon;
            this.backgroundColor = backgroundColor;
            this.action = action;
        }
    }
}

