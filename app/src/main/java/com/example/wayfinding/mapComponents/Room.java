package com.example.wayfinding.mapComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private int id;
    private String name;
    private int nElements;
    private List<Element> room;
    private int width; //x
    private int height; //y


    public Room(){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
    }

    public Room(int id){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
        this.id = id;
    }

    public Room(int id, String name){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
        this.id = id;
        this.name = name;
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

    public Element getElement(int pos){
        return this.room.get(pos);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getnElements() {
        return nElements;
    }

    public void setnElements(int nElements) {
        this.nElements = nElements;
    }

    public List<Element> getRoom() {
        return room;
    }

    public void setRoom(List<Element> room) {
        this.room = room;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
