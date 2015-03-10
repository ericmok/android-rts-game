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

/**
 * Created by eric on 3/6/15.
 */
public class BattleNode extends Node {

    public static final String _NAME = "battleNode";

    public static final VoidFunc<BattleNode> _DONOTHING = new VoidFunc<BattleNode>() {
        @Override
        public void apply(BattleNode element) { }
    };

    public static final Gamer _NO_GAMER = new Gamer("none");

    public Gamer gamer;

    public Coords coords;
    public Vector2 velocity;

    public DoublePtr startingHp = new DoublePtr() {{ v = 1; }};
    public DoublePtr hp = new DoublePtr() {{ v = 1; }};
    public DoublePtr armor;

    public IntegerPtr isAttackable = new IntegerPtr() {{ v = 1; }};

    public DoublePtr attackRange = new DoublePtr() {{ v = 1; }};
    public DoublePtr targetAcquisitionRange = new DoublePtr() {{ v = 1; }};

    public DoublePtr attackDamage = new DoublePtr() {{ v = 1; }};
    public DoublePtr attackSwingTime = new DoublePtr() {{ v = 1; }};
    public DoublePtr attackCooldown = new DoublePtr() {{ v = 1; }};

    public IntegerPtr attackState = new IntegerPtr() {{ v = 0; }};
    public IntegerPtr attackProgress = new IntegerPtr() {{ v = 0; }};

    public VoidFunc<BattleNode> onAttack = _DONOTHING;
    public VoidFunc<BattleNode> onDie = _DONOTHING;
    public VoidFunc<BattleNode> onTargetAcquired = _DONOTHING;

    public VoidFunc<BattleNode> onHpHit = _DONOTHING;
    public VoidFunc<BattleNode> onArmorHit = _DONOTHING;

    public ArrayList<String> events;

    public void revive() {}
    public void kill() {}

    public BattleNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, BattleNode.class, this);
    }
}