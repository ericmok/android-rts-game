package noteworthyengine;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import noteworthyframework.*;
import structure.RewriteOnlyArray;
import utils.BooleanFunc2;
import utils.VoidFunc2;

/**
 * Created by eric on 3/6/15.
 */
public class BattleSystem extends noteworthyframework.System {

    public QueueMutationList<BattleNode> battleNodes = new QueueMutationList<BattleNode>(127);

    public Hashtable<Gamer, QueueMutationList<BattleNode>> battleNodesByGamer =
            new Hashtable<Gamer, QueueMutationList<BattleNode>>(8);

    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(8);

    public BattleNode.Ptr tempBattleNodePtr = new BattleNode.Ptr();

    // External system dependency
    public GridSystem gridSystem;

    public BattleSystem(GridSystem gridSystem) {
        this.gridSystem = gridSystem;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == BattleNode.class) {
            BattleNode battleNode = (BattleNode) node;
            battleNodes.queueToAdd(battleNode);

            QueueMutationList gamerUnits = battleNodesByGamer.get(battleNode.gamer.v);
            if (gamerUnits == null) {
                gamerUnits = new QueueMutationList<BattleNode>(127);
                battleNodesByGamer.put(battleNode.gamer.v, gamerUnits);
                gamers.add(battleNode.gamer.v);
            }
            gamerUnits.queueToAdd(battleNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == BattleNode.class) {
            BattleNode battleNode = (BattleNode)node;
            battleNodes.queueToRemove(battleNode);

            QueueMutationList gamerUnits = battleNodesByGamer.get(battleNode.gamer.v);
            if (gamerUnits != null) {
                gamerUnits.queueToRemove(battleNode);
            }
        }
    }

    public void moveNodeTowardsEnemy(BattleNode battleNode, BattleNode otherBattleNode) {
        battleNode.enemyAttractionForce.translate(
                otherBattleNode.coords.pos.x - battleNode.coords.pos.x,
                otherBattleNode.coords.pos.y - battleNode.coords.pos.y);

        double distance = otherBattleNode.coords.pos.distanceTo(battleNode.coords.pos);

        double numerator = (distance - (battleNode.attackRange.v * battleNode.fractionToWalkIntoAttackRange.v));

        // The unit walks towards the enemy with more force than it walks away from it
        // to maintain ~constant range
        if (numerator < 0) {
            numerator *= 0.8;
        }

        double ramp = battleNode.maxSpeed.v *
                numerator / (battleNode.attackRange.v);

        double mag = Math.min(battleNode.maxSpeed.v, ramp);

        battleNode.enemyAttractionForce.setNormalized();
        battleNode.enemyAttractionForce.scale(mag, mag);
    }

    /**
     * Performs an exhaustive search for the closest target that is not the enemy...
     * @param battleNode
     * @return
     */
    public double findAttackablesWithinRange(BattleNode.Ptr out, BattleNode battleNode, double range, BooleanFunc2<BattleNode, BattleNode> criteria) {

        out.v = null;
        double bestDistance = 10000000;

        Grid grid = gridSystem.grid;

        int query = 0;

        while (query <= grid.numberCellsForRange(range)) {

            List<GridNode> gridNodes = grid.getShell(battleNode.gridX.v, battleNode.gridY.v, query);

            for (int i = gridNodes.size() - 1; i >= 0; i--) {

                //BattleNode possibleTarget = (BattleNode)gridNodes.get(i).unit.node(BattleNode._NAME);
                BattleNode possibleTarget = (BattleNode) gridNodes.get(i)._battleNode;

                // Not all gridNodes belong to units that have battleNodes...
                if (possibleTarget == null) {
                    continue;
                }

                // Narrow phase
                if (battleNode.coords.pos.distanceTo(possibleTarget.coords.pos) > range) {
                    continue;
                }

                if (!battleNodeShouldAttackOther(battleNode, possibleTarget)) {
                    continue;
                }
                if (!criteria.apply(battleNode, possibleTarget)) {
                    continue;
                }

                double distance = battleNode.coords.pos.distanceTo(possibleTarget.coords.pos);

                if (distance < range) {

                    // TODO: Check if it is in "front" (not in back)

                    if (distance < bestDistance) {
                        out.v = possibleTarget;
                        bestDistance = distance;
                    }
                }
            }

            // We found a target that meets all criteria, so skip further search
            if (out.v != null) {
                return bestDistance;
            }

            query += 1;
        }

        return bestDistance;
    }

    /**
     * Performs an exhaustive search for the closest target that is not the enemy...
     * @param battleNode
     * @return
     */
    public double findAttackablesWithinRange(RewriteOnlyArray<BattleNode.Target> out, BattleNode battleNode, double range, BooleanFunc2<BattleNode, BattleNode> criteria) {

        out.resetWriteIndex();

        double bestDistance = 10000000;

        Grid grid = gridSystem.grid;
        //List<GridNode> gridNodes = grid.getSurroundingNodes(battleNode.gridX.v, battleNode.gridY.v , range);

        int query = 0;

        while (query <= grid.numberCellsForRange(range)) {

            List<GridNode> gridNodes = grid.getShell(battleNode.gridX.v, battleNode.gridY.v, query);

            for (int i = gridNodes.size() - 1; i >= 0; i--) {
                BattleNode possibleTarget = (BattleNode) gridNodes.get(i).unit.node(BattleNode._NAME);

                // Not all gridNodes belong to units that have battleNodes...
                if (possibleTarget == null) {
                    continue;
                }

                // Narrow phase
                if (battleNode.coords.pos.distanceTo(possibleTarget.coords.pos) > range) {
                    continue;
                }

                if (!battleNodeShouldAttackOther(battleNode, possibleTarget)) {
                    continue;
                }
                if (!criteria.apply(battleNode, possibleTarget)) {
                    continue;
                }

                double distance = battleNode.coords.pos.distanceTo(possibleTarget.coords.pos);

                if (distance < range) {

                    // TODO: Check if it is in "front" (not in back)

                    // Ugly overflow check
                    if (out.size() < out.capacity() - 1) {
                        BattleNode.Target nodeToAdd = out.takeNextWritable();
                        nodeToAdd.v = possibleTarget;
                        nodeToAdd.distance = distance;
                    }
                }
            }

            query += 1;
        }

        return bestDistance;
    }

    /**
     * A range of checks excluding attack range
     * @param battleNode
     * @param otherBattleNode
     * @return
     */
    public static boolean battleNodeShouldAttackOther(BattleNode battleNode, BattleNode otherBattleNode) {
        return (battleNode != otherBattleNode) &&
                //(battleNode.gamer.v != otherBattleNode.gamer.v) &&
                (otherBattleNode.hp.v > 0) &&
                (otherBattleNode.isAttackable.v == 1);
    }

    public boolean battleNodeHasAliveTarget(BattleNode battleNode) {

        // Check if target is null first, then hp
        return battleNode.target.v != null && battleNode.target.v.hp.v > 0;
    }

    private void acquireNewTarget(BattleNode battleNode) {
        // If it has no target or has dead target, get new target
        // Also, we can acquire new target during cooldown, but that may take cycles...
//        if (!battleNodeHasAliveTarget(battleNode) ||
//                battleNode.attackState.v == BattleNode.ATTACK_STATE_READY ||
//                battleNode.stickyAttack.v == 0) {

            // Find closest enemy...may be null
            //findAttackablesWithinRange(tempBattleNodePtr, battleNode, battleNode.targetAcquisitionRange.v, DEFAULT_TARGET_CRITERIA);
            //battleNode.target.v = tempBattleNodePtr.v;
        battleNode.onAcquireTarget.apply(this, battleNode);
       // }
    }


    public boolean cleanUpBattleNode(BattleNode battleNode) {
        if (battleNode.hp.v <= 0) {
            battleNode.onDie.apply(this, battleNode);
            this.getBaseEngine().removeUnit(battleNode.unit);
            battleNode.target.v = null;
            return  true;
        }
        return false;
    }

    public void step(double ct, double dt) {
        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode battleNode = battleNodes.get(i);

            if (cleanUpBattleNode(battleNode)) {
                continue;
            }

            // So we may or may not have a target (All the enemy may be dead)
            // We step the battle phases anyways

            // Zero by default, to be calculated only if there is an enemy
            battleNode.enemyAttractionForce.zero();

            if (battleNodeHasAliveTarget(battleNode)) {
                moveNodeTowardsEnemy(battleNode, battleNode.target.v);
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_READY) {

                acquireNewTarget(battleNode);

                if (battleNodeHasAliveTarget(battleNode)) {
                    // If in range, start the swing immediately

                    if (battleNode.coords.pos.distanceTo(battleNode.target.v.coords.pos) <= battleNode.attackRange.v ||
                            battleNode.attackSwingEvenWhenNotInRange.v == 1) {
                        battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                        battleNode.attackProgress.v = 0;

                        battleNode.onAttackSwing.apply(this, battleNode, battleNode.target.v);
                    }
                }
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                if (battleNode.attackProgress.v >= battleNode.attackSwingTime.v) {

                    // At attack cast time, ditch the old target for any new targets that
                    // walked into the swing
                    if (battleNode.stickyAttack.v == 0) {
                        //findAttackablesWithinRange(tempBattleNodePtr, battleNode, battleNode.attackRange.v, DEFAULT_TARGET_CRITERIA);
                        //battleNode.target.v = tempBattleNodePtr.v;
                        findAttackablesWithinRange(battleNode.target, battleNode, battleNode.attackRange.v, DEFAULT_TARGET_CRITERIA);
                    }

                    if (!battleNodeHasAliveTarget(battleNode)) {
                        // Lost the target before the swing finished (death or out of range)
                        battleNode.onAttackCastFail.apply(this, battleNode);
                    }
                    else {
                        // We do have a target at cast time

                        battleNode.onAttackCast.apply(this, battleNode, battleNode.target.v);

                        // Moved into attack cast
                        //battleNode.target[0].inflictDamage.apply(this, battleNode.target[0], battleNode, battleNode.attackDamage.v);

//                        // Check if battleNode killed something
//                        // We don't nullify the target pointer since it gets fixed in the front of the loop
//                        if (battleNode.target[0].hp.v <= 0) {
//                            this.getBaseEngine().removeUnit(battleNode.target[0].unit);
//
//                            // The dead unit also has a target...
//                            battleNode.target[0].target[0] = null;
//
//                            battleNode.target[0] = null;
//                        }
                    }

                    battleNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN;
                    battleNode.attackProgress.v = 0;
                }
                else {
                    battleNode.attackProgress.v += dt;
                }
            }

            // Invariant to whether or not there is a target
            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN) {

                //acquireNewTarget(battleNode); // Unless we want unit to be idle when in cooldown

                if (battleNode.attackProgress.v >= battleNode.attackCooldown.v) {
                    battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;
                    battleNode.attackProgress.v = 0;

                    // TODO: Simplify this callback
                    battleNode.onAttackReady.apply(this, battleNode, battleNode.target.v);
                }
                else {
                    battleNode.attackProgress.v += dt;
                }
            }
        }
    }

    @Override
    public void flushQueues() {
        battleNodes.flushQueues();

        for (int i = 0; i < gamers.size(); i++) {
            Gamer gamer = gamers.get(i);
            QueueMutationList list = battleNodesByGamer.get(gamer);
            list.flushQueues();
        }
    }


    public static final BooleanFunc2<BattleNode, BattleNode> DEFAULT_TARGET_CRITERIA =
        new BooleanFunc2<BattleNode, BattleNode>() {
        @Override
        public boolean apply(BattleNode battleNode, BattleNode battleNode2) {
            return battleNode.gamer.v != battleNode2.gamer.v && battleNodeShouldAttackOther(battleNode, battleNode2);
        }
    };

    public static final VoidFunc2<BattleSystem, BattleNode> DEFAULT_ON_ACQUIRE_TARGET =
        new VoidFunc2<BattleSystem, BattleNode>() {

        @Override
        public void apply(BattleSystem system, BattleNode node) {
            system.findAttackablesWithinRange(node.target, node, node.targetAcquisitionRange.v, DEFAULT_TARGET_CRITERIA);

            // Test if node has a target, if it doesn't find a new target

//            if (node.target.v == null) {
//                system.findAttackablesWithinRange(node.target, node, node.attackRange.v, node.targetCriteria);
//            }
//            else {
//                if (node.target.v.hp.v < 0) {
//                    system.findAttackablesWithinRange(node.target, node, node.attackRange.v, node.targetCriteria);
//                }
//            }
            //system.findAttackablesWithinRange(sharedTargetsPool, node, node.attackRange.v);


//            if (sharedTargetsPool.size() > 0) {
//                sharedTargetsPool.sort();
//
//                int i = 0;
//                node.target.v = null;
//                while (node.target.v == null && i < sharedTargetsPool.size()) {
//                    if (sharedTargetsPool.get(i).v.gamer.v.team != node.gamer.v.team) {
//                        node.target.v = sharedTargetsPool.get(i).v;
//                    }
//
//                    i += 1;
//                }
//            }
        }
    };
}
