package noteworthyengine;

import structure.GameSettings;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementSystem extends System {

    public MovementSystem() {
    }

    public void step(QueueMutationList<MovementNode> movementNodes, double ct, double dt) {

        movementNodes.flushQueues();

        for (int i = 0; i < movementNodes.items.size(); i++) {
            Vector2 position = movementNodes.items.get(i).coords.pos;
            Vector2 velocity = movementNodes.items.get(i).velocity;

            velocity.x += (Math.random() > 0.5 ? 1 : -1) * GameSettings.UNIT_LENGTH_MULTIPLIER / 30;
            velocity.y += (Math.random() > 0.5 ? 1 : -1) * GameSettings.UNIT_LENGTH_MULTIPLIER / 30;

            position.translate(velocity.x * dt, velocity.y * dt);
        }
    }
}
