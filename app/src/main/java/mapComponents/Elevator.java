package mapComponents;

import java.util.ArrayList;
import java.util.List;

public class Elevator extends Element{
    private boolean wheelchair;
    private int capacity;

    Elevator(){
        super();
        this.wheelchair = true;
        this.capacity = 6;
    }

    public Elevator(boolean wheelchair, int capacity) {
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    public Elevator(int id, boolean wheelchair, int capacity) {
        super(id);
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    public Elevator(int id, int orientation, String type, boolean open, boolean wheelchair, int capacity, int x, int y, ArrayList<Integer> conn) {
        super(id, orientation, type, open, x, y, conn);
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    public Elevator(int id, int orientation, String type, boolean open, List<Integer> connects, boolean wheelchair, int capacity) {
        super(id, orientation, type, open, connects);
        this.wheelchair = wheelchair;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        String elevatorString = super.toString();
        elevatorString += "Wheelchair: " + this.wheelchair + "\n";
        elevatorString += "Capacity: " + this.capacity + "\n";

        return elevatorString;
    }

    public boolean Wheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
