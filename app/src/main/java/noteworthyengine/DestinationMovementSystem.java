package noteworthyengine;

import noteworthyframework.*;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 10/8/15.
 */
public class DestinationMovementSystem extends noteworthyframework.System<DestinationMovementNode> {

    public QueueMutationList<DestinationMovementNode> destinationMovementNodes = new QueueMutationList<DestinationMovementNode>(127);

    private Vector2 temp = new Vector2();

    @Override
    public void addNode(Node node) {
        if (node.getClass() == DestinationMovementNode.class) {
            destinationMovementNodes.queueToAdd((DestinationMovementNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == DestinationMovementNode.class) {
            destinationMovementNodes.queueToRemove((DestinationMovementNode)node);
        }
    }

    @Override
    public void step(double ct, double dt) {

        for (int i = 0; i < destinationMovementNodes.items.size(); i++) {

            DestinationMovementNode destinationMovementNode = destinationMovementNodes.items.get(i);

            Vector2 position = destinationMovementNode.coords.pos;
            Vector2 velocity = destinationMovementNode.velocity;
            double speed = destinationMovementNode.maxSpeed.v;
            double accel = destinationMovementNode.maxAcceleartion.v;
            Vector2 destination = destinationMovementNode.destination;

            Vector2.subtract(temp, destination, position);

            if (position.distanceTo(destination) < (speed * dt * 1.01)) {
                VoidFunc<DestinationMovementSystem> callback = destinationMovementNode.onDestinationReached;

                if (callback != null) {
                    callback.apply(this);
                }
            }
            else {
                temp.setNormalized();
                destinationMovementNode.coords.rot.setDirection(temp);
                temp.scale(speed);
                position.translate(temp.x * dt, temp.y * dt);
            }
        }
    }

    @Override
    public void flushQueues() {
        destinationMovementNodes.flushQueues();
    }
}
