package utils;

import java.util.Hashtable;

/**
 * Created by eric on 11/2/14.
 */
public class FiniteStateMachine<K> {

    public static int PRE_ALLOCATIONS = 32;

    public Hashtable<K, Runnable> states = new Hashtable<K, Runnable>(PRE_ALLOCATIONS);
    public Hashtable<K, K> transitions = new Hashtable<K, K>(PRE_ALLOCATIONS);

    public K currentState = null;

    public FiniteStateMachine() {
    }

    public void addState(K state, Runnable runnable) {
        if (states.isEmpty()) {
            currentState = state;
        }
        states.put(state, runnable);
    }

    public void removeState(K state) {
        if (currentState == state) {
            currentState = null;
        }
        states.remove(state);
    }

    public void transition(K event) {
        K newState = transitions.get(event);
        if (newState != null) {
            currentState = newState;
            states.get(currentState).run();
        }
    }

}
