package com.example.wayfinding;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private int nElements;
    private List<Element> room;
    private int width; //x
    private int height; //y


    Room(){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
    }

    Room(int id){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
        this.id = id;
    }

    Room(int id, boolean up, boolean down){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
        this.id = id;
    }

    Room(int id, int nElements, List<Element> room){
        this.room = new ArrayList<Element>();
        this.id = id;
        this.nElements = nElements;
        this.room = room;
    }

    public void addElement(String type,  int orientation, int capacity, boolean open, boolean wheelchair){
        switch (type) {
            case "door": room.add(new Door(nElements, orientation, type, open)); break;
            case "elevator": room.add(new Elevator(nElements, orientation, type, open, wheelchair, capacity)); break;
            case "stairs": room.add(new Stairs(nElements, orientation, type, open, wheelchair)); break;
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


    public int nElements() {
        return nElements;
    }

    public void nElements(int nElements) {
        this.nElements = nElements;
    }


    public List<Element> getRoom() {
        return room;
    }

    public void setRoom(List<Element> room) {
        this.room = room;
    }

    public Element get(int pos){
        return room.get(pos);
    }
}
