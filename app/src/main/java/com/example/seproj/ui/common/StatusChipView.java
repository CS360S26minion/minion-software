package com.example.seproj.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.seproj.R;
import com.google.android.material.chip.Chip;

/**
 * Custom view for rendering appointment and feedback status labels as chips.
 * Provides consistent color treatment for booked, attended, cancelled, and related states.
 *
 * Outstanding issues:
 * - Status colors are fixed in code and could move to style resources.
 */
public class StatusChipView extends Chip {

    public StatusChipView(@NonNull Context context) {
        super(context);
    }

    public StatusChipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusChipView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStatus(String status) {
        if (status == null) status = "Unknown";
        setText(status);
        String s = status.toLowerCase();
        int bgColorRes = R.color.status_pending_bg;
        int textColorRes = R.color.status_pending;

        if (s.contains("confirm")) {
            bgColorRes = R.color.status_confirmed_bg;
            textColorRes = R.color.status_confirmed;
        } else if (s.contains("cancel")) {
            bgColorRes = R.color.status_cancelled_bg;
            textColorRes = R.color.status_cancelled;
        } else if (s.contains("complet")) {
            bgColorRes = R.color.divider;
            textColorRes = R.color.text_secondary;
        }

        setChipBackgroundColorResource(bgColorRes);
        setTextColor(ContextCompat.getColor(getContext(), textColorRes));
        setChipStrokeWidth(0);
        
        // Entrance animation specified in requirements
        setScaleX(0.7f);
        setScaleY(0.7f);
        setAlpha(0f);
        animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(180).start();
    }
}



