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

    public static final VoidFunc2<BattleSystem, BattleNode> _DONOTHING2 =
        new VoidFunc2<BattleSystem, BattleNode>() {
            @Override
            public void apply(BattleSystem system, BattleNode element) { }
        };
    public static final VoidFunc3<BattleSystem, BattleNode, BattleNode> _DONOTHING3 =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem system, BattleNode element, BattleNode element2) { }
            };
//
//    public static final VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr> INFLICT_DAMAGE_DEFAULT =
//            new VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr>() {
//                @Override
//                public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2, DoublePtr damage) {
//                    battleNode.hp.v -= damage.v;
//                }
//            };
//
//    public static final VoidFunc3<BattleSystem, BattleNode, BattleNode> ON_ATTACK_CAST =
//            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
//                @Override
//                public void apply(BattleSystem battleSystem, BattleNode attacker, BattleNode otherBattleNode) {
//                    otherBattleNode.inflictDamage.apply(battleSystem, otherBattleNode, attacker, attacker.attackDamage);
//                }
//            };
//
//    public static final VoidFunc2<BattleSystem, BattleNode> ON_ATTACK_CAST_FAIL_DEFAULT =
//            new VoidFunc2<BattleSystem, BattleNode>() {
//                @Override
//                public void apply(BattleSystem system, BattleNode node) {
//                    node.attackState.v = BattleNode.ATTACK_STATE_READY;
//                    node.attackProgress.v = 0;
//                }
//            };

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
    public IntegerPtr stickyAttack = new IntegerPtr() {{ v = 1; }};

    /// The fudge factor for node to walk within attack range to deal with
    /// various race conditions (attack swing time, round-off error)
    public DoublePtr fractionToWalkIntoAttackRange = new DoublePtr() {{ v = 0.9; }};

    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetAcquired = _DONOTHING3;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetLost = _DONOTHING3;

    public BooleanFunc2<BattleNode, BattleNode> targetCriteria = BattleSystem.DEFAULT_TARGET_CRITERIA;

    public VoidFunc2<BattleSystem, BattleNode> onAcquireTarget = BattleSystem.DEFAULT_ON_ACQUIRE_TARGET;

    //public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackSwing = _DONOTHING3;
    //public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackCast = ON_ATTACK_CAST;
    //public VoidFunc2<BattleSystem, BattleNode> onAttackCastFail = ON_ATTACK_CAST_FAIL_DEFAULT;
    //public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackReady = _DONOTHING3;
    //public VoidFunc2<BattleSystem, BattleNode> onDie = _DONOTHING2;

    //public VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr> inflictDamage = INFLICT_DAMAGE_DEFAULT;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onArmorHit = _DONOTHING3;

    public ArrayList<String> events;

    public void revive() {}
    public void kill() {}

    public BattleNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, BattleNode.class, this);
    }

    public void onAttackReady(BattleSystem battleSystem, BattleNode target) {
    }

    public void inflictDamage(BattleSystem battleSystem, BattleNode attacker, double damage) {
        this.hp.v -= damage;
    }

    public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
        target.inflictDamage(battleSystem, target, target.attackDamage.v);
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