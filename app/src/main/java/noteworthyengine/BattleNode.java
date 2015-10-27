package noteworthyengine;

import android.util.Log;

import java.util.ArrayList;

import noteworthyframework.Coords;
import structure.RewriteOnlyArray;
import utils.BooleanFunc2;
import utils.BooleanFunc3;
import utils.DoublePtr;
import noteworthyframework.Gamer;
import noteworthyframework.GamerPtr;
import utils.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.JsonSerializable;
import utils.Vector2;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc3;
import utils.VoidFunc4;

/**
 * Created by eric on 3/6/15.
 */
public class BattleNode extends Node {

    public static final String _NAME = "battleNode";
    public static final int MAX_POSSIBLE_TARGETS = 30;

    public static final RewriteOnlyArray<Target> sharedTargetsPool = new RewriteOnlyArray<Target>(Target.class, 127);


    public static final VoidFunc3<BattleSystem, BattleNode, BattleNode> _DONOTHING3 =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem system, BattleNode element, BattleNode element2) { }
            };

    public static final int ATTACK_STATE_READY = 0;
    public static final int ATTACK_STATE_SWINGING = 1;
    public static final int ATTACK_STATE_WAITING_FOR_COOLDOWN = 2;

    public static final Gamer _NO_GAMER = new Gamer("none");

    public GamerPtr gamer;

    public Coords coords;
    public IntegerPtr gridX;
    public IntegerPtr gridY;

    public Vector2 velocity;
    public DoublePtr maxSpeed;

    public Vector2 enemyAttractionForce;

    public DoublePtr startingHp = new DoublePtr() {{ v = 1; }};
    public DoublePtr hp = new DoublePtr() {{ v = 1; }};
    public DoublePtr armor;

    public IntegerPtr isAttackable = new IntegerPtr() {{ v = 1; }};

    /// The attack will initiate even when not in range
    public IntegerPtr attackSwingEvenWhenNotInRange = new IntegerPtr() {{ v = 0; }};

    /// The attack swing will be canceled if the unit walks outside of range
    //public IntegerPtr attackSwingCanMiss = new IntegerPtr() {{ v = 0; }};

    public DoublePtr attackRange = new DoublePtr() {{ v = 1; }};
    public DoublePtr targetAcquisitionRange = new DoublePtr() {{ v = 1; }};

    public DoublePtr attackDamage = new DoublePtr() {{ v = 1; }};
    public DoublePtr attackSwingTime = new DoublePtr() {{ v = 1; }};
    public DoublePtr attackCooldown = new DoublePtr() {{ v = 1; }};

    public IntegerPtr attackState = new IntegerPtr() {{ v = ATTACK_STATE_WAITING_FOR_COOLDOWN; }};
    public DoublePtr attackProgress = new DoublePtr() {{ v = 0; }};

    public boolean _hasTarget = false;
    public DoublePtr tieBreaker = new DoublePtr() {{ v = Math.random(); }};
    //public DoublePtr targetDistance = new DoublePtr() {{ v = 0; }};

    /// Target lock-on, also acts as a cache. The battleNode will pursue and attack this target
    /// which is obtained at target acquisition time.
    public Ptr target = new Ptr();

    /// For dealing damage to multiple targets
    //public RewriteOnlyArray<Target> possibleTargets = new RewriteOnlyArray<Target>(Target.class, MAX_POSSIBLE_TARGETS);

    //public IntegerPtr canAttackMultiple = new IntegerPtr() {{ v = 0; }};

    /// If the attack is sticky, the attack target is obtained at target acquisition time
    /// but not re-evaluated during attack swing
    public IntegerPtr nonCancellableSwing = new IntegerPtr() {{
        v = 1;
    }};

    /// If true, the node keeps attacking the previous target until it dies
    /// as opposed to just attacking the next closest target on every attack.
    public IntegerPtr lockOnAttack = new IntegerPtr() {{
        v = 0;
    }};

    /// The fudge factor for node to walk within attack range to deal with
    /// various race conditions (attack swing time, round-off error)
    public DoublePtr fractionToWalkIntoAttackRange = new DoublePtr() {{ v = 0.9; }};

    public BooleanFunc2<BattleNode, BattleNode> targetCriteria = BattleSystem.DEFAULT_TARGET_CRITERIA;

    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onArmorHit = _DONOTHING3;

    public ArrayList<String> events;

    public void revive() {}
    public void kill() {}

    public BattleNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, BattleNode.class, this);
    }

    public void findNewTarget(BattleSystem battleSystem) {
        battleSystem.findAttackablesWithinRange(this.target,
                this,
                this.targetAcquisitionRange.v,
                this.targetCriteria);

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

    public void onTargetAcquired() {
    }

    public void onTargetLost() {
    }

    public void onAttackReady(BattleSystem battleSystem, BattleNode target) {
    }

    public void inflictDamage(BattleSystem battleSystem, BattleNode attacker, double damage) {
        this.hp.v -= damage;
    }

    public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
        target.inflictDamage(battleSystem, this, this.attackDamage.v);
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
        if (!target.isAlive()) {
            this.target.v = null;
        }
    }

    public void onAttackSwing(BattleSystem battleSystem, BattleNode target) {
    }

    public void onAttackCastFail(BattleSystem battleSystem) {
        this.attackState.v = ATTACK_STATE_READY;
        this.attackProgress.v = 0;
    }

    public void onDie(BattleSystem battleSystem) {

    }

    public void reset() {
        Log.v("BattleNode reset", "SHOULD BE OVERRIDDEN");
        this.target.v = null;
    }

    public boolean isAlive() {
        return this.hp.v > 0;
    }

    public boolean hasLivingTarget() {
        return target.v != null && target.v.hp.v > 0;
    }

    public boolean targetWithinAttackRange() {
        return this.coords.pos.distanceTo(this.target.v.coords.pos) <= this.attackRange.v;
    }

    public static class Ptr implements JsonSerializable {
        public BattleNode v = null;

        @Override
        public String json() {
            return "\"To be implemented!\"";
        }
    }

    public static class Target implements Comparable<Target>{
        public BattleNode v = null;
        public double distance = 100000000;

        @Override
        public int compareTo(Target target) {
            if (this.distance < target.distance) {
                return -1;
            }
            if (this.distance > target.distance) {
                return 1;
            }
            return 0;
        }
    }
}