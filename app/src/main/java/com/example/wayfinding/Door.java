package com.example.wayfinding;

import java.util.List;

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

    public Door(int id, int orientation, String type, boolean open, List<Integer> connects) {
        super(id, orientation, type, open, connects);
    }
}
