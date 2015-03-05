package behaviors;

import utils.Vector2;

/**
 * Created by eric on 11/13/14.
 */
public class VelocityComponent extends Component {

    public double moveSpeed = 1;

    public Vector2 velocity = new Vector2();
    public double deltaDegrees = 0;

    public Vector2 acceleration = new Vector2();
}
