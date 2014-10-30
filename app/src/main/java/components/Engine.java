package components;

import java.util.ArrayList;

import tenth.system.SystemNode;

/**
 * Created by eric on 10/30/14.
 */
public class Engine {

    private DenormalizedDataSet entityComponentDataSet;
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
        entityComponentDataSet = new DenormalizedDataSet(16, 300);
        entityTagDataSet = new DenormalizedDataSet(16, 300);

        DenormalizedDataSet.DataPoint entity = new DenormalizedDataSet.DataPoint() {

            ArrayList<Integer> labels = new ArrayList<Integer>(16) {{
                this.add(LOGIC_FIELD_MOVEMENT);
            }};

            public ArrayList<Integer> getLabels() {
                return labels;
            }
        };

        entityComponentDataSet.addDataPoint(entity);
    }
}
