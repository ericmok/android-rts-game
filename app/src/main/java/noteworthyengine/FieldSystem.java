package noteworthyengine;

import android.util.Log;

import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class FieldSystem extends System {

    public QueueMutationList<FieldNode> troops = new QueueMutationList<FieldNode>(127);
    public QueueMutationList<FieldNode> fields = new QueueMutationList<FieldNode>(127);

    private Vector2 temp = new Vector2();

    public static final Class[] ACCEPTED_NODES = {
        MovementNode.class,
        FieldNode.class
    };

    // To much down casting
    //public Hashtable<Class, QueueMutationList> nodesLists = new Hashtable<Class, QueueMutationList>(ACCEPTED_NODES.length);

    public FieldSystem() {
        super();
    }

    public void addNode(Node node) {
        if (node.getClass() == FieldNode.class) {
            FieldNode fieldNode = (FieldNode) node;

            if (fieldNode._fieldAgentNode != null) {
                troops.queueToAdd(fieldNode);
            }
            if (fieldNode._fieldArrowNode != null) {
                fields.queueToAdd(fieldNode);
            }

//            if (fieldNode.isFieldControl.v == 0) {
//                troops.queueToAdd(fieldNode);
//            } else {
//                fields.queueToAdd(fieldNode);
//            }
        }

//        for (int i = 0; i < ACCEPTED_NODES.length; i++) {
//
//            Class nodeClass = node.getClass();
//
//            if (nodeClass == ACCEPTED_NODES[i]) {
//                if (nodesLists.get(nodeClass) == null) {
//                    nodesLists.put(nodeClass, new QueueMutationList(127));
//                }
//                nodesLists.get(nodeClass).queueToAdd(node);
//            }
//        }
    }

    public void removeNode(Node node) {
        if (node.getClass() == FieldNode.class) {
            FieldNode fieldNode = (FieldNode) node;

            if (fieldNode._fieldAgentNode != null) {
                troops.queueToRemove(fieldNode);
            }
            if (fieldNode._fieldArrowNode != null) {
                fields.queueToRemove(fieldNode);
            }

//            if (fieldNode.isFieldControl.v == 0) {
//                troops.queueToRemove(fieldNode);
//            } else {
//                fields.queueToRemove(fieldNode);
//            }
        }
    }

    @Override
    public void flushQueues() {
        troops.flushQueues();
        fields.flushQueues();
    }

    @Override
    public void step(EngineDataPack engineDataPack, double ct, double dt) {

        for (int i = 0; i < troops.size(); i++) {
            // Loop through each troop to aggregate field forces

            FieldNode troopFieldNode = troops.get(i);

            //Coords troopCoords = (Coords)troopFieldNode.unit.field("coords");
            //Vector2 fieldForce = (Vector2)troopFieldNode.unit.field("fieldForce");

            Coords troopCoords = troopFieldNode._fieldAgentNode.coords;
            Vector2 fieldForce = troopFieldNode._fieldAgentNode.fieldForce;

            fieldForce.zero();

            for (int j = 0; j < fields.size(); j++) {
                // Field controls add to the forces per troop

                FieldNode control = fields.get(j);

                //FieldNode fieldNodeForControl = fields.get(j);
                //FieldUnit fieldUnit = (FieldUnit)fieldNodeForControl.unit;
                //FieldControl fieldControl = fieldUnit.fieldControl;

                // Aggregate
                // TODO:
                //fieldForce.translate(Math.random(), Math.random());
                //double distance = fieldControl.position.distanceTo(troopCoords.pos);

                double sqDistance = control._fieldArrowNode.coords.pos.squaredDistanceTo(troopCoords.pos) + 0.00001;
                double speed = 0.1 / sqDistance;

                temp.copy(control._fieldArrowNode.coords.rot);
                temp.setNormalized();
                temp.scale(speed, speed);

                //fieldForce.translate(Math.random(), Math.random());
                fieldForce.translate(temp.x, temp.y);
            }
        }
    }
}
