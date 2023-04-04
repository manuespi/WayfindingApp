package com.example.wayfinding;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MapCanvas extends View {

//    public MapCanvas(Context context) {
//        super(context);
//    }

    Paint paintR, paintD;
    Path path;
    int roomLength = 900, roomWidth = 600;
    int viewWidth, viewHeight;

    public MapCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        paintR = new Paint();
        paintD = new Paint();
        path = new Path();

        //room
        paintR.setColor(Color.GRAY);
        paintR.setAntiAlias(true);
        paintR.setStrokeWidth(10);
        paintR.setStyle(Paint.Style.STROKE);
        // paintR.setStrokeJoin(Paint.Join.ROUND);
        // paintR.setStrokeCap(Paint.Cap.ROUND);

        //door
        paintD.setAntiAlias(true);
        paintD.setColor(Color.RED);
        paintD.setStrokeJoin(Paint.Join.ROUND); //cuando retiramos dedo de pantalla, se queda round
        paintD.setStrokeCap(Paint.Cap.ROUND);
        paintD.setStyle(Paint.Style.STROKE);
        paintD.setStrokeWidth(20f);





    }

//    public MapCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public MapCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paintD);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX(); //coge pixel donde toca nuestro dedo
        float yPos = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                return false;
        }

        invalidate();
        return true;

    }

    void setRoomLength(int x){
        roomLength = x;
    }

    void setRoomWidth(int y){
        roomLength = y;
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        viewWidth = xNew;
        viewHeight = yNew;
    }
}
