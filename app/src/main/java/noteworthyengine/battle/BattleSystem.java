package noteworthyengine.battle;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import noteworthyengine.Grid;
import noteworthyengine.GridNode;
import noteworthyengine.GridSystem;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.*;
import structure.RewriteOnlyArray;
import utils.BooleanFunc2;

/**
 * Created by eric on 3/6/15.
 */
public class BattleSystem extends noteworthyframework.System {

    public QueueMutationList<BattleNode> battleNodes = new QueueMutationList<BattleNode>(127);

    public Hashtable<PlayerUnit, QueueMutationList<BattleNode>> battleNodesByGamer =
            new Hashtable<PlayerUnit, QueueMutationList<BattleNode>>(8);

    public ArrayList<PlayerUnit> players = new ArrayList<PlayerUnit>(8);

    public BattleNode.Ptr tempBattleNodePtr = new BattleNode.Ptr();

    // External system dependency
    public GridSystem gridSystem;

    public BattleSystem(GridSystem gridSystem) {
        this.gridSystem = gridSystem;
    }

    @Override
    public void addNode(Node node) {
        if (node instanceof BattleNode) {
            BattleNode battleNode = (BattleNode) node;
            battleNodes.queueToAdd(battleNode);

            QueueMutationList gamerUnits = battleNodesByGamer.get(battleNode.playerUnitPtr.v);
            if (gamerUnits == null) {
                gamerUnits = new QueueMutationList<BattleNode>(127);
                battleNodesByGamer.put(battleNode.playerUnitPtr.v, gamerUnits);
                players.add(battleNode.playerUnitPtr.v);
            }
            gamerUnits.queueToAdd(battleNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof BattleNode) {
            BattleNode battleNode = (BattleNode)node;
            battleNodes.queueToRemove(battleNode);

            QueueMutationList gamerUnits = battleNodesByGamer.get(battleNode.playerUnitPtr.v);
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
            numerator *= 0.4;
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
                (otherBattleNode.isAlive()) &&
                (otherBattleNode.isAttackable.v == 1);
    }

    public boolean removeAndCleanDeadBattleNode(BattleNode battleNode) {
        if (!battleNode.isAlive()) {
            battleNode.onDie(this);

            // TODO: Do a reset
            battleNode.target.v = null;
            this.getBaseEngine().removeUnit(battleNode.unit);

            return true;
        }
        return false;
    }

    public void updateBattleNode(BattleNode battleNode, double ct, double dt) {

        // So we may or may not have a target (All the enemy may be dead)
        // We step the battle phases anyways

        if (battleNode.battleState.v == BattleNode.BATTLE_STATE_IDLE) {
            battleNode.findNewTarget(this);

            if (battleNode.hasLivingTarget()) {
                battleNode.battleState.v = BattleNode.BATTLE_STATE_TRYING_TO_MEET_CONDITION_TO_CAST_ON_TARGET;
            }
        }

        // Zero by default, to be calculated only if there is an enemy
        battleNode.enemyAttractionForce.zero();

        //if (battleNode.hasLivingTarget()) {
        if (battleNode.battleState.v == BattleNode.BATTLE_STATE_TRYING_TO_MEET_CONDITION_TO_CAST_ON_TARGET) {

            // Prematrue idling prevents units from shooting and moving at same time
            //if (battleNode.fieldForce.magnitude() > battleNode.maxSpeed.v / 2) {
            //    battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
            //    return;
            //}

            if (!battleNode.hasLivingTarget()) {
                battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
            }
            else {
                if (!battleNode.targetWithinAttackRange()) {
                    // TODO: Make it customizable on how to achieve attack range condition
                    moveNodeTowardsEnemy(battleNode, battleNode.target.v);

                    if (battleNode.fieldForce.magnitude() > battleNode.maxSpeed.v / 2) {
                        battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
                    }
                    // Non sticky Case
                    //battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
                }
                if (battleNode.targetWithinAttackRange() || battleNode.attackSwingEvenWhenNotInRange.v == 1) {
                    battleNode.battleState.v = BattleNode.BATTLE_STATE_SWINGING;
                    battleNode.battleProgress.v = 0;
                    battleNode.onAttackSwing(this, battleNode.target.v);
                }
            }
        }

        if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING) {

            if (battleNode.battleProgress.v >= battleNode.attackSwingTime.v) {

                // At attack cast time, ditch the old target for any new targets that
                // walked into the swing (Useful for explosion swings)
                if (battleNode.nonCancellableSwing.v == 0) {
                    battleNode.findNewTarget(this);
                }

                if (!battleNode.hasLivingTarget()) {
                    // Lost the target before the swing finished (death or out of range)
                    battleNode.onAttackCastFail(this);
                }
                else {
                    // We do have a target at cast time
                    battleNode.onAttackCast(this, battleNode.target.v);
                }

                battleNode.battleState.v = BattleNode.BATTLE_STATE_WAITING_FOR_COOLDOWN;
                battleNode.battleProgress.v = 0;
            }
            else {
                if (!battleNode.hasLivingTarget() && battleNode.nonCancellableSwing.v == 0) {
                    battleNode.target.v = null;
                    battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
                    battleNode.battleProgress.v = 0;
                }
                else {
                    battleNode.battleProgress.v += dt;
                }
            }
        }

        // Invariant to whether or not there is a target
        if (battleNode.battleState.v == BattleNode.BATTLE_STATE_WAITING_FOR_COOLDOWN) {

            //acquireNewTarget(battleNode); // Unless we want unit to be idle when in cooldown

            if (battleNode.battleProgress.v >= battleNode.attackCooldown.v) {
                battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
                battleNode.battleProgress.v = 0;

                // TODO: Simplify this callback
                battleNode.onAttackReady(this, battleNode.target.v);
            }
            else {
                battleNode.battleProgress.v += dt;
            }
        }
    }

    public void step(double ct, double dt) {
        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode battleNode = battleNodes.get(i);
            updateBattleNode(battleNode, ct, dt);
        }

        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode battleNode = battleNodes.get(i);

            removeAndCleanDeadBattleNode(battleNode);

            // This addresses a bug where a target dies midswing in one frame
            // Then in the next frame a new unit is spawned that borrows the same BattleNode
            if (!battleNode.hasLivingTarget()) {
                battleNode.target.v = null;
            }
        }
    }

    @Override
    public void flushQueues() {
        battleNodes.flushQueues();

        for (int i = 0; i < players.size(); i++) {
            PlayerUnit player = players.get(i);
            QueueMutationList list = battleNodesByGamer.get(player);
            list.flushQueues();
        }
    }

    public static final BooleanFunc2<BattleNode, BattleNode> DEFAULT_TARGET_CRITERIA =
        new BooleanFunc2<BattleNode, BattleNode>() {
        @Override
        public boolean apply(BattleNode battleNode, BattleNode battleNode2) {
            return battleNode.playerUnitPtr.v != battleNode2.playerUnitPtr.v;
        }
    };
}
