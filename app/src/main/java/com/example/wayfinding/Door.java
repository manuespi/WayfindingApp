package com.example.wayfinding;

public class Door {
    private int id;
    private int orientation; // 0 Norte, 1 Este, 2 Sur, 3 Oeste

    Door(){

    }

    Door(int id){
        this.id = id;
    }

    Door(int id, int orientation){
        this.id = id;
        this.orientation = orientation;
    }

    public String toString(){
        String doorString;

        doorString = "Puerta : " + String.valueOf(this.id) + "\n";
        doorString += "Orientación: " + this.getOrientationString() + "\n";


        return doorString;
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

    public String getOrientationString() {
        String orientationString;

        switch (this.orientation){
            case 0: orientationString = "Norte"; break;
            case 1: orientationString = "Este"; break;
            case 2: orientationString = "Sur"; break;
            case 3: orientationString = "Oeste"; break;
            default: orientationString = null; break;
        }

        return orientationString;
    }


    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
