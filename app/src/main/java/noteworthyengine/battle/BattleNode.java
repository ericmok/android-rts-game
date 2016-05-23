package noteworthyengine.battle;

import java.util.ArrayList;

import noteworthyengine.players.PlayerUnitPtr;
import noteworthyframework.Coords;
import structure.RewriteOnlyArray;
import utils.BooleanFunc2;
import utils.DoublePtr;
import utils.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.JsonSerializable;
import utils.Vector2;
import utils.VoidFunc3;

/**
 * Created by eric on 3/6/15.
 */
public class BattleNode extends Node implements BattleTriggerHandler, BattleBuff, Resetable {

    public static final String _NAME = "battleNode";
    public static final int MAX_POSSIBLE_TARGETS = 30;

    public static final RewriteOnlyArray<Target> sharedTargetsPool = new RewriteOnlyArray<Target>(Target.class, 127);


    public static final VoidFunc3<BattleSystem, BattleNode, BattleNode> _DONOTHING3 =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem system, BattleNode element, BattleNode element2) { }
            };

    public static final int BATTLE_STATE_IDLE = 1;
    public static final int BATTLE_STATE_TRYING_TO_MEET_CONDITION_TO_CAST_ON_TARGET = 2;
    public static final int BATTLE_STATE_SWINGING = 3;
    public static final int BATTLE_STATE_WAITING_FOR_COOLDOWN = 4;

    //public static final Gamer _NO_GAMER = new Gamer("none");

    //public GamerPtr gamer;
    public PlayerUnitPtr playerUnitPtr;

    public Coords coords;
    public IntegerPtr gridX;
    public IntegerPtr gridY;

    public Vector2 velocity;
    public DoublePtr maxSpeed;

    /// Used to determine whether to not the unit should move towards target
    public Vector2 fieldForce;
    public Vector2 enemyAttractionForce;

    public DoublePtr startingHp = new DoublePtr() {{ v = 1; }};
    public DoublePtr hp = new DoublePtr() {{ v = 1; }};


    public BattleNode.Ptr lastAttacker = new BattleNode.Ptr() {{ v = null; }};

    public IntegerPtr isAttackable = new IntegerPtr() {{ v = 1; }};

    /// The attack will initiate even when not in range
    public IntegerPtr attackSwingEvenWhenNotInRange = new IntegerPtr() {{ v = 0; }};

    /// The attack swing will be canceled if the unit walks outside of range
    //public IntegerPtr attackSwingCanMiss = new IntegerPtr() {{ v = 0; }};

    //public IntegerPtr attackType = new IntegerPtr() {{ v = BattleBalance.ATTACK_TYPE_KINETIC; }};
    //public DoublePtr attackRange = new DoublePtr() {{ v = 1; }};
    //public DoublePtr attackDamage = new DoublePtr() {{ v = 1; }};
    //public DoublePtr attackSwingTime = new DoublePtr() {{ v = 1; }};
    //public DoublePtr attackCooldown = new DoublePtr() {{ v = 1; }};
    public BattleAttack battleAttack = new BattleAttack();

    public DoublePtr targetAcquisitionRange = new DoublePtr() {{ v = 1; }};

    public BattleArmor battleArmor = new BattleArmor();

    public IntegerPtr battleState = new IntegerPtr() {{ v = BATTLE_STATE_IDLE; }};
    public DoublePtr battleProgress = new DoublePtr() {{ v = 0; }};

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

    public ArrayList<BattleEffect> battleEffects = new ArrayList<>(1);

    public BattleNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, BattleNode.class, this);
    }

    public void emitEvent(BattleSystem battleSystem, BattleTriggerHandler.Event event) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).sendEvent(battleSystem, this, event);
        }
    }

    public final void onSpawn(BattleSystem battleSystem) {
        //onSpawn(battleSystem);
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onSpawn(battleSystem);
        }
        emitEvent(battleSystem, Event.SPAWN);
    }

    public final void onFindNewTarget(BattleSystem battleSystem) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onFindNewTarget(battleSystem);
        }
        emitEvent(battleSystem, Event.FIND_NEW_TARGET);
    }

    public final void onTargetAcquired(BattleSystem battleSystem) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onTargetAcquired(battleSystem);
        }
        emitEvent(battleSystem, Event.TARGET_ACQUIRED);
    }

    public final void onTargetLost(BattleSystem battleSystem) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onTargetLost(battleSystem);
        }
        emitEvent(battleSystem, Event.TARGET_LOST);
    }

    public final void onAttackReady(BattleSystem battleSystem, BattleNode target) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onAttackReady(battleSystem, target);
        }
        emitEvent(battleSystem, Event.ATTACK_READY);
    }

    public final void onAttacked(BattleSystem battleSystem, BattleNode attacker, double damage) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onAttacked(battleSystem, attacker, damage);
        }
        emitEvent(battleSystem, Event.ATTACKED);
    }

    public final void onAttackSwing(BattleSystem battleSystem, BattleNode target) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onAttackSwing(battleSystem, target);
        }
        emitEvent(battleSystem, Event.ATTACK_SWING);
    }

    @Override
    public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onAttackCast(battleSystem, target);
        }
        emitEvent(battleSystem, Event.ATTACK_CAST);
    }

    public final void onAttackCastFail(BattleSystem battleSystem) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onAttackCastFail(battleSystem);
        }
        emitEvent(battleSystem, Event.ATTACK_CAST_FAIL);
    }

    public final void onDie(BattleSystem battleSystem) {
        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).onDie(battleSystem);
        }
        emitEvent(battleSystem, Event.DIE);
    }

    @Override
    public double buffArmorAmount(double in) {
        double value = in;

        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffArmorAmount(value);
        }

        return value;
    }

    public void reset() {

        for (int i = 0; i < battleEffects.size(); i++) {
            battleEffects.get(i).reset();
        }

        this.velocity.zero();
        this.enemyAttractionForce.zero();
        this.target.v = null;
        this.lastAttacker.v = null;
        this.battleState.v = BATTLE_STATE_IDLE;
        this.battleProgress.v = 0;
    }

    public boolean isAlive() {
        return this.hp.v > 0;
    }

    public boolean hasLivingTarget() {
        return target.v != null && target.v.hp.v > 0;
    }

    public boolean targetWithinAttackRange() {
        return this.coords.pos.distanceTo(this.target.v.coords.pos) <= this.battleAttack.range;
    }

    @Override
    public final double buffAttackRange(double in) {
        double value = battleAttack.range;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffAttackRange(value);
        }
        return value;
    }

    @Override
    public final double buffAttackDamage(double in) {
        double value = battleAttack.amount;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffAttackDamage(value);
        }
        return value;
    }

    @Override
    public final double buffAttackSwingTime(double in) {
        double value = battleAttack.swingTime;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffAttackSwingTime(value);
        }
        return value;
    }

    @Override
    public final double buffAttackCooldown(double in) {
        double value = battleAttack.cooldownTime;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffAttackCooldown(value);
        }
        return value;
    }

    @Override
    public final int buffIsAttackable(int in) {
        int value = isAttackable.v;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffIsAttackable(value);
        }
        return value;
    }

    @Override
    public final double buffHp(double in) {
        double value = hp.v;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffHp(value);
        }
        return value;
    }

    @Override
    public final double buffMaxSpeed(double in) {
        double value = maxSpeed.v;
        for (int i = 0; i < battleEffects.size(); i++) {
            value = battleEffects.get(i).buffMaxSpeed(value);
        }
        return value;
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