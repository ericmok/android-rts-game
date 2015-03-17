package noteworthyengine;

import noteworthyframework.*;
import utils.Orientation;
import utils.QueueMutationHashedList;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class FormationSystem extends noteworthyframework.System {

    public static final int MAX_GAMERS = 4;

    public QueueMutationList<FormationNode> nodes = new QueueMutationList<FormationNode>(127);

    public QueueMutationHashedList<Gamer, FormationNode> nodesByGamer =
            new QueueMutationHashedList<Gamer, FormationNode>(3, 127);

    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();

    @Override
    public void addNode(Node node) {
        if (node.getClass() == FormationNode.class) {
            FormationNode formationNode = (FormationNode)node;
            nodes.queueToAdd(formationNode);
            nodesByGamer.queueToAdd(formationNode.gamer.v, formationNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == FormationNode.class) {
            FormationNode formationNode = (FormationNode)node;
            nodes.queueToRemove(formationNode);
            nodesByGamer.queueToRemove(formationNode.gamer.v, formationNode);
        }
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = nodesByGamer.numberKeys() - 1; i >= 0; i--) {
            QueueMutationList<FormationNode> nodesForGamerOne = nodesByGamer.getListByKeyIndex(i);

            for (int j = nodesForGamerOne.size() - 1; j >= 0; j--) {
                FormationNode formationNode = nodesForGamerOne.get(j);
                formationNode.formationForce.zero();

                for (int k = nodesForGamerOne.size() - 1; k >= 0; k--) {

                    if (j == k) continue;

                    FormationNode otherNode = nodesForGamerOne.get(k);

                    Vector2.subtract(temp, formationNode.coords.pos, otherNode.coords.pos);
                    double mag = temp.magnitude();

                    double formationRadius = 1;

                    if (mag < formationRadius) {
                        continue;
                    }

                    double distance = mag + formationRadius;

                    // Smaller the distance, the lower the force (could try also sqDistance)
                    double cosinusoidal = Math.cos(distance * Math.PI) / (distance);
                    //double cosinusoidal = Math.cos(formationRadius * 2 * Math.PI) / (distance);

                    // Scale by dot product with the perpendicular
                    //formationNode.coords.rot.getPerpendicular(temp2);

                    // See if otherNode is along perp or not
                    //temp.setNormalized();
                    //double dotProduct = temp2.dotProduct(temp);
                    //dotProduct = Math.abs(dotProduct); // To prevent repulsions

                    //cosinusoidal = cosinusoidal * dotProduct;

                    // TODO: Average the formation forces

                    temp.scale(cosinusoidal, cosinusoidal);

                    formationNode.formationForce.translate(temp.x, temp.y);
                }
            }
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
        nodesByGamer.flushQueues();
    }
}
