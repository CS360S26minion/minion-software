package com.example.seproj.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

import com.example.seproj.R;

/**
 * Custom view for displaying rating stars in a compact, reusable form.
 * Used in counselor listings and feedback-related UI where rating visualization is needed.
 *
 * Outstanding issues:
 * - It is display-oriented and does not replace interactive RatingBar input.
 */
public class StarRatingView extends LinearLayout {

    private int rating = 0;
    private boolean isInteractive = false;
    private final ImageView[] stars = new ImageView[5];

    public StarRatingView(Context context) {
        super(context);
        init();
    }

    public StarRatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        
        int size = getResources().getDimensionPixelSize(R.dimen.icon_size_medium);
        
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMarginEnd(4);
            star.setLayoutParams(params);
            star.setImageResource(R.drawable.ic_star_outline);
            
            final int index = i;
            star.setOnClickListener(v -> {
                if (isInteractive) {
                    setRating(index + 1);
                    animateStarPress(star);
                }
            });
            
            stars[i] = star;
            addView(star);
        }
    }

    public void setInteractive(boolean interactive) {
        this.isInteractive = interactive;
    }

    public void setRating(int newRating) {
        this.rating = Math.max(0, Math.min(newRating, 5));
        
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
                stars[i].setColorFilter(getResources().getColor(R.color.star_active));
            } else {
                stars[i].setImageResource(R.drawable.ic_star_outline);
                stars[i].setColorFilter(getResources().getColor(R.color.star_inactive));
            }
        }
    }

    public int getRating() {
        return rating;
    }

    private void animateStarPress(View star) {
        star.animate().cancel();
        star.setScaleX(1f);
        star.setScaleY(1f);
        star.animate()
            .scaleX(1.3f).scaleY(1.3f)
            .setDuration(100)
            .withEndAction(() -> star.animate().scaleX(1f).scaleY(1f).setDuration(100).start())
            .start();
    }
}



