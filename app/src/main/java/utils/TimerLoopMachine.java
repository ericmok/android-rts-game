package utils;

import java.util.ArrayList;

/**
 * Created by eric on 3/19/15.
 * Acts as a simple FSM. Each state is a timer.
 * As we step the machine, we wind up in the next state
 * only if the time surpasses the current state timer.
 */
public class TimerLoopMachine {

    private ArrayList<Double> timers;
    private int currentState = -1;
    private double currentProgress = 0;

    private boolean hasTransitioned = false;

    public TimerLoopMachine(int capacity) {
        timers = new ArrayList<Double>(capacity);
    }

    public TimerLoopMachine addTimer(double timer) {
        timers.add(timer);

        if (timers.size() == 1) {
            currentState = 0;
        }

        return this;
    }

    public void clear() {
        timers.clear();
        currentState = -1;
    }

    public void step(double dt) {
        if (timers.size() < 1) {
            return;
        }

        currentProgress += dt;

        double currentTimer = timers.get(currentState);

        if (currentProgress >= currentTimer) {

            // This is risky, if every timer is lower than currentProgress
            // then infinite loop...
            // Makes this continuous as no time is loss
            // while (this.hasTransitioned()) {
            //  this.step(dt);
            // }
            // currentProgress = currentTimer - currentProgress;
            currentProgress = 0;

            currentState = (currentState + 1) % (timers.size());

            this.hasTransitioned = true;
        }
        else {
            this.hasTransitioned = false;
        }
    }

    public boolean hasTransitioned() {
        return this.hasTransitioned;
    }

    public int getCurrentState() {
        return currentState;
    }

    public TimerLoopMachine setCurrentState(int state) {
        this.currentState = state;
        return this;
    }

    public double getCurrentProgress() {
        return currentProgress;
    }

    public TimerLoopMachine setCurrentProgress(double progress) {
        this.currentProgress = progress;
        return this;
    }
}
