package mapComponents;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.example.wayfinding.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room extends BaseObservable implements Serializable {

    private int id;
    private int nElements;
    private List<Element> room;
    private String width; //x
    private String length; //y
    private String name;
    private List<Marker> markers;


    public Room(){
        this.room = new ArrayList<Element>();
        this.markers = new ArrayList<Marker>();
        this.nElements = 0;
    }

    public Room(int id){
        this.room = new ArrayList<Element>();
        this.markers = new ArrayList<Marker>();
        this.nElements = 0;
        this.id = id;
    }

    public Room(int id, String name){
        this.room = new ArrayList<Element>();
        this.markers = new ArrayList<Marker>();
        this.nElements = 0;
        this.id = id;
        this.name = name;
    }

    Room(int id, boolean up, boolean down){
        this.room = new ArrayList<Element>();
        this.markers = new ArrayList<Marker>();
        this.nElements = 0;
        this.id = id;
    }

    Room(int id, int nElements, List<Element> room){
        this.room = new ArrayList<Element>();
        this.markers = new ArrayList<Marker>();
        this.id = id;
        this.nElements = nElements;
        this.room = room;
    }

    @Bindable
    public String getName() { return name; }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getWidth() {
        return width;
    }

    @Bindable
    public void setWidth(String width) {
        this.width = width;
        notifyPropertyChanged(BR.width);
    }

    @Bindable
    public String getLength() {
        return length;
    }

    @Bindable
    public void setLength(String length) {
        this.length = length;
        notifyPropertyChanged(BR.length);
    }


    public Element addElement(String type,  int orientation, int capacity, boolean open, boolean wheelchair, int x, int y, ArrayList<Integer> conn){
        Element ret = new Element();
        switch (type) {
            case "door": ret = new Door(nElements, orientation, type, open, x, y, conn); room.add(ret); break;
            case "elevator": ret = new Elevator(nElements, orientation, type, open, wheelchair, capacity, x, y, conn); room.add(ret); break;
            case "stairs": ret = new Stairs(nElements, orientation, type, open, wheelchair, x, y, conn); room.add(ret); break;
            default: break;
        }

        nElements++;
        return ret;
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

    public boolean removeById(int id){
        int i = 0;
        boolean found = false;
        while(i < this.nElements && !found){
            if(room.get(i).getId() == id) found = true;
            ++i;
        }

        if(found){ room.remove(i-1); this.nElements--;}

        return found;
    }

    public class Marker implements Serializable {
        private float x, y;
        private String type;

        public Marker(float x, float y, String elementType) {
            this.x = x;
            this.y = y;
            this.type = elementType;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public String getElementType() {
            return type;
        }
    }

    public void addMarker(float x, float y, String elementType) {
        Marker marker = new Marker(x, y, elementType);
        markers.add(marker);
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public Element getElement(int pos){
        return this.room.get(pos);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Element get(int pos){
        return room.get(pos);
    }
}
