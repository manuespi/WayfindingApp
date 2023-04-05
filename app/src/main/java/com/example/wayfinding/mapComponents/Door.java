package com.example.wayfinding.mapComponents;

import java.io.Serializable;
import java.util.List;

public class Door extends Element implements Serializable {
    public Door() {}

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
