package model;

import utils.Vector2;

/**
 * Created by eric on 11/11/14.
 */
public class ProjectileCasterComponent extends Component {
    public Vector2 castPoint = new Vector2();
    public Projectile projectile = new Projectile();

    public static enum Phase {
        READY,
        SHOOTING,
        COOLDOWN
    }

    public Phase phase = Phase.READY;

    public double coolDown = 0;
}
