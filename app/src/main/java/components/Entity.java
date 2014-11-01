package components;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Vector3;

/**
 * Created by eric on 10/30/14.
 */
public class Entity {

    public static final int LOGIC_FORMATION = 0;
    public static final int LOGIC_SEPARATION = 1;
    public static final int LOGIC_FIELD_MOVEMENT = 2;
    public static final int LOGIC_FORCE_INTEGRATOR = 3;
    public static final int LOGIC_BATTLE = 4;
    public static final int LOGIC_UNIT_DRAW = 5;
    public static final int LOGIC_ORIENTATION = 6;
    public static final int LOGIC_SELECTION = 7;

    public static final int LOGIC_CAMERA = 8;

    public static final int TAG_PLAYER_OWNED = 0;
    public static final int TAG_ALLIED_OWNED = 1;
    public static final int TAG_ENEMY_OWNED = 2;
    public static final int TAG_LEADER = 3;
    public static final int TAG_FOLLOWER = 4;

    public static final int TAG_TROOP_TYPE = 5;

    public Entity self = this;

    private static int NEXT_UNIQUE_ID = 1;
    private int id;

    private ArrayList<Integer> components = new ArrayList<Integer>(16);
    private ArrayList<Integer> tags = new ArrayList<Integer>(16);

    /**
     * A map from Component classes to the component the entity has of that class
     */
    public Hashtable<Class<? extends Component>, Component> cData = new Hashtable<Class<? extends Component>, Component>(64);

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
