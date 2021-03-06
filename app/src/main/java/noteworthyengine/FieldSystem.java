package noteworthyengine;

import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;

import noteworthyengine.players.PlayerUnit;
import noteworthyframework.*;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class FieldSystem extends noteworthyframework.System {

    public static final double FORCE_WEIGHTING = 2;

    public Hashtable<PlayerUnit, QueueMutationList<FieldNode>> agentsByGamer =
            new Hashtable<PlayerUnit, QueueMutationList<FieldNode>>(4);
            //new QueueMutationList<FieldNode>(127);
    public Hashtable<PlayerUnit, QueueMutationList<FieldNode>> arrowsByGamer =
            new Hashtable<PlayerUnit, QueueMutationList<FieldNode>>(4);
            //new QueueMutationList<FieldNode>(127);

    public ArrayList<PlayerUnit> playerUnits = new ArrayList<PlayerUnit>(4);

    private Vector2 temp = new Vector2();

    public static final Class[] ACCEPTED_NODES = {
        MovementNode.class,
        FieldNode.class
    };

//    public static final int MAX_GAMERS = 4;
//
//    public Hashtable<String, DenormalizedDataSet<FieldNode, String>> nodesByGamer;
//

    // To much down casting
    //public Hashtable<Class, QueueMutationList> nodesLists = new Hashtable<Class, QueueMutationList>(ACCEPTED_NODES.length);

    public FieldSystem() {
        super();
    }

    public void addNode(Node node) {
        if (node.getClass() == FieldNode.class) {
            FieldNode fieldNode = (FieldNode) node;

            if (fieldNode._fieldAgentNode != null) {

                QueueMutationList<FieldNode> fieldAgentNodes = agentsByGamer.get(fieldNode.playerUnitPtr.v);
                if (fieldAgentNodes == null) {
                    fieldAgentNodes = new QueueMutationList<FieldNode>(127);
                    agentsByGamer.put(fieldNode.playerUnitPtr.v, fieldAgentNodes);
                    if (!playerUnits.contains(fieldNode.playerUnitPtr.v)) {
                        playerUnits.add(fieldNode.playerUnitPtr.v);
                    }
                }
                fieldAgentNodes.queueToAdd(fieldNode);

                //troops.queueToAdd(fieldNode);
            }
            if (fieldNode._fieldArrowNode != null) {

                QueueMutationList<FieldNode> fieldArrowNodes = arrowsByGamer.get(fieldNode.playerUnitPtr.v);
                if (fieldArrowNodes == null) {
                    fieldArrowNodes = new QueueMutationList<FieldNode>(127);
                    arrowsByGamer.put(fieldNode.playerUnitPtr.v, fieldArrowNodes);
                    if (!playerUnits.contains(fieldNode.playerUnitPtr.v)) {
                        playerUnits.add(fieldNode.playerUnitPtr.v);
                    }
                }
                fieldArrowNodes.queueToAdd(fieldNode);

                //arrowsByGamer.queueToAdd(fieldNode);
            }

//            if (fieldNode.isFieldControl.v == 0) {
//                troops.queueToAdd(fieldNode);
//            } else {
//                arrowsByGamer.queueToAdd(fieldNode);
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
                QueueMutationList<FieldNode> list = agentsByGamer.get(fieldNode.playerUnitPtr.v);
                if (list != null) {
                    list.queueToRemove(fieldNode);
                }
                //troops.queueToRemove(fieldNode);
            }
            if (fieldNode._fieldArrowNode != null) {
                QueueMutationList<FieldNode> list = arrowsByGamer.get(fieldNode.playerUnitPtr.v);
                if (list != null) {
                    arrowsByGamer.get(fieldNode.playerUnitPtr.v).queueToRemove(fieldNode);
                }
                //arrowsByGamer.queueToRemove(fieldNode);
            }

//            if (fieldNode.isFieldControl.v == 0) {
//                troops.queueToRemove(fieldNode);
//            } else {
//                arrowsByGamer.queueToRemove(fieldNode);
//            }
        }
    }

    @Override
    public void flushQueues() {
        //troops.flushQueues();
        //arrowsByGamer.flushQueues();
        for (int i = 0; i < playerUnits.size(); i++) {
            QueueMutationList<FieldNode> list = agentsByGamer.get(playerUnits.get(i));
            if (list != null) {
                list.flushQueues();
            }
            list = arrowsByGamer.get(playerUnits.get(i));
            if (list != null) {
                list.flushQueues();
            }
        }
    }

    @Override
    public void step(double ct, double dt) {

        for (int gamerIndex = 0; gamerIndex < playerUnits.size(); gamerIndex += 1) {

            PlayerUnit player = playerUnits.get(gamerIndex);

            QueueMutationList<FieldNode> agents = agentsByGamer.get(player);
            QueueMutationList<FieldNode> arrows = arrowsByGamer.get(player);

            if (agents == null) continue;

            for (int i = 0; i < agents.size(); i++) {
                // Loop through each troop to aggregate field forces

                FieldNode troopFieldNode = agents.get(i);

                //Coords troopCoords = (Coords)troopFieldNode.unit.field("coords");
                //Vector2 fieldForce = (Vector2)troopFieldNode.unit.field("fieldForce");

                Coords troopCoords = troopFieldNode._fieldAgentNode.coords;
                Vector2 fieldForce = troopFieldNode._fieldAgentNode.fieldForce;

                //fieldForce.scale(0.3, 0.3);
                fieldForce.zero();

                if (arrows == null) { continue; }

                for (int j = 0; j < arrows.size(); j++) {
                    // Field controls add to the forces per troop

                    FieldNode control = arrows.get(j);

                    //double sqDistance = control._fieldArrowNode.coords.pos.squaredDistanceTo(troopCoords.pos) + 0.00001;
                    //double speed = 1 / sqDistance;
                    double distanceBetweenArrowAndAgent = control._fieldArrowNode.coords.pos.distanceTo(troopCoords.pos) + 0.000001;

                    // TODO: Add other conditions
//                    if (distance > control._fieldArrowNode.fieldArrowInfluenceRadius.v) {
//                        continue;
//                    }

                    double rampDistance = control._fieldArrowNode.rampDistance.v;

                    distanceBetweenArrowAndAgent = Math.min(distanceBetweenArrowAndAgent, rampDistance);

                    // Linear ramp with arbitrary scaling factor
                    double ramp = Math.min( 2 * (rampDistance - distanceBetweenArrowAndAgent + 0.00001) / rampDistance, 2 );
                    ramp = ramp * ramp * troopFieldNode._fieldAgentNode.maxSpeed.v;
                    ramp = FORCE_WEIGHTING * Math.min(ramp, troopFieldNode._fieldAgentNode.maxSpeed.v);

                    temp.copy(control._fieldArrowNode.coords.rot);
                    //temp.setNormalized();
                    temp.scale(ramp, ramp);
                    //temp.scale(speed, speed);

                    //fieldForce.translate(Math.random(), Math.random());
                    fieldForce.translate(temp.x, temp.y);
                }
            }
        }
    }
}
