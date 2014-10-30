package components;

import java.util.ArrayList;

/**
 * Created by eric on 10/30/14.
 */
public class Engine {

    private DenormalizedDataSet entityComponentDataSet;
    private DenormalizedDataSet entityTagDataSet;

    public static final int COMPONENT_COLLIDABLE = 0;
    public static final int COMPONENT_SELECTABLE = 1;
    public static final int COMPONENT_POSITIONABLE = 2;

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
                this.add(COMPONENT_COLLIDABLE);
            }};

            public ArrayList<Integer> getLabels() {
                return labels;
            }
        };

        entityComponentDataSet.addDataPoint(entity);
    }
}
