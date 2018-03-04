package com.example.m.androidpenza;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class AlphaTransformation extends BitmapTransformation {

    private static final String ID = "com.example.m.androidpenza.AlphaTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private static final String TAG = "AlphaTransformation";

    private int color;

    public AlphaTransformation(int color) {
        this.color = color;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Log.d(TAG, "Transform");
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        return bitmap;
    }

    @Override
    public String toString() {
        return "AlphaTransformation(color=" + color + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AlphaTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
