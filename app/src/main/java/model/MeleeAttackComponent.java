package model;

import java.util.ArrayList;

import processors.Functional;
import utils.VoidFunc;

/**
 * Created by eric on 11/11/14.
 */
public class MeleeAttackComponent extends Component {

    public static enum Event {
        READY,
        ATTACKING_TARGET,
        COOLDOWN,
    }

    public Event event = Event.READY;

    public double targetAcquisitionRange = 2;

    public ArrayList<Entity> targetsInRange = new ArrayList<Entity>(8);

    public int attackStrength = 1;

    public double attackSwingTime = 2;

    public double attackSwingProgress = 0;

    public double attackCooldown = 2;

    public VoidFunc<Entity> script = Functional.FN_NOOP;
}
