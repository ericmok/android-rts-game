package components;

import java.util.Hashtable;

import game.androidgame2.Vector3;

/**
 * Created by eric on 10/30/14.
 */
public class Entity {

    private static int NEXT_UNIQUE_ID = 1;
    private int id;

    public Hashtable<Class<? extends Component>, Component> data = new Hashtable<Class<? extends Component>, Component>(64);

    public Entity() {
        id = NEXT_UNIQUE_ID;
        NEXT_UNIQUE_ID += 1;
    }
}
