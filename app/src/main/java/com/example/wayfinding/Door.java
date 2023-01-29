package com.example.wayfinding;

public class Door extends Element{
    public Door() {
        super();
    }

    public Door(int id) {
        super(id);
    }

    public Door(int id, int orientation, String type, boolean open) {
        super(id, orientation, type, open);
    }

    public Door(int id, int orientation, String type, boolean open, Integer[] connects) {
        super(id, orientation, type, open, connects);
    }
}
