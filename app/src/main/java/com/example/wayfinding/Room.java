package com.example.wayfinding;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private int nElements;
    private List<Element> room;
    private boolean up, down;   //If user can go up and/or down trough this room


    Room(){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
    }

    Room(int id){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
        this.id = id;
        this.up = true;
        this.down = true;
    }

    Room(int id, boolean up, boolean down){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
        this.id = id;
        this.up = up;
        this.down = down;
    }

    Room(int id, int nElements, List<Element> room){
        this.room = new ArrayList<Element>();
        this.id = id;
        this.nElements = nElements;
        this.room = room;
    }

    Room(int id, int nElements, List<Element> room, boolean up, boolean down){
        this.room = new ArrayList<Element>();
        this.id = id;
        this.nElements = nElements;
        this.room = room;
        this.up = up;
        this.down = down;
    }

    public void addElement(String type,  int orientation, int capacity, boolean open, boolean wheelchair, boolean up, boolean down){
        switch (type) {
            case "door": room.add(new Door(nElements, orientation, type, open)); break;
            case "elevator": room.add(new Elevator(nElements, orientation, type, open, wheelchair, capacity)); break;
            case "stairs": room.add(new Stairs(nElements, orientation, type, open, wheelchair, up, down)); break;
            default: break;
        }

        nElements++;
    }

    public String toString(){
        String roomString = "Room: "+ String.valueOf(this.id) + " Empty\n";

        if(!room.isEmpty()) {
            roomString = "Room ID: " + String.valueOf(this.id) + "\n";
            roomString += "Element number: " + String.valueOf(this.nElements) + "\n";

            for (int i = 0; i < nElements; ++i) {
                roomString += room.get(i).toString();
            }
        }

        return roomString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getnDoors() {
        return nElements;
    }

    public void setnDoors(int nElements) {
        this.nElements = nElements;
    }


    public List<Element> getRoom() {
        return room;
    }

    public void setRoom(List<Element> room) {
        this.room = room;
    }
}
