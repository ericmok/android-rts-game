package noteworthyengine;

import java.util.ArrayList;
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

    private void battleAllCollisionPairs(double ct, double dt) {
        for (int i = 0; i < activeBattleNodes.size(); i++) {

            BattleNode attackerNode = activeBattleNodes.get(i).v;
            BattleNode otherBattleNode = attackerNode.target[0];

            attackerNode.onTargetAcquired.apply(this, attackerNode, otherBattleNode);

            timerLoopMachine.clear();
            timerLoopMachine.addTimer(attackerNode.attackSwingTime.v)
                    .addTimer(attackerNode.attackCooldown.v)
                    .addTimer(0);

            if (attackerNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                timerLoopMachine.setCurrentState(0);
            }
            else if (attackerNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN) {
                timerLoopMachine.setCurrentState(1);
            }
            else if (attackerNode.attackState.v == BattleNode.ATTACK_STATE_READY) {
                timerLoopMachine.setCurrentState(2);
            }

            timerLoopMachine.setCurrentProgress(attackerNode.attackProgress.v);
            timerLoopMachine.step(dt);

            if (timerLoopMachine.hasTransitioned()) {

                // We want to attack but it may not be possible, do extra checks
                if (timerLoopMachine.getCurrentState() == 0) {
                    if (attackerNode.targetDistance.v > attackerNode.attackRange.v) {
                        timerLoopMachine.setCurrentState(2); // Go back to ready
                    } else {
                        attackerNode.onAttackSwing.apply(this, attackerNode, otherBattleNode);
                        attackerNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                    }
                }
                else if (timerLoopMachine.getCurrentState() == 1) {
                    attackerNode.onAttackCast.apply(this, attackerNode, otherBattleNode);
                    otherBattleNode.onHpHit.apply(this, otherBattleNode,
                            attackerNode, attackerNode.attackDamage.v);
                    attackerNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN;
                }
                else if (timerLoopMachine.getCurrentState() == 2) {
                    attackerNode.onAttackReady.apply(this, attackerNode, otherBattleNode);
                    attackerNode.attackState.v = BattleNode.ATTACK_STATE_READY;
                }
            }

            attackerNode.attackProgress.v = timerLoopMachine.getCurrentProgress();

            // If the unit is dead, as a result of it being attacked by this node
            // or by other nodes, remove the unit, clean up the attack state
            if (otherBattleNode.hp.v <= 0) {
                otherBattleNode.onDie.apply(this, otherBattleNode);
                //otherBattleNode.isActive = false;
                //battleNodes.queueToRemove(otherBattleNode);
                this.getBaseEngine().removeUnit(otherBattleNode.unit);
//
//
//                if (attackerNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
//                    attackerNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLODOWN;
//                    attackerNode.attackProgress.v = 0;
//                }
//                else if (attackerNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLODOWN) {
//                    attackerNode.attackState.v = BattleNode.ATTACK_STATE_READY;
//                    attackerNode.attackProgress.v = 0;
//                }
            }

            // Lose the target
            attackerNode.target[0] = null;
            attackerNode.targetDistance.v = 0;


            attackerNode.enemyAttractionForce.translate(
                    otherBattleNode.coords.pos.x - attackerNode.coords.pos.x,
                    otherBattleNode.coords.pos.y - attackerNode.coords.pos.y);

            double distance = otherBattleNode.coords.pos.distanceTo(attackerNode.coords.pos);
            double ramp = attackerNode.maxSpeed.v *
                    (distance - (attackerNode.attackRange.v * 0.9)) / (attackerNode.attackRange.v * 3);
            double mag = Math.min(attackerNode.maxSpeed.v, ramp);

            attackerNode.enemyAttractionForce.scale(mag, mag);
        }
    }

    private void collide(double ct, double dt, BattleNode battleNode, BattleNode otherBattleNode) {

        // Don't collide with self! (In case we want units of same team to collide one day)
        if (battleNode == otherBattleNode) { return; }

        double distance = battleNode.coords.pos.distanceTo(otherBattleNode.coords.pos);

        // Only battles with units in "front" of it
        Vector2.subtract(temp, otherBattleNode.coords.pos, battleNode.coords.pos);
        if (battleNode.coords.rot.dotProduct(temp) < -0.8) {
            return;
        }

        // Only battles with units it can see
        if (distance <= battleNode.targetAcquisitionRange.v) {

            battleNode._hasTarget = true;

            if (battleNode.target[0] != null) {
                // Update old target with new target if closer

                if (distance == battleNode.targetDistance.v) {
                    if (otherBattleNode.tieBreaker.v > battleNode.target[0].tieBreaker.v) {
                        battleNode.targetDistance.v = distance;
                        battleNode.target[0] = otherBattleNode;
                    }
                }
                if (distance < battleNode.targetDistance.v) {
                    battleNode.targetDistance.v = distance;
                    battleNode.target[0] = otherBattleNode;
                }
            }
            else {
                // Battlenode didn't have a target, now it does, wake it up

                battleNode.targetDistance.v = distance;
                battleNode.target[0] = otherBattleNode;

                BattleNode.Ptr battleNodePtr = activeBattleNodes.takeNextWritable();
                battleNodePtr.v = battleNode;
            }

//            CollisionNode collisionNode = collidedBattleNodes.takeNextWritable();
//            collisionNode.battleNode = battleNode;
//            collisionNode.otherBattleNode = otherBattleNode;
//            collisionNode.distance = distance;
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
     * @param out
     * @param battleNode
     * @return
     */
    public double findClosestEnemyFor(BattleNode.Ptr out, BattleNode battleNode) {

        BattleNode bestTarget = null;
        double bestDistance = 1000000000;

        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode possibleTarget = battleNodes.get(i);

            // Cannot attack self after all!
            if (battleNode == possibleTarget) { continue; }
            if (battleNode.gamer.v.team == possibleTarget.gamer.v.team) { continue; }
            if (possibleTarget.hp.v <= 0) { continue; }

            double distance = battleNode.coords.pos.distanceTo(possibleTarget.coords.pos);

            if (distance < battleNode.targetAcquisitionRange.v) {

                // TODO: Check if it is in "front" (not in back)

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestTarget = possibleTarget;
                }
            }
        }

        out.v = bestTarget;

        return bestDistance;
    }

    public void step2(double ct, double dt) {
        for (int i = battleNodes.size() - 1; i >= 0; i--) {
            BattleNode battleNode = battleNodes.get(i);

            // If we run out of attackable targets, the movement won't be calculated
            // We zero it out here
            battleNode.enemyAttractionForce.zero();

            // If it has no target or has dead target, get new target
            if (battleNode.target[0] == null || battleNode.target[0].hp.v <= 0 ||
                    battleNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN) {

                // Find new target

                // Reset
                battleNode.target[0] = null;
                battleNode.targetDistance.v = 0;

                // Find closest enemy...may be null
                double distance = this.findClosestEnemyFor(tempBattleNodePtr, battleNode);
                battleNode.target[0] = tempBattleNodePtr.v;
                battleNode.targetDistance.v = distance;
            }

            // So we may or may not have a target (All the enemy may be dead)
            // We step the battle phases anyways:
            // If in cooldown, let it cool normally
            // TODO: Consider making cooldown as a buff (decaying unit)

            double distanceToSupposedlyAttackableTarget = 0;
            if (battleNode.target[0] != null) {
                distanceToSupposedlyAttackableTarget = battleNode.coords.pos.distanceTo(battleNode.target[0].coords.pos);

                moveNodeTowardsEnemy(battleNode, battleNode.target[0]);
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_READY) {
                if (battleNode.target[0] != null) {

                    // If in range, start the swing immediately
                    if (distanceToSupposedlyAttackableTarget <= battleNode.attackRange.v) {
                        battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                        battleNode.attackProgress.v = 0;

                        battleNode.onAttackSwing.apply(this, battleNode, battleNode.target[0]);
                    }
                }
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                if (battleNode.target[0] != null) {
                    if (battleNode.attackProgress.v >= battleNode.attackSwingTime.v) {

                        battleNode.onAttackCast.apply(this, battleNode, battleNode.target[0]);
                        battleNode.target[0].onHpHit.apply(this, battleNode.target[0], battleNode, battleNode.attackDamage.v);

                        // Check if battleNode killed something
                        // We don't nullify the target pointer since it gets fixed in the front of the loop
                        if (battleNode.target[0].hp.v <= 0) {
                            this.getBaseEngine().removeUnit(battleNode.target[0].unit);

                            // The dead unit also has a target...
                            battleNode.target[0] = null;
                        }

                        battleNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN;
                        battleNode.attackProgress.v = 0;
                    }
                    else {
                        battleNode.attackProgress.v += dt;
                    }
                }
                else {
                    // The enemy died before the swing finished!
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

//            timerLoopMachine.clear();
//            timerLoopMachine.addTimer(0)
//                            .addTimer(battleNode.attackSwingTime.v)
//                            .addTimer(battleNode.attackCooldown.v);
//            timerLoopMachine.setCurrentState(battleNode.attackState.v);
//            timerLoopMachine.setCurrentProgress(battleNode.attackProgress.v);
//            timerLoopMachine.step(dt);
//
//            if (timerLoopMachine.hasTransitioned()) {
//                if (timerLoopMachine.getCurrentState() == 0) {
//                    battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;
//                }
//                else if (timerLoopMachine.getCurrentState() == 1) {
//                    if (battleNode.targetDistance.v > battleNode.attackRange.v) {
//                        timerLoopMachine.setCurrentState(0);
//                    }
//                    battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
//                }
//                else if (timerLoopMachine.getCurrentState() == 2) {
//                    battleNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLDOWN;
//                }
//            }
        }
    }

    @Override
    public void step(double ct, double dt) {

        step2(ct, dt);
        if (1 == 1) return;

        activeBattleNodes.resetWriteIndex();

        for (int i = 0; i < gamers.size(); i++) {
            Gamer gamer = gamers.get(i);
            QueueMutationList<BattleNode> gamerUnits = battleNodesByGamer.get(gamer);

            for (int j = 0; j < gamerUnits.size(); j++) {
                BattleNode battleNode = gamerUnits.get(j);

                battleNode.enemyAttractionForce.zero();

                //if (!battleNode.isActive) { continue; }
                //if (battleNode.hp.v <= 0) { continue; }

                for (int k = 0; k < gamers.size(); k++) {

                    // Don't collide with own team
                    if (i == k) { continue; }

                    Gamer otherGamer = gamers.get(k);
                    QueueMutationList<BattleNode> otherGamerUnits = battleNodesByGamer.get(otherGamer);

                    for (int m = 0; m < otherGamerUnits.size(); m++) {
                        BattleNode otherBattleNode = otherGamerUnits.get(m);

                        //if (!otherBattleNode.isActive) { continue; }
                        //if (otherBattleNode.hp.v <= 0) { continue; }

                        // PHEW!!!
                        collide(ct, dt, battleNode, otherBattleNode);
                    }
                }

            }
        }

        //collidedBattleNodes.sort();
        //battleAllCollisionPairs(ct, dt);
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


//
//    public static class CollisionNode implements Comparable<CollisionNode> {
//        public BattleNode battleNode;
//        public BattleNode otherBattleNode;
//        public double distance;
//
//        @Override
//        public int compareTo(CollisionNode collisionNode) {
//            if (this.distance < collisionNode.distance) return -1;
//            else if (this.distance > collisionNode.distance) return 1;
//            else
//                return 0;
//        }
//    }
}
