package com.example.wayfinding;

public class Stairs extends Element{
    private boolean wheelchair;

    Stairs(){
        super();
        this.wheelchair = true;
    }

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

    public Stairs(int id, int orientation, String type, boolean open, Integer[] connects, boolean wheelchair) {
        super(id, orientation, type, open, connects);
        this.wheelchair = wheelchair;
    }

    @Override
    public String toString() {
        String stairsString = super.toString();
        stairsString += "Wheelchair: " + this.wheelchair + "\n";

        return stairsString;
    }

    public boolean Wheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }
}
