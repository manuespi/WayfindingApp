package mapComponents;

import java.util.ArrayList;
import java.util.List;

public class IndoorMap {
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
        this.map.add(nRoom, new Room(nRoom));
        nRoom++;
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
