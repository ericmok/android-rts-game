package utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import noteworthyframework.QueueMutationList;

/**
 * Created by eric on 3/17/15.
 */
public class QueueMutationHashedList<K, V> {
    public Hashtable<K, QueueMutationList<V>> hashtable;
    public ArrayList<K> keys;

    private int keyCapacity;
    private int valueCapacity;

    public QueueMutationHashedList(int keyCapacity, int valueCapacity) {
        this.keyCapacity = keyCapacity;
        this.valueCapacity = valueCapacity;

        hashtable = new Hashtable<K, QueueMutationList<V>>(keyCapacity);
        keys = new ArrayList<K>(keyCapacity);
    }

    public void queueToAdd(K key, V value) {
        QueueMutationList<V> list = hashtable.get(key);
        if (list == null) {
            list = new QueueMutationList<V>(valueCapacity);
            hashtable.put(key, list);
            keys.add(key);
        }

        list.queueToAdd(value);
    }

    public void queueToRemove(K key, V value) {
        hashtable.get(key).queueToRemove(value);
    }

    public QueueMutationList<V> getListByKeyIndex(int index) {
        return hashtable.get(keys.get(index));
    }

    public QueueMutationList<V> getListFor(K key) {
        return hashtable.get(key);
    }

    public void flushQueues() {
        for (int i = keys.size() - 1; i >= 0; i--) {
            hashtable.get(keys.get(i)).flushQueues();
        }
    }

    public int numberKeys() {
        return keys.size();
    }
}
