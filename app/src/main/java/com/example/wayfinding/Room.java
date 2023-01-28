package com.example.wayfinding;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int id;
    private int nDoors;
    List<Door> room;

    Room(){
        room = new ArrayList<Door>();
        nDoors = 0;
    }

    Room(int id){
        room = new ArrayList<Door>();
        nDoors = 0;
        this.id = id;
    }

    Room(int id, int nDoors){
        room = new ArrayList<Door>();
        this.id = id;
        this.nDoors = nDoors;
    }

    Room(int id, int nDoors, List<Door> room){
        this.room = new ArrayList<Door>(); //?¿
        this.id = id;
        this.nDoors = nDoors;
        this.room = room;
    }

    public void addDoor(int orientation){
        switch (orientation) {
            case 0: room.add(new Door(nDoors, 0)); break;
            case 1: room.add(new Door(nDoors, 1)); break;
            case 2: room.add(new Door(nDoors, 2)); break;
            case 3: room.add(new Door(nDoors, 3)); break;
            default:
        }

        nDoors++;
    }

    public String toString(){
        String roomString = "Sala: "+ String.valueOf(this.id) + " Vacía\n";

        if(!room.isEmpty()) {
            roomString = "Room ID: " + String.valueOf(this.id) + "\n";
            roomString += "Door number: " + String.valueOf(this.nDoors) + "\n";

            for (int i = 0; i < nDoors; ++i) {
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
        return nDoors;
    }

    public void setnDoors(int nDoors) {
        this.nDoors = nDoors;
    }


    public List<Door> getRoom() {
        return room;
    }

    public void setRoom(List<Door> room) {
        this.room = room;
    }
}
