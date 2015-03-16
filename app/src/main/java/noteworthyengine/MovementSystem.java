package noteworthyengine;

import noteworthyframework.*;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementSystem extends noteworthyframework.System {

    public QueueMutationList<MovementNode> movementNodes = new QueueMutationList<MovementNode>(127);

    public MovementSystem() {
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == MovementNode.class) {
            movementNodes.queueToAdd((MovementNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == MovementNode.class) {
            movementNodes.queueToRemove((MovementNode)node);
        }
    }

    @Override
    public void flushQueues() {
        movementNodes.flushQueues();
    }

    @Override
    public void step(double ct, double dt) {

        for (int i = 0; i < movementNodes.items.size(); i++) {

            MovementNode movementNode = movementNodes.items.get(i);

            Vector2 position = movementNode.coords.pos;
            Vector2 velocity = movementNode.velocity;
            Vector2 fieldForce = movementNode.fieldForce;

            movementNode.acceleration.zero();
            movementNode.acceleration.translate(fieldForce.x, fieldForce.y);
            movementNode.acceleration.translate(movementNode.separationForce.x, movementNode.separationForce.y);

            //velocity.x += (Math.random() > 0.5 ? 1 : -1) * GameSettings.UNIT_LENGTH_MULTIPLIER / 30;
            //velocity.y += (Math.random() > 0.5 ? 1 : -1) * GameSettings.UNIT_LENGTH_MULTIPLIER / 30;

            velocity.translate(movementNode.acceleration.x, movementNode.acceleration.y);
            velocity.translate(movementNode.enemyAttractionForce.x, movementNode.enemyAttractionForce.y);
            velocity.withClampMagnitude(movementNode.maxSpeed.v);

            movementNode.gfxOldPosition.copy(position);

            position.translate(velocity.x * dt, velocity.y * dt);
            movementNode.coords.rot.setDirection(velocity);
        }
    }
}
