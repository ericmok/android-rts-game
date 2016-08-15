package noteworthyengine.battle;

import java.util.ArrayList;
import java.util.Hashtable;

import noteworthyengine.QuadTreeSystem;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.Node;
import noteworthyframework.QueueMutationList;
import utils.BooleanFunc2;
import utils.QTree;

/**
 * Created by eric on 3/6/15.
 */
public class BattleSystem extends noteworthyframework.System {

    public static final QTree.DistanceMeasurable QTREE_BATTLE_DISTANCE_MEASURE = new QTree.DistanceMeasurable<QuadTreeSystem.QuadTreeNode>() {

        @Override
        public double distanceMeasure(QuadTreeSystem.QuadTreeNode item, QuadTreeSystem.QuadTreeNode candidateItem) {
            BattleNode otherBattleNode = (BattleNode) candidateItem.unit.getNode(BattleNode.class);
            if (otherBattleNode == null) {
                return QTree.INFINITE_DISTANCE;
            }
            BattleNode battleNode = (BattleNode) item.unit.getNode(BattleNode.class);

            // TODO: Target criteria varies per unit class!
            if (battleNode.playerUnitPtr.v == otherBattleNode.playerUnitPtr.v) {
                return QTree.INFINITE_DISTANCE;
            }

            if (battleNodeShouldAttackOther(battleNode, otherBattleNode)) {
                return item.getPosition().squaredDistanceTo(candidateItem.getPosition());
            } else {
                return QTree.INFINITE_DISTANCE;
            }
        }
    };


    public QueueMutationList<BattleNode> battleNodes = new QueueMutationList<BattleNode>(127);

    public Hashtable<PlayerUnit, QueueMutationList<BattleNode>> battleNodesByGamer =
            new Hashtable<PlayerUnit, QueueMutationList<BattleNode>>(8);

    public ArrayList<PlayerUnit> players = new ArrayList<PlayerUnit>(8);

    public BattleNode.Ptr tempBattleNodePtr = new BattleNode.Ptr();

    // External system dependency
    public QuadTreeSystem quadTreeSystem;

    public BattleSystem(QuadTreeSystem quadTreeSystem) {
        this.quadTreeSystem = quadTreeSystem;
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

        double numerator = (distance - (battleNode.battleAttack.range * battleNode.fractionToWalkIntoAttackRange.v));

        // The unit walks towards the enemy with more force than it walks away from it
        // to maintain ~constant range
        if (numerator < 0) {
            numerator *= 0.4;
        }

        double ramp = battleNode.maxSpeed.v *
                numerator / (battleNode.battleAttack.range);

        double mag = Math.min(battleNode.maxSpeed.v, ramp);

        battleNode.enemyAttractionForce.setNormalized();
        battleNode.enemyAttractionForce.scale(mag, mag);
    }

    /**
     * Performs an exhaustive search for the closest target that is not the enemy...
     * @param battleNode
     * @return
     */
    public double findClosestBatleNodeWithinRange(BattleNode.Ptr out, BattleNode battleNode, double range, BooleanFunc2<BattleNode, BattleNode> criteria) {

        QuadTreeSystem.QuadTreeNode quadTreeNode = quadTreeSystem.queryClosestTo(
                (QuadTreeSystem.QuadTreeNode) battleNode.unit.getNode(QuadTreeSystem.QuadTreeNode.class),
                QTREE_BATTLE_DISTANCE_MEASURE);

        if (quadTreeNode != null) {
            BattleNode closest = (BattleNode) quadTreeNode.unit.getNode(BattleNode.class);
            out.v = closest;
        } else {
            out.v = null;
        }

        return Double.MAX_VALUE;
    }

    public void inflictSplashDamage(BattleNode battleNode, double range, BooleanFunc2<BattleNode, BattleNode> criteria) {
        ArrayList<QuadTreeSystem.QuadTreeNode> quadTreeNodes =
                quadTreeSystem.queryRange(battleNode.coords.pos.x,
                        battleNode.coords.pos.y, range);

        for (int i = 0; i < quadTreeNodes.size(); i++) {
            BattleNode target = (BattleNode) quadTreeNodes.get(i).unit.getNode(BattleNode.class);
            if (criteria.apply(battleNode, target)) {
                calculateAndInflictDamage(battleNode, target);
            }
        }
    }

    /**
     * Performs an exhaustive search for the closest target that is not the enemy...
     * @param battleNode
     * @return
     */
    public ArrayList<QuadTreeSystem.QuadTreeNode> xfindBattleNodesWithinRange(BattleNode battleNode, double range, BooleanFunc2<BattleNode, BattleNode> criteria) {
        ArrayList<QuadTreeSystem.QuadTreeNode> quadTreeNodes =
                quadTreeSystem.queryRange(battleNode.coords.pos.x,
                        battleNode.coords.pos.y, range);

        return quadTreeNodes;
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

    /**
     * Inflicts the appropriate amount of damage between 2 nodes.
     * It will account for BattleAttack and BattleArmor differences and
     * apply hp changes.
     *
     * In addition, onAttacked will get called.
     *
     * TODO: May also consider dead node logic here
     * @param battleNode
     * @param target
     */
    public void calculateAndInflictDamage(BattleNode battleNode, BattleNode target) {
        double finalAttack = battleNode.buffAttackDamage(battleNode.battleAttack.amount);
        double finalAttackeeArmor = target.buffArmorAmount(battleNode.target.v.battleArmor.amount);

        double damageReduction = BattleBalance.getDamageMultiplier(
                battleNode.battleAttack.type,
                target.battleArmor.type); // + finalAttackeeArmor * 0.06;

        // TODO: Make this calculation configurable
        double finalDamage = Math.max(0, finalAttack * damageReduction);

        target.hp.v -= finalDamage;
        target.lastAttacker.v = battleNode;
        target.onAttacked(this, battleNode, finalDamage);
    }

    public void updateBattleNode(BattleNode battleNode, double ct, double dt) {

        // So we may or may not have a target (All the enemy may be dead)
        // We step the battle phases anyways

        if (battleNode.battleState.v == BattleNode.BATTLE_STATE_IDLE) {
            battleNode.onFindNewTarget(this);

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

            if (battleNode.battleProgress.v >= battleNode.battleAttack.swingTime) {

                // At attack cast time, ditch the old target for any new targets that
                // walked into the swing (Useful for explosion swings)
                if (battleNode.nonCancellableSwing.v == 0) {
                    battleNode.onFindNewTarget(this);
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

            if (battleNode.battleProgress.v >= battleNode.battleAttack.cooldownTime) {
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
