package components;

import java.util.ArrayList;

import tenth.system.SystemNode;

/**
 * Created by eric on 10/30/14.
 */
public class Engine {

    private DenormalizedDataSet entityLogicDataSet;
    private DenormalizedDataSet entityTagDataSet;

    public static final int LOGIC_FORMATION = 0;
    public static final int LOGIC_SEPARATION = 1;
    public static final int LOGIC_FIELD_MOVEMENT = 2;
    public static final int LOGIC_FORCE_INTEGRATOR = 3;
    public static final int LOGIC_BATTLE = 4;
    public static final int LOGIC_TROOP_DRAW = 5;
    public static final int LOGIC_ORIENTATION = 6;
    public static final int LOGIC_SELECTION = 7;

    public static final int TAG_PLAYER_OWNED = 0;
    public static final int TAG_ALLIED_OWNED = 1;
    public static final int TAG_ENEMY_OWNED = 2;
    public static final int TAG_LEADER = 3;
    public static final int TAG_FOLLOWER = 4;

    public Engine() {
        entityLogicDataSet = new DenormalizedDataSet(16, 300);
        entityTagDataSet = new DenormalizedDataSet(16, 300);
    }

    public void addEntity(Entity entity) {

    }
}
