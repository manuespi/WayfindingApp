package com.example.wayfinding;

public class Stairs extends Element{
    private boolean wheelchair, up, down;

    Stairs(){
        super();
        this.wheelchair = true;
        this.up = true;
        this.down = true;
    }

    public Stairs(boolean wheelchair, boolean up, boolean down) {
        this.wheelchair = wheelchair;
        this.up = up;
        this.down = down;
    }

    public Stairs(int id, boolean wheelchair, boolean up, boolean down) {
        super(id);
        this.wheelchair = wheelchair;
        this.up = up;
        this.down = down;
    }

    public Stairs(int id, int orientation, String type, boolean open, boolean wheelchair, boolean up, boolean down) {
        super(id, orientation, type, open);
        this.wheelchair = wheelchair;
        this.up = up;
        this.down = down;
    }

    public Stairs(int id, int orientation, String type, boolean open, Integer[] connects, boolean wheelchair, boolean up, boolean down) {
        super(id, orientation, type, open, connects);
        this.wheelchair = wheelchair;
        this.up = up;
        this.down = down;
    }

    @Override
    public String toString() {
        String stairsString = super.toString();
        stairsString += "Wheelchair: " + this.wheelchair + "\n";
        stairsString += "Up: " + this.up + " Down: " + this.down + "\n";

        return stairsString;
    }

    public boolean Wheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public boolean Up() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean Down() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
