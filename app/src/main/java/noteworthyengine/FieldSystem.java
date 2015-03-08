package noteworthyengine;

import java.util.ArrayList;

import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class FieldSystem extends System {

    public QueueMutationList<FieldNode> troops = new QueueMutationList<FieldNode>(127);
    public QueueMutationList<FieldNode> fields = new QueueMutationList<FieldNode>(127);

    public void addNode(Node node) {
        if (node.getClass() == FieldNode.class) {
            FieldNode fieldNode = (FieldNode) node;

            if (fieldNode.isFieldControl.v == 0) {
                troops.queueToAdd(fieldNode);
            } else {
                fields.queueToAdd(fieldNode);
            }
        }
    }

    public void removeNode(Node node) {
        if (node.getClass() == FieldNode.class) {
            FieldNode fieldNode = (FieldNode) node;

            if (fieldNode.isFieldControl.v == 0) {
                troops.queueToRemove(fieldNode);
            } else {
                fields.queueToRemove(fieldNode);
            }
        }
    }

    @Override
    public void flushQueues() {
        troops.flushQueues();
        fields.flushQueues();
    }

    public void step(double ct, double dt) {

        for (int i = 0; i < troops.size(); i++) {
            // Loop through each troop to aggregate field forces

            FieldNode troopFieldNode = troops.get(i);
            Coords troopCoords = (Coords)troopFieldNode.unit.field("coords");

            Vector2 fieldForce = (Vector2)troopFieldNode.unit.field("fieldForce");
            fieldForce.zero();

            for (int j = 0; j < fields.size(); j++) {
                // Field controls add to the forces per troop

                FieldNode fieldNodeForControl = fields.get(j);
                FieldUnit fieldUnit = (FieldUnit)fieldNodeForControl.unit;
                FieldControl fieldControl = fieldUnit.fieldControl;

                // Aggregate
                // TODO:
                //fieldForce.translate(Math.random(), Math.random());
                //double distance = fieldControl.position.distanceTo(troopCoords.pos);

                fieldForce.translate(Math.random(), Math.random());
            }
        }
    }
}
