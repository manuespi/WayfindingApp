package com.example.wayfinding.mapComponents;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.wayfinding.BR;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room extends BaseObservable implements Serializable {
    private int id;
    private String name;
    private int nElements;
    private List<Element> room;
    private String width; //x
    private String length; //y


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

    public void addElement(String type, int orientation, int capacity, boolean open, boolean wheelchair){
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
            roomString = "Room name: " + this.name + "\n";
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

    @Bindable
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
        //notifyPropertyChanged(BR.width);
    }

    @Bindable
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
        //notifyPropertyChanged(BR.length);
    }
}
