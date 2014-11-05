package components;

import java.util.ArrayList;
import java.util.Hashtable;

import game.androidgame2.Vector3;

/**
 * Created by eric on 10/30/14.
 */
public class Entity implements Denormalizable<Integer> {

    public static final int LOGIC_FORMATION = 0;
    public static final int LOGIC_SEPARATION = 1;
    public static final int LOGIC_FIELD_MOVEMENT = 2;
    public static final int LOGIC_FORCE_INTEGRATOR = 3;
    public static final int LOGIC_BATTLE = 4;
    public static final int LOGIC_UNIT_DRAW = 5;
    public static final int LOGIC_ORIENTATION = 6;
    public static final int LOGIC_SELECTION = 7;
    public static final int LOGIC_DESTINATION_MOVEMENT = 8;

    public static final int LOGIC_ABILITIES = 9;

    public static final int TAG_PLAYER_OWNED = 100;
    public static final int TAG_ALLIED_OWNED = 101;
    public static final int TAG_ENEMY_OWNED = 102;
    public static final int TAG_LEADER = 103;
    public static final int TAG_FOLLOWER = 104;

    public static final int TAG_TROOP_TYPE = 105;

    public static final int UI_BUTTON = 500;

    public Entity self = this;

    private static int NEXT_UNIQUE_ID = 1;
    private int id;

    private ArrayList<Integer> labels = new ArrayList<Integer>(16);

    /**
     * A map from Component classes to the component the entity has of that class
     */
    public Hashtable<Class<? extends Component>, Component> cData = new Hashtable<Class<? extends Component>, Component>(64);

    public Entity() {
        id = NEXT_UNIQUE_ID;
        NEXT_UNIQUE_ID += 1;
    }

    public ArrayList<Integer> getLabels() {
        return labels;
    }
}
