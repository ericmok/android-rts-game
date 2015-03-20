package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.Coords;
import noteworthyframework.DoublePtr;
import noteworthyframework.Gamer;
import noteworthyframework.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.TimerLoopMachine;
import utils.Vector2;
import utils.VoidFunc2;
import utils.VoidFunc3;
import utils.VoidFunc4;

/**
 * Created by eric on 3/6/15.
 */
public class BattleNode extends Node {

    public static final String _NAME = "battleNode";

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

    public static final VoidFunc4<BattleSystem, BattleNode, BattleNode, Double> ON_HP_HIT_DEFAULT =
            new VoidFunc4<BattleSystem, BattleNode, BattleNode, Double>() {
                @Override
                public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2, Double damage) {
                    battleNode.hp.v -= damage;
                }
            };

    public static final int ATTACK_STATE_READY = 0;
    public static final int ATTACK_STATE_SWINGING = 1;
    public static final int ATTACK_STATE_WAITING_FOR_COOLDOWN = 2;

    public static final Gamer _NO_GAMER = new Gamer("none");

    public GamerPtr gamer;

    public Coords coords;
    public Vector2 velocity;
    public DoublePtr maxSpeed;

    public Vector2 enemyAttractionForce;

    public DoublePtr startingHp = new DoublePtr() {{ v = 1; }};
    public DoublePtr hp = new DoublePtr() {{ v = 1; }};
    public DoublePtr armor;

    public IntegerPtr isAttackable = new IntegerPtr() {{ v = 1; }};

    public DoublePtr attackRange = new DoublePtr() {{ v = 1; }};
    public DoublePtr targetAcquisitionRange = new DoublePtr() {{ v = 1; }};

    public DoublePtr attackDamage = new DoublePtr() {{ v = 1; }};
    public DoublePtr attackSwingTime = new DoublePtr() {{ v = 1; }};
    public DoublePtr attackCooldown = new DoublePtr() {{ v = 1; }};

    public IntegerPtr attackState = new IntegerPtr() {{ v = ATTACK_STATE_READY; }};
    public DoublePtr attackProgress = new DoublePtr() {{ v = 0; }};

    public boolean _hasTarget = false;
    public DoublePtr tieBreaker = new DoublePtr() {{ v = Math.random(); }};
    public DoublePtr targetDistance = new DoublePtr() {{ v = 0; }};
    public BattleNode[] target = new BattleNode[1];

    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetAcquired = _DONOTHING3;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetLost = _DONOTHING3;

    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackSwing = _DONOTHING3;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackCast = _DONOTHING3;
    public VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackReady = _DONOTHING3;
    public VoidFunc2<BattleSystem, BattleNode> onDie = _DONOTHING2;

    public VoidFunc4<BattleSystem, BattleNode, BattleNode, Double> onHpHit = ON_HP_HIT_DEFAULT;
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
}