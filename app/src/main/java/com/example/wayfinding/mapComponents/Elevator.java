package com.example.wayfinding.mapComponents;

import java.io.Serializable;
import java.util.List;

public class Elevator extends Element implements Serializable {
    private boolean wheelchair;
    private int capacity;

    public Elevator(){}

    public Elevator(boolean wheelchair, int capacity) {
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    public Elevator(int id, boolean wheelchair, int capacity) {
        super(id);
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    public Elevator(int id, int orientation, String type, boolean open, boolean wheelchair, int capacity) {
        super(id, orientation, type, open);
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    public Elevator(int id, int orientation, String type, boolean open, List<Integer> connects, boolean wheelchair, int capacity) {
        super(id, orientation, type, open, connects);
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        String elevatorString = super.toString();
        elevatorString += "Wheelchair: " + this.wheelchair + "\n";
        elevatorString += "Capacity: " + this.capacity + "\n";

        return elevatorString;
    }

    public boolean isWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}