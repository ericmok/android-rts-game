package utils;

import java.util.ArrayList;
import java.util.HashMap;

import noteworthyframework.QueueMutationList;

/**
 * Created by eric on 3/17/15.
 */
public class QueueMutationHashedList<K, V> {
    public HashMap<K, QueueMutationList<V>> HashMap;
    public ArrayList<K> keys;

    private int keyCapacity;
    private int valueCapacity;

    public QueueMutationHashedList(int keyCapacity, int valueCapacity) {
        this.keyCapacity = keyCapacity;
        this.valueCapacity = valueCapacity;

        HashMap = new HashMap<K, QueueMutationList<V>>(keyCapacity);
        keys = new ArrayList<K>(keyCapacity);
    }

    public void queueToAdd(K key, V value) {
        QueueMutationList<V> list = HashMap.get(key);
        if (list == null) {
            list = new QueueMutationList<V>(valueCapacity);
            HashMap.put(key, list);
            keys.add(key);
        }

        list.queueToAdd(value);
    }

    public void queueToRemove(K key, V value) {
        HashMap.get(key).queueToRemove(value);
    }

    public QueueMutationList<V> getListByKeyIndex(int index) {
        return HashMap.get(keys.get(index));
    }

    public QueueMutationList<V> getListFor(K key) {
        return HashMap.get(key);
    }

    public void flushQueues() {
        for (int i = keys.size() - 1; i >= 0; i--) {
            HashMap.get(keys.get(i)).flushQueues();
        }
    }

    public int numberKeys() {
        return keys.size();
    }
}
