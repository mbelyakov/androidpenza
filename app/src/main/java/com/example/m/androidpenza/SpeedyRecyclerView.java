package com.example.m.androidpenza;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

class SpeedyRecyclerView extends RecyclerView {

    private int factor = 1;

    public SpeedyRecyclerView(Context context) {
        super(context);
    }

    public SpeedyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFlingFactor(int factor) {
        this.factor = factor;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return super.fling(velocityX, velocityY * factor);
    }
}
