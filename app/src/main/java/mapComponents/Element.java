package mapComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Door.class, name = "door"),
        @JsonSubTypes.Type(value = Stairs.class, name = "stairs"),
        @JsonSubTypes.Type(value = Elevator.class, name = "elevator"),
})
public class Element {
    private int id;
    private int orientation; // 0 North, 1 East, 2 South, 3 West
    private String type;    // door, elevator, stairs
    private boolean open;
    private List<Integer> connects; //id of Rooms it connects
    private int x, y; //position in the room

    public Element(){
        this.open = true;
        this.connects = new ArrayList<Integer>();
    }

    public Element(int id){
        this.id = id;
        this.type = "door";
        this.open = true;
        this.connects = new ArrayList<Integer>();
    }

    public Element(int id, int orientation, String type, boolean open){
        this.id = id;
        this.orientation = orientation;
        this.type = type;
        this.open = open;
        this.connects = new ArrayList<Integer>();
    }

    public Element(int id, int orientation, String type, boolean open, List<Integer> connects){
        this.id = id;
        this.orientation = orientation;
        this.type = type;
        this.open = open;
        this.connects = connects;
    }

    public String toString(){
        String ret;

        ret = type + ": " + String.valueOf(this.id) + "\n";
        ret += "Orientation: " + this.orientationString() + "\n";
        //ret += getConnectsString() + "\n";

        return ret;
    }

    public String orientationString() {
        String orientationString;

        switch (this.orientation){
            case 0: orientationString = "north"; break;
            case 1: orientationString = "east"; break;
            case 2: orientationString = "south"; break;
            case 3: orientationString = "west"; break;
            default: orientationString = null; break;
        }

        return orientationString;
    }

    private String getConnectsString() {
        String conString = "";

        for(int i = 0; i < this.connects.size(); ++i){
            conString += this.connects.get(i) + ", ";
        }

        return conString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<Integer> getConnects() {
        return connects;
    }

    public void setConnects(List<Integer> connects) {
        this.connects = connects;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
