package behaviors;

import java.util.ArrayList;

import model.Entity;
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

    public Entity targetLock = null;

    public int attackStrength = 1;

    public double attackSwingTime = 0.4;

    public double attackSwingProgress = 0;

    public double attackCooldown = 1;

    public VoidFunc<Entity> script = Functional.FN_NOOP;
}
