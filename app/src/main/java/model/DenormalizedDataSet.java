package model;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by eric on 10/30/14.
 *
 * <p>
 * Where data is duplicated in various denormalizedLists but needs to be managed so that
 * adding and removal of the data is consistent across all the denormalizedLists.
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
 *     world has two denormalizedLists,
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
 * Entities are duplicated across 'processing denormalizedLists' given the processing components they contain.
 * </p>
 *
 * @param <E> The type of the denormalizable that has the labels
 * @param <F> The type of the labels the denormalizable is tagged with. Make sure equality checks are efficient
 */
public class DenormalizedDataSet<E extends Denormalizable, F> implements Denormalizable<Integer> {

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
    public Hashtable<F, ArrayList<E>> denormalizedLists;

    private ArrayList<Integer> labels = new ArrayList<Integer>();

    private int upperBoundNumberDenormalizablesPerLabel;

    /**
     * @param upperBoundNumberLabels Keep this small!
     * @param upperBoundNumberDenormalizablesPerLabel
     */
    public DenormalizedDataSet(int upperBoundNumberLabels, int upperBoundNumberDenormalizablesPerLabel) {
        this.upperBoundNumberDenormalizablesPerLabel = upperBoundNumberDenormalizablesPerLabel;

        denormalizedLists = new Hashtable<F, ArrayList<E>>(upperBoundNumberLabels);

//        Iterator<Map.Entry<Integer, ArrayList<Denormalizable>>> itr = denormalizedLists.entrySet().iterator();
//        while (itr.hasNext()) {
//            denormalizedLists.put( itr.next().getKey(), new ArrayList<Denormalizable>(upperBoundNumberDenormalizablesPerLabel));
//        }
    }

    /**
     * Adds the denormalizable to all the denormalizedLists for which the denormalizable contains the label for.
     * @param denormalizable
     */
    public synchronized void addDenormalizable(E denormalizable) {
        ArrayList<F> labels = denormalizable.labels();

        for (int i = 0; i < labels.size(); i++) {
            F label = labels.get(i);

            if (!denormalizedLists.containsKey(label)) {
                denormalizedLists.put(label, new ArrayList<E>(upperBoundNumberDenormalizablesPerLabel));
            }

            denormalizedLists.get(label).add(denormalizable);
        }
    }

    /**
     * Removes the denormalizable from all denormalizedLists for which it is labeled for
     * @param denormalizable
     */
    public synchronized boolean removeDenormalizable(E denormalizable) {
        ArrayList<F> labels = denormalizable.labels();

        boolean anyRemoved = false;

        for (int i = 0; i < labels.size(); i++) {

            F label = labels.get(i);

            // Get the list responsible for the label and remove
            if (denormalizedLists.get(label).remove(denormalizable)) {
                anyRemoved = true;
            }
        }

        return anyRemoved;
    }

    public ArrayList<E> getListForLabel(F label) {
        ArrayList<E> ret = denormalizedLists.get(label);
        if (ret == null) {
            denormalizedLists.put(label, new ArrayList<E>(upperBoundNumberDenormalizablesPerLabel));
            ret = denormalizedLists.get(label);
        }

        return ret;

    }

    public ArrayList<Integer> labels() {
        return labels;
    }
}
