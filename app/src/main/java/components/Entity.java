package components;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Vector3;

/**
 * Created by eric on 10/30/14.
 */
public class Entity {

    public Entity self = this;

    private static int NEXT_UNIQUE_ID = 1;
    private int id;

    private ArrayList<Integer> components = new ArrayList<Integer>(16);
    private ArrayList<Integer> tags = new ArrayList<Integer>(16);

    public Hashtable<Class<? extends Component>, Component> data = new Hashtable<Class<? extends Component>, Component>(64);

    private Denormalizable componentLabeler = new Denormalizable() {
        public Object getContainer() { return self; }
        public ArrayList<Integer> getLabels() { return components; }
    };

    private Denormalizable tagLabeler = new Denormalizable() {
        public Object getContainer() { return self; }
        public ArrayList<Integer> getLabels() { return tags; }
    };

    public Entity() {
        id = NEXT_UNIQUE_ID;
        NEXT_UNIQUE_ID += 1;
    }

    public Denormalizable getComponentLabeler() {
        return componentLabeler;
    }

    public Denormalizable getTagLabeler() {
        return tagLabeler;
    }
}
