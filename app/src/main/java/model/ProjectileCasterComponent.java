package model;

import utils.Vector2;

/**
 * Created by eric on 11/11/14.
 */
public class ProjectileCasterComponent extends Component {
    public Vector2 castPoint = new Vector2();

    public static enum Phase {
        READY,
        SHOOTING,
        COOLDOWN
    }

    public Phase phase = Phase.READY;

    public double shootingTime = 1;
    public double shootingProgress = 0;

    public double coolDownTime = 1;
    public double coolDownProgress = 0;

    /**
     * Update the state machine. If the action phase succeeds, process returns true
     * @param dt
     * @return
     */
    public boolean update(double dt) {

        if (phase == Phase.READY) {
            //phase = Phase.SHOOTING;
        }

        if (phase == Phase.SHOOTING) {
            shootingProgress += dt;

            if (shootingProgress >= shootingTime) {
                phase = Phase.COOLDOWN;
                shootingProgress = 0;
                return true;
            }
        }

        if (phase == Phase.COOLDOWN) {
            coolDownProgress += dt;

            if (coolDownProgress >= coolDownTime) {
                phase = Phase.READY;
                coolDownProgress = 0;
            }
        }

        return false;
    }
}
