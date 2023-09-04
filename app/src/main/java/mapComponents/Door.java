package mapComponents;

import java.util.ArrayList;
import java.util.List;

public class Door extends Element{
    public Door() {
        super();
    }

    public Door(int id) {
        super(id);
    }

    public Door(int id, int orientation, String type, boolean open, int x, int y, ArrayList<Integer> conn) {
        super(id, orientation, type, open, x, y, conn);
    }

    public Door(int id, int orientation, String type, boolean open, List<Integer> connects) {
        super(id, orientation, type, open, connects);
    }
}
