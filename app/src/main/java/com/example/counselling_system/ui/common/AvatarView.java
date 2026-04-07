package com.example.counselling_system.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.counselling_system.R;

public class AvatarView extends AppCompatImageView {
    private String initials = "";
    private int bgColor = Color.GRAY;
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect textBounds = new Rect();

    private final int[] PALETTE = {
        0xFFE8715A, 0xFFF4A942, 0xFF6BAF92, 0xFFD9534F, 0xFF5C8A8A, 0xFF965C8A
    };

    public AvatarView(@NonNull Context context) {
        super(context);
        init();
    }

    public AvatarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
    }

    public void bind(String name, String imageUrl) {
        generateInitials(name);
        if (name != null) {
            bgColor = PALETTE[Math.abs(name.hashCode()) % PALETTE.length];
        }
        setImageResource(0); // clear image
        invalidate();
    }

    private void generateInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            initials = "?";
            return;
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length > 1) {
            initials = parts[0].substring(0, 1).toUpperCase() + parts[parts.length - 1].substring(0, 1).toUpperCase();
        } else {
            initials = parts[0].substring(0, 1).toUpperCase();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            bgPaint.setColor(bgColor);
            float cx = getWidth() / 2f;
            float cy = getHeight() / 2f;
            float radius = Math.min(cx, cy);
            canvas.drawCircle(cx, cy, radius, bgPaint);
            
            textPaint.setTextSize(radius * 0.8f);
            textPaint.getTextBounds(initials, 0, initials.length(), textBounds);
            canvas.drawText(initials, cx, cy - textBounds.exactCenterY(), textPaint);
        } else {
            super.onDraw(canvas);
        }
    }
}
