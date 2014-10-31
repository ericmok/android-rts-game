package components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by eric on 10/30/14.
 *
 * <p>
 * Where data is duplicated in various lists but needs to be managed so that
 * adding and removal of the data is consistent across all the lists.
 * Denormalizing data allows for faster data access with the expense of using more memory.
 * </p>
 *
 * <p>
 * For example, a game engine with 10000 game objects might only have 1000 collidable objects.
 * Collision detection is an expensive operation.
 * However, a subset of game objects may be both collidable and selectable, with some collidable
 * objects not being selectable.
 * </p>
 *
 * <p>
 * By denormalizing data, collision detection process needs only operate on a limited subset
 * and the selection process may be limited to another overlapping subset, thus increasing
 * processing efficiency.
 * </p>
 *
 * <code>
 *     DenormalizedDataSet world = new DenormalizedDataSet();
 *     world.add( someObjectLabeled_1_ ); x 1000
 *     world.add( someObjectLabeled_2_ ); x 1000
 *     world.add( someObjectLabeled_1_And_2_ ); x 1000
 *
 *     // Internally
 *     world has two lists,
 *     world.getListLabeled(1) <= has 2000 objects
 *     world.getListLabeled(2) <= has 2000 objects
 *
 *     world.removeObject( someObjectLabeled_1_And_2 )
 *     world.getListLabeled(1) <= has 1999 objects
 *     world.getListLabeled(2) <= has 1999 objects
 * </code>
 *
 * <p>
 * Data is duplicated in accordance with labels.
 * This programming pattern can be found in Entity Component System frameworks.
 * Entities are duplicated across 'processing lists' given the processing components they contain.
 * </p>
 */
public class DenormalizedDataSet {

    /**
     * <p>
     *     Hash that maps a label to a list.
     * </p>
     *
     * <p>
     *     Example: We have a list for n-body simulation and a list for selectable units.
     *     The elements in one list may be repeated in another because we may have bodies that
     *     are selectable.
     * </p>
     */
    public Hashtable<Integer, ArrayList<DataPoint>> lists;

    private int upperBoundNumberDataPointsPerLabel;

    /**
     * @param upperBoundNumberLabels Keep this small!
     * @param upperBoundNumberDataPointsPerLabel
     */
    public DenormalizedDataSet(int upperBoundNumberLabels, int upperBoundNumberDataPointsPerLabel) {
        this.upperBoundNumberDataPointsPerLabel = upperBoundNumberDataPointsPerLabel;

        lists = new Hashtable<Integer, ArrayList<DataPoint>>(upperBoundNumberLabels);

//        Iterator<Map.Entry<Integer, ArrayList<DataPoint>>> itr = lists.entrySet().iterator();
//        while (itr.hasNext()) {
//            lists.put( itr.next().getKey(), new ArrayList<DataPoint>(upperBoundNumberDataPointsPerLabel));
//        }
    }

    /**
     * Adds the dataPoint to all the lists for which the dataPoint contains the label for.
     * @param dataPoint
     */
    public synchronized void addDataPoint(DataPoint dataPoint) {
        ArrayList<Integer> labels = dataPoint.getLabels();

        for (int i = 0; i < labels.size(); i++) {
            int label = labels.get(i);

            if (!lists.containsKey(label)) {
                lists.put(label, new ArrayList<DataPoint>(upperBoundNumberDataPointsPerLabel));
            }

            lists.get(label).add(dataPoint);
        }
    }

    /**
     * Removes the dataPoint from all lists for which it is labeled for
     * @param dataPoint
     */
    public synchronized void removeDataPoint(DataPoint dataPoint) {
        ArrayList<Integer> labels = dataPoint.getLabels();

        for (int i = 0; i < labels.size(); i++) {

            int label = labels.get(i);

            if (lists.contains(label)) {
                lists.get(label).remove(dataPoint);
            }
        }
    }

    public static interface DataPoint {
        public Object getContainer();
        public ArrayList<Integer> getLabels();
    }
}
