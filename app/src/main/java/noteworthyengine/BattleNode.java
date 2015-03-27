package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.Coords;
import noteworthyframework.DoublePtr;
import noteworthyframework.Gamer;
import noteworthyframework.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
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

    public static final VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr> INFLICT_DAMAGE_DEFAULT =
            new VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr>() {
                @Override
                public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2, DoublePtr damage) {
                    battleNode.hp.v -= damage.v;
                }
            };

    public static final VoidFunc3<BattleSystem, BattleNode, BattleNode> ON_ATTACK_CAST =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode otherBattleNode) {
                    INFLICT_DAMAGE_DEFAULT.apply(battleSystem, otherBattleNode, battleNode, battleNode.attackDamage);
                }
            };

    public static final VoidFunc2<BattleSystem, BattleNode> ON_ATTACK_CAST_FAIL_DEFAULT =
            new VoidFunc2<BattleSystem, BattleNode>() {
                @Override
                public void apply(BattleSystem system, BattleNode node) {
                    node.attackState.v = BattleNode.ATTACK_STATE_READY;
                    node.attackProgress.v = 0;
                }
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
    public BattleNode[] target = new BattleNode[1];

    /// For dealing damage to multiple targets
    //public RewriteOnlyArray<Target> possibleTargets = new RewriteOnlyArray<Target>(Target.class, MAX_POSSIBLE_TARGETS);

    //public IntegerPtr canAttackMultiple = new IntegerPtr() {{ v = 0; }};

    /// If the attack is sticky, the attack target is obtained at target acquisition time
    /// but not re-evaluated during attack swing
    public IntegerPtr stickyAttack = new IntegerPtr() {{ v = 1; }};

    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetAcquired = _DONOTHING3;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetLost = _DONOTHING3;

    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackSwing = _DONOTHING3;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackCast = ON_ATTACK_CAST;
    public VoidFunc2<BattleSystem, BattleNode> onAttackCastFail = ON_ATTACK_CAST_FAIL_DEFAULT;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackReady = _DONOTHING3;
    public VoidFunc2<BattleSystem, BattleNode> onDie = _DONOTHING2;

    public VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr> inflictDamage = INFLICT_DAMAGE_DEFAULT;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onArmorHit = _DONOTHING3;

    public ArrayList<String> events;

    public void revive() {}
    public void kill() {}

    public BattleNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, BattleNode.class, this);
    }

    public static class Ptr {
        public BattleNode v = null;
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