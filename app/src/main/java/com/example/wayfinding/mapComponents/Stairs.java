package com.example.wayfinding.mapComponents;

import android.util.Log;

import java.util.List;

public class Stairs extends Element {
    private boolean wheelchair;

    public Stairs(){}

    public Stairs(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public Stairs(int id, boolean wheelchair) {
        super(id);
        this.wheelchair = wheelchair;
    }

    public Stairs(int id, int orientation, String type, boolean open, boolean wheelchair) {
        super(id, orientation, type, open);
        this.wheelchair = wheelchair;
    }

    public Stairs(int id, int orientation, String type, boolean open, List<Integer> connects, boolean wheelchair) {
        super(id, orientation, type, open, connects);
        this.wheelchair = wheelchair;
    }

    @Override
    public String toString() {
        String stairsString = super.toString();
        stairsString += "Wheelchair: " + this.wheelchair + "\n";
        Log.d("tostring", "lo iso");

        return stairsString;
    }

    public boolean isWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }
}
