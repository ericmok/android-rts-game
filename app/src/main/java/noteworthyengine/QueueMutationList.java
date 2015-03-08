package noteworthyengine;

import java.util.ArrayList;

/**
 * Created by eric on 3/8/15.
 */
public class QueueMutationList<F> {

    public ArrayList<F> items;
    public ArrayList<F> itemsToAdd;
    public ArrayList<F> itemsToRemove;

    public QueueMutationList(int capacity) {
        items = new ArrayList<F>(capacity);
        itemsToAdd = new ArrayList<F>(capacity);
        itemsToRemove = new ArrayList<F>(capacity);
    }

    public void queueToAdd(F item) {
        itemsToAdd.add(item);
    }

    public void queueToRemove(F item) {
        itemsToRemove.add(item);
    }

    /**
     * Adds and removes items
     */
    public void flushQueues() {
        for (int i = 0; i < itemsToAdd.size(); i++) {
            items.add(itemsToAdd.get(i));
        }
        itemsToAdd.clear();

        for (int i = 0; i < itemsToRemove.size(); i++) {
            items.remove(itemsToRemove.get(i));
        }
        itemsToRemove.clear();
    }

    public int size() {
        return items.size();
    }

    public F get(int i) {
        return items.get(i);
    }
}
