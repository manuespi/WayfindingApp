package mapComponents;

import java.util.ArrayList;
import java.util.List;

public class IndoorMap {
    private ArrayList<Room> map;
    private String name;

    public IndoorMap(){
        map = new ArrayList<Room>();
    }

    public void addElementToRoom(int id, String type,  int orientation, int capacity, boolean open, boolean wheelchair){
        this.map.get(id).addElement(type, orientation, capacity, open, wheelchair);
    }

    public void addRoom(){
        this.map.add(this.map.size(), new Room(this.map.size() - 1));
    }

    public IndoorMap(String name){
        this.name = name;
    }

    public ArrayList<Room> getMap() {
        return map;
    }

    public void setMap(ArrayList<Room> map) {
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
