package com.example.wayfinding;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RoomElement {
    private int x, y ;

    public RoomElement(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas){
        Paint myPaint = new Paint();
        myPaint.setColor(Color.BLACK);
        myPaint.setStrokeWidth(10);
        canvas.drawRect((float)x,(float)y, 200,200, myPaint);
    }
}
