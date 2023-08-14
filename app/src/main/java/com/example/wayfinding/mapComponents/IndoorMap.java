package com.example.wayfinding.mapComponents;

import java.io.Serializable;
import java.util.ArrayList;

public class IndoorMap implements Serializable {
    private ArrayList<Room> map;
    private int nRoom;
    private String name;

    public IndoorMap(){
        map = new ArrayList<Room>();
        nRoom = 0;
    }

    public IndoorMap(String name){
        this.name = name;
        map = new ArrayList<Room>();
        nRoom = 0;
    }

    public void addElementToRoom(int id, String type,  int orientation, int capacity, boolean open, boolean wheelchair){
        this.map.get(id).addElement(type, orientation, capacity, open, wheelchair);
    }

    public void addRoom(){
        this.map.add(nRoom, new Room(nRoom, Integer.toString(nRoom))); //TODO meterle el nombre cuando proceda
        nRoom++;
    }

    public void addRoom(Room r){
        this.map.add(nRoom, r);
        nRoom++;
    }

    public int NextId(){
        int ret = -1;

        for(int i = 0; i < this.map.size(); ++i)
            if(map.get(i).getId() > ret) ret = map.get(i).getId();

        if(ret == -1) return 0;
        else return ret + 1;
    }

    public Room getRoom(int pos){
        return this.map.get(pos);
    }

    public ArrayList<Room> getMap() {
        return map;
    }

    public void setMap(ArrayList<Room> map) {
        this.map = map;
    }

    public int getnRoom() {
        return nRoom;
    }

    public void setnRoom(int nRoom) {
        this.nRoom = nRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
