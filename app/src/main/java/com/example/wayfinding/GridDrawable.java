package com.example.wayfinding;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class GridDrawable extends Drawable {

    private Paint linePaint;

    public GridDrawable(int color) {
        this.linePaint = new Paint();
        this.linePaint.setColor(color);
        this.linePaint.setStrokeWidth(2);
    }

    @Override
    public void draw(Canvas canvas) {
        int width = getBounds().right;
        int height = getBounds().bottom;

        // lineas horizontales
        for (int y = 0; y < height; y += 40) {
            canvas.drawLine(0, y, width, y, linePaint);
        }

        // lineas verticales
        for (int x = 0; x < width; x += 40) {
            canvas.drawLine(x, 0, x, height, linePaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
