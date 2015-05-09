package noteworthyengine;

import noteworthyframework.*;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementSystem extends noteworthyframework.System {

    public QueueMutationList<MovementNode> movementNodes = new QueueMutationList<MovementNode>(127);

    public Vector2 temp = new Vector2();
    public Vector2 temp2 = new Vector2();
    public Vector2 temp3 = new Vector2();

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
            Vector2 separationForce = movementNode.separationForce;
            Vector2 formationForce = movementNode.formationForce;

            Vector2 desiredVelocity = temp;
            //desiredVelocity.zero();
            desiredVelocity.copy(movementNode.velocity);
            desiredVelocity.scale(0.4, 0.4);

            // If the field force tries to push units into other units..
            // We assume separation force is zero if not colliding
            double pushForce = fieldForce.dotProduct(separationForce);

            if (pushForce < 0) {
                fieldForce.zero();
            }

            desiredVelocity.translate(fieldForce.x, fieldForce.y);

            if (fieldForce.isZero()) {
                desiredVelocity.translate(movementNode.enemyAttractionForce.x, movementNode.enemyAttractionForce.y);
            }

            temp2.copy(desiredVelocity);

            desiredVelocity.translate(formationForce.x, formationForce.y);
            desiredVelocity.translate(separationForce.x, separationForce.y);

            Vector2.subtract(movementNode.acceleration, desiredVelocity, movementNode.velocity);

            //velocity.x += (Math.random() > 0.5 ? 1 : -1) * GameSettings.UNIT_LENGTH_MULTIPLIER / 30;
            //velocity.y += (Math.random() > 0.5 ? 1 : -1) * GameSettings.UNIT_LENGTH_MULTIPLIER / 30;

            velocity.translate(movementNode.acceleration.x, movementNode.acceleration.y);
            velocity.withClampMagnitude(movementNode.maxSpeed.v);

            movementNode.gfxOldPosition.copy(position);

            position.translate(velocity.x * dt, velocity.y * dt);

            if (!fieldForce.isZero()) {
                movementNode.coords.rot.addVector(fieldForce.x, fieldForce.y, movementNode.turnFactor.v);
            }
            else if (movementNode.target.v != null) {
                Vector2.subtract(temp3, movementNode.target.v.coords.pos, movementNode.coords.pos);
                temp3.setNormalized();
                movementNode.coords.rot.addVector(temp3.x, temp3.y, movementNode.turnFactor.v);
            }
            else if (velocity.magnitude() != 0) {
                movementNode.coords.rot.addVector(velocity.x, velocity.y, movementNode.turnFactor.v);
                //movementNode.coords.rot.setDirection(velocity);
                //movementNode.coords.rot.setDirection(temp2);
            }
        }
    }
}
