package mapComponents;

import android.text.Editable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.wayfinding.BR;

import java.util.ArrayList;
import java.util.List;

public class Room extends BaseObservable {

    private int id;
    private int nElements;
    private List<Element> room;
    private String width; //x
    private String length; //y
    private String roomName;


    @Bindable
    public String getName() { return roomName;    }

    @Bindable
    public void setName(String name) {
        this.roomName = name;
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




    public Room(){
        this.room = new ArrayList<Element>();
        this.nElements = 0;
    }

    public Room(int id){
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

//    public void setParameters(int x, int y){
//        this.length= y;
//        this.width = x;
//    }
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
        String roomString = "Room: "+ this.id + " Empty\n";

        if(!room.isEmpty()) {
            roomString = "Room ID: " + this.id + "\n";
            roomString += "Element number: " + this.nElements + "\n";

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
