package com.example.wayfinding;

public class Element {
    private int id;
    private int orientation; // 0 North, 1 East, 2 South, 3 West
    private String type;    // door, elevator, stairs
    private boolean open;
    private Integer[] connects; //id of Rooms it connects

    Element(){
        this.type = "door";
        this.open = true;
        this.connects = new Integer[]{null, null};
    }

    Element(int id){
        this.id = id;
        this.type = "door";
        this.open = true;
        this.connects = new Integer[]{null, null};
    }

    Element(int id, int orientation, String type, boolean open){
        this.id = id;
        this.orientation = orientation;
        this.type = type;
        this.open = open;
        this.connects = new Integer[]{null, null};
    }

    Element(int id, int orientation, String type, boolean open, Integer[] connects){
        this.id = id;
        this.orientation = orientation;
        this.type = type;
        this.open = open;
        this.connects = connects;
    }

    public String toString(){
        String ret;

        ret = type + ": " + String.valueOf(this.id) + "\n";
        ret += "Orientation: " + this.getOrientationString() + "\n";
        ret += "Connects: " + this.connects[0] + " and " + this.connects[1] + "\n";


        return ret;
    }

    public String getOrientationString() {
        String orientationString;

        switch (this.orientation){
            case 0: orientationString = "north"; break;
            case 1: orientationString = "east"; break;
            case 2: orientationString = "south"; break;
            case 3: orientationString = "west"; break;
            default: orientationString = null; break;
        }

        return orientationString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Integer[] getConnects() {
        return connects;
    }

    public void setConnects(Integer[] connects) {
        this.connects = connects;
    }
}
