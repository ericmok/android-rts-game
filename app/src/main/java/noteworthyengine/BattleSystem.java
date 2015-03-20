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
            BattleNode battleNode = (BattleNode) node;
            battleNodes.queueToRemove((BattleNode)node);

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
            else if (attackerNode.attackState.v == BattleNode.ATTACK_STATE_WAITING_FOR_COOLODOWN) {
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
                    attackerNode.attackState.v = BattleNode.ATTACK_STATE_WAITING_FOR_COOLODOWN;
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

    @Override
    public void step(double ct, double dt) {

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
        battleAllCollisionPairs(ct, dt);
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
