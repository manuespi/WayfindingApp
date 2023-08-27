package mapComponents;

import java.util.List;

public class Door extends Element{
    public Door() {
        super();
    }

    public Door(int id) {
        super(id);
    }

    public Door(int id, int orientation, String type, boolean open, int x, int y) {
        super(id, orientation, type, open, x, y);
    }

    public Door(int id, int orientation, String type, boolean open, List<Integer> connects) {
        super(id, orientation, type, open, connects);
    }
}
