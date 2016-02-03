package noteworthyengine;

import java.util.List;

import noteworthyengine.players.PlayerUnit;
import noteworthyframework.*;
import utils.QueueMutationHashedList;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class FormationSystem extends noteworthyframework.System {

    public static final int MAX_GAMERS = 4;

    public QueueMutationList<FormationNode> nodes = new QueueMutationList<FormationNode>(127);
    public QueueMutationList<FormationNode.FormationLeader> formationLeaders = new QueueMutationList<>(127);
    public QueueMutationList<FormationNode.FormationSheep> formationSheeps = new QueueMutationList<>(127);

    public QueueMutationHashedList<PlayerUnit, FormationNode> nodesByGamer =
            new QueueMutationHashedList<PlayerUnit, FormationNode>(3, 127);

    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();

    public GridSystem gridSystem;

    public FormationSystem(GridSystem gridSystem) {
        super();
        this.gridSystem = gridSystem;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == FormationNode.class) {
            FormationNode formationNode = (FormationNode)node;
            nodes.queueToAdd(formationNode);
            nodesByGamer.queueToAdd(formationNode.playerUnitPtr.v, formationNode);
        }
        if (node.getClass() == FormationNode.FormationLeader.class) {
            formationLeaders.queueToAdd((FormationNode.FormationLeader)node);
        }
        if (node.getClass() == FormationNode.FormationSheep.class) {
            formationSheeps.queueToAdd((FormationNode.FormationSheep)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == FormationNode.class) {
            FormationNode formationNode = (FormationNode)node;
            nodes.queueToRemove(formationNode);
            nodesByGamer.queueToRemove(formationNode.playerUnitPtr.v, formationNode);
        }
        if (node.getClass() == FormationNode.FormationLeader.class) {
            formationLeaders.queueToRemove((FormationNode.FormationLeader) node);
        }
        if (node.getClass() == FormationNode.FormationSheep.class) {
            formationSheeps.queueToRemove((FormationNode.FormationSheep) node);
        }
    }

    public void acquireFormationSheep(FormationNode.FormationLeader leader) {

        if (!leader.canTakeSheep()) {
            return;
        }

        int query = 0;

        List<GridNode> gridNodeList;

        while (query < gridSystem.grid.numberCellsForRange(leader.sheepAcquisitionDistance.v)) {
            gridNodeList = gridSystem.grid.getShell(leader.gridX.v, leader.gridY.v, query);

            for (int i = gridNodeList.size() - 1; i >= 0; i--) {
                GridNode gridNode = gridNodeList.get(i);

                FormationNode.FormationSheep sheep = (FormationNode.FormationSheep)gridNode.unit.node(FormationNode.FormationSheep.NAME);

                if (sheep == null) {
                    continue;
                }

                if (sheep.playerUnitPtr.v != leader.playerUnitPtr.v) {
                    continue;
                }

                if (sheep.unit == leader.unit) {
                    continue;
                }

                if (sheep.hasLeader()) {
                    continue;
                }

                if (!leader.canTakeSheep()) {
                    return;
                }

                int sheepIndex = leader.takeIndex();
                sheep.formationIndex.v = sheepIndex;
                sheep.leaderToFollow.v = leader;
//                leader.calculateSheepPosition(sheep.formationDestination, sheepIndex);
//                //leader.formationForce.x = sheep.formationDestination.x - sheep.coords.pos.x;
//                //leader.formationForce.y = sheep.formationDestination.y - sheep.coords.pos.y;
//                //sheep.formationForce.x = sheep.formationDestination.x - sheep.coords.pos.x;
//                //sheep.formationForce.y = sheep.formationDestination.y - sheep.coords.pos.y;
//                sheep.formationForce.x = sheep.formationDestination.x - sheep.coords.pos.x;
//                sheep.formationForce.y = sheep.formationDestination.y - sheep.coords.pos.y;
//                sheep.formationForce.setNormalized();
//                sheep.formationForce.scale(sheep.maxSpeed.v, sheep.maxSpeed.v);
                leader.sheeps.add(sheep);
            }

            query += 1;
        }
    }

    @Override
    public void step(double ct, double dt) {

        for (int i = formationLeaders.size() - 1; i >= 0; i--) {
            FormationNode.FormationLeader formationLeader = formationLeaders.get(i);

            acquireFormationSheep(formationLeader);

            for (int j = formationLeader.sheeps.size() - 1; j >= 0; j--) {
                FormationNode.FormationSheep sheep = formationLeader.sheeps.get(j);
                formationLeader.calculateSheepPosition(sheep.formationDestination, sheep.formationIndex.v);
                sheep.formationForce.x = sheep.formationDestination.x - sheep.coords.pos.x;
                sheep.formationForce.y = sheep.formationDestination.y - sheep.coords.pos.y;

                double dx = sheep.formationForce.x;
                double dy = sheep.formationForce.y;
                double mag = sheep.formationForce.magnitude();

                //sheep.formationForce.setNormalized();
                sheep.formationForce.x /= mag;
                sheep.formationForce.y /= mag;

                double rampSpeed = Math.max(Math.min(mag / 1, 1), 0);

                sheep.formationForce.scale(rampSpeed * sheep.maxSpeed.v, rampSpeed * sheep.maxSpeed.v);
            }
        }

//        for (int i = formationSheeps.size() - 1; i >= 0; i--) {
//            FormationNode.FormationSheep sheep = formationSheeps.get(i);
//            if (sheep.hasLeader()) {
//                //double dx = sheep.leaderToFollow.v.coords.pos.x - sheep.coords.pos.x;
//                //double dy = sheep.leaderToFollow.v.coords.pos.y - sheep.coords.pos.y;
//                //temp.set(dx, dy);
//                //temp.setNormalized();
//                //temp.scale(sheep.maxSpeed.v, sheep.maxSpeed.v);
//
////                double dx = sheep.formationDestination.x - sheep.coords.pos.x;
////                double dy = sheep.formationDestination.x - sheep.coords.pos.x;
////                temp.set(dx, dy);
////                temp.setNormalized();
////                temp.scale(sheep.maxSpeed.v, sheep.maxSpeed.v);
//
//  //              sheep.formationForce.copy(temp);
//                //sheep.coords.pos.translate(temp.x * dt, temp.y * dt);
//            } else {
//                sheep.formationForce.zero();
//            }
//        }

//        for (int i = nodesByGamer.numberKeys() - 1; i >= 0; i--) {
//            QueueMutationList<FormationNode> nodesForGamerOne = nodesByGamer.getListByKeyIndex(i);
//
//            int numberNodesForGamer = nodesForGamerOne.size();
//
//            for (int j = numberNodesForGamer - 1; j >= 0; j--) {
//                FormationNode formationNode = nodesForGamerOne.get(j);
//                formationNode.formationForce.zero();
//
//                for (int k = numberNodesForGamer - 1; k >= 0; k--) {
//
//                    if (j == k) continue;
//
//                    FormationNode otherNode = nodesForGamerOne.get(k);
//
//                    Vector2.subtract(temp, formationNode.coords.pos, otherNode.coords.pos);
//                    double mag = temp.magnitude();
//
//                    double formationRadius = 1.4;
//
//                   if (mag < formationRadius || mag > formationRadius * 3) {
//                        continue;
//                    }
//
//                    double epsilon = 0.00001;
//                    double distance = mag;
//
//                    // Smaller the distance, the lower the force (could try also sqDistance)
//                    double cosinusoidal = Math.cos(formationRadius * distance * 2 * Math.PI) / distance;
//                    //double cosinusoidal = Math.cos(formationRadius * 2 * Math.PI) / (distance);
//
//                    // Scale by dot product with the perpendicular
//                    //formationNode.coords.rot.getPerpendicular(temp2);
//
//                    // See if otherNode is along perp or not
//                    //temp.setNormalized();
//                    //double dotProduct = temp2.dotProduct(temp);
//                    //dotProduct = Math.abs(dotProduct); // To prevent repulsions
//
//                    //cosinusoidal = cosinusoidal * dotProduct;
//
//                    // TODO: Average the formation forces
//                    temp.setNormalized();
//                    temp.scale(0.5 * cosinusoidal / numberNodesForGamer, 0.5 * cosinusoidal / numberNodesForGamer);
//
//                    formationNode.formationForce.translate(temp.x, temp.y);
//                }
//            }
//        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
        nodesByGamer.flushQueues();

        for (int i = formationLeaders.itemsToRemove.size() - 1; i >= 0; i--) {
            FormationNode.FormationLeader leader = formationLeaders.itemsToRemove.get(i);
            for (int j = leader.sheeps.size() - 1; j >= 0; j--) {
                if (leader.sheeps.get(j).hasLeader()) {
                    leader.sheeps.get(j).leaderToFollow.v = null;
                }
            }
        }

        for (int i = formationSheeps.itemsToRemove.size() - 1; i >= 0; i--) {
            FormationNode.FormationSheep sheep = formationSheeps.itemsToRemove.get(i);
            if (sheep.hasLeader()) {
                sheep.leaderToFollow.v.removeSheep(sheep);
            }
        }

        formationLeaders.flushQueues();
        formationSheeps.flushQueues();
    }
}
