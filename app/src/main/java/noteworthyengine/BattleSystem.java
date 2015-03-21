package noteworthyengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import noteworthyframework.*;
import structure.RewriteOnlyArray;
import utils.TimerLoopMachine;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class BattleSystem extends noteworthyframework.System {

    public QueueMutationList<BattleNode> battleNodes = new QueueMutationList<BattleNode>(127);

    public Hashtable<Gamer, QueueMutationList<BattleNode>> battleNodesByGamer =
            new Hashtable<Gamer, QueueMutationList<BattleNode>>(8);

    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(8);

    private RewriteOnlyArray<BattleNode.Ptr> activeBattleNodes =
            new RewriteOnlyArray<BattleNode.Ptr>(BattleNode.Ptr.class, 1024);

    private TimerLoopMachine timerLoopMachine = new TimerLoopMachine(4);

    private BattleNode.Ptr tempBattleNodePtr = new BattleNode.Ptr();
    private Vector2 temp = new Vector2();

    public BattleSystem() {
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
        double ramp = battleNode.maxSpeed.v *
                (distance - (battleNode.attackRange.v * 0.8)) / (battleNode.attackRange.v * 3);
        double mag = Math.min(battleNode.maxSpeed.v, ramp);

        battleNode.enemyAttractionForce.scale(mag, mag);
    }

    /**
     * Performs an exhaustive search for the closest target that is not the enemy...
     * @param battleNode
     * @return
     */
    public BattleNode.Target findClosestEnemyFor(BattleNode battleNode) {

        battleNode.possibleTargets.resetWriteIndex();

        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode possibleTarget = battleNodes.get(i);

            // Cannot attack self after all!
            if (battleNode == possibleTarget) { continue; }
            if (battleNode.gamer.v.team == possibleTarget.gamer.v.team) { continue; }
            if (possibleTarget.hp.v <= 0) { continue; }

            double distance = battleNode.coords.pos.distanceTo(possibleTarget.coords.pos);

            if (distance < battleNode.targetAcquisitionRange.v) {

                // TODO: Check if it is in "front" (not in back)

                if (battleNode.possibleTargets.size() < BattleNode.MAX_POSSIBLE_TARGETS) {
                    BattleNode.Target target = battleNode.possibleTargets.takeNextWritable();
                    target.v = possibleTarget;
                    target.distance = distance;
                }
            }
        }

        battleNode.possibleTargets.sort();
        return battleNode.possibleTargets.get(0);
    }

    public boolean battleNodeHasValidTarget(BattleNode battleNode) {

        // Check if target is null first, then hp
        return battleNode.target[0] != null && battleNode.target[0].hp.v > 0;
    }

    public void step(double ct, double dt) {
        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode battleNode = battleNodes.get(i);

            // If we run out of attackable targets, the movement won't be calculated
            // We zero it out here
            battleNode.enemyAttractionForce.zero();

            // If it has no target or has dead target, get new target
            //if (battleNode.target[0] == null || battleNode.target[0].hp.v <= 0 ||
            if (!battleNodeHasValidTarget(battleNode) ||
                    battleNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN) {

                // Find new target

                // Reset
                battleNode.target[0] = null;
                //battleNode.targetDistance.v = 0;

                // Find closest enemy...may be null
                BattleNode.Target target = this.findClosestEnemyFor(battleNode);
                battleNode.target[0] = target != null ? target.v : null;

                //battleNode.targetDistance.v = target.distance;
            }

            // So we may or may not have a target (All the enemy may be dead)
            // We step the battle phases anyways:
            // If in cooldown, let it cool normally
            // TODO: Consider making cooldown as a buff (decaying unit)

            double distanceToSupposedlyAttackableTarget = 0;
            //if (battleNode.target[0] != null) {
            if (battleNodeHasValidTarget(battleNode)) {
                distanceToSupposedlyAttackableTarget = battleNode.coords.pos.distanceTo(battleNode.target[0].coords.pos);

                moveNodeTowardsEnemy(battleNode, battleNode.target[0]);
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_READY) {
                //if (battleNode.target[0] != null) {
                if (battleNodeHasValidTarget(battleNode)) {
                    // If in range, start the swing immediately
                    if (distanceToSupposedlyAttackableTarget <= battleNode.attackRange.v ||
                            battleNode.attackSwingEvenWhenNotInRange.v == 1) {
                        battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                        battleNode.attackProgress.v = 0;

                        battleNode.onAttackSwing.apply(this, battleNode, battleNode.target[0]);
                    }
                }
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                // Keep getting new targets during the swing if no sticky attack
                if (battleNode.stickyAttack.v == 0) {
                    findClosestEnemyFor(battleNode);
                }

                //if (battleNode.target[0] != null) {
                if (battleNodeHasValidTarget(battleNode)) {

                    if (battleNode.attackProgress.v >= battleNode.attackSwingTime.v) {

                        battleNode.onAttackCast.apply(this, battleNode, battleNode.target[0]);

                        if (battleNode.canAttackMultiple.v == 1) {
                            findClosestEnemyFor(battleNode);

                            // It will have targets b/c of upper check
                            for (int p = battleNode.possibleTargets.size() - 1; p >= 0; p--) {
                                BattleNode.Target targetToCleave = battleNode.possibleTargets.get(p);
                                targetToCleave.v.onHpHit.apply(this, targetToCleave.v, battleNode, battleNode.attackDamage.v);

                                // Check if battleNode killed something
                                // We don't nullify the target pointer since it gets fixed in the front of the loop
                                if (targetToCleave.v.hp.v <= 0) {
                                    this.getBaseEngine().removeUnit(targetToCleave.v.unit);

                                    // The dead unit also has a target...
                                    targetToCleave.v.target[0] = null;
                                }
                            }
                        }
                        else {
                            battleNode.target[0].onHpHit.apply(this, battleNode.target[0], battleNode, battleNode.attackDamage.v);

                            // Check if battleNode killed something
                            // We don't nullify the target pointer since it gets fixed in the front of the loop
                            if (battleNode.target[0].hp.v <= 0) {
                                this.getBaseEngine().removeUnit(battleNode.target[0].unit);

                                // The dead unit also has a target...
                                battleNode.target[0] = null;
                            }

                        }

                        battleNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN;
                        battleNode.attackProgress.v = 0;
                    }
                    else if (battleNode.canAttackSwingMiss.v == 1) {
                        // No damage dealt

                        battleNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN;
                        battleNode.attackProgress.v = 0;
                    }
                    else {
                        battleNode.attackProgress.v += dt;
                    }
                }
                else {
                    // The target disappeared (death or out of range) before the swing finished!
                    battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;
                    battleNode.attackProgress.v = 0;
                }
            }

            // Invariant to whether or not there is a target
            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN) {
                if (battleNode.attackProgress.v >= battleNode.attackCooldown.v) {
                    battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;
                    battleNode.attackProgress.v = 0;

                    // TODO: Simplify this callback
                    battleNode.onAttackReady.apply(this, battleNode, battleNode.target[0]);
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

}
