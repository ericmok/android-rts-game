package structure.utils;

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

        this.assertTrue(timerLoopMachine.getCurrentState() == 0);

        timerLoopMachine.step(0);
        this.assertTrue(timerLoopMachine.getCurrentState() == 1);

        timerLoopMachine.step(2);
        this.assertTrue(timerLoopMachine.getCurrentState() == 1);
        this.assertTrue(timerLoopMachine.getCurrentProgress() == 2);
        this.assertTrue(!timerLoopMachine.hasTransitioned());

        timerLoopMachine.step(4);
        this.assertTrue(timerLoopMachine.getCurrentState() == 1);
        this.assertTrue(timerLoopMachine.getCurrentProgress() == 6);
        this.assertTrue(!timerLoopMachine.hasTransitioned());

        timerLoopMachine.step(10);
        this.assertTrue(timerLoopMachine.getCurrentState() == 2);
        this.assertTrue(timerLoopMachine.getCurrentProgress() == 0);
        this.assertTrue(timerLoopMachine.hasTransitioned());

        timerLoopMachine.step(10);
        this.assertTrue(timerLoopMachine.getCurrentState() == 2);
        this.assertTrue(timerLoopMachine.getCurrentProgress() == 10);

        timerLoopMachine.step(40);
        this.assertTrue(timerLoopMachine.getCurrentState() == 0);
        this.assertTrue(timerLoopMachine.getCurrentProgress() == 0);
    }
}
