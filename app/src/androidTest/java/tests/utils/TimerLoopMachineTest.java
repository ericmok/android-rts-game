package tests.utils;

import android.app.Application;
import android.test.ApplicationTestCase;

import utils.TimerLoopMachine;

/**
 * Created by eric on 3/19/15.
 */
public class TimerLoopMachineTest extends ApplicationTestCase<Application> {

    public TimerLoopMachineTest() {
        super(Application.class);
    }

    public void testOne() {
        TimerLoopMachine timerLoopMachine = new TimerLoopMachine(3);
        timerLoopMachine.addTimer(0).addTimer(10).addTimer(20);

        assertTrue(timerLoopMachine.getCurrentState() == 0);

        timerLoopMachine.step(0);
        assertTrue(timerLoopMachine.getCurrentState() == 1);

        timerLoopMachine.step(2);
        assertTrue(timerLoopMachine.getCurrentState() == 1);
        assertTrue(timerLoopMachine.getCurrentProgress() == 2);
        assertTrue(!timerLoopMachine.hasTransitioned());

        timerLoopMachine.step(4);
        assertTrue(timerLoopMachine.getCurrentState() == 1);
        assertTrue(timerLoopMachine.getCurrentProgress() == 6);
        assertTrue(!timerLoopMachine.hasTransitioned());

        timerLoopMachine.step(10);
        assertTrue(timerLoopMachine.getCurrentState() == 2);
        assertTrue(timerLoopMachine.getCurrentProgress() == 0);
        assertTrue(timerLoopMachine.hasTransitioned());

        timerLoopMachine.step(10);
        assertTrue(timerLoopMachine.getCurrentState() == 2);
        assertTrue(timerLoopMachine.getCurrentProgress() == 10);

        timerLoopMachine.step(40);
        assertTrue(timerLoopMachine.getCurrentState() == 0);
        assertTrue(timerLoopMachine.getCurrentProgress() == 0);
    }
}
