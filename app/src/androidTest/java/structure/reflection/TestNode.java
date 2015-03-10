package structure.reflection;

import noteworthyengine.IntegerPtr;
import noteworthyengine.Node;
import noteworthyengine.Unit;
import utils.VoidFunc;

/**
 * Created by eric on 3/10/15.
 */
public class TestNode extends Node {

    public static final String CONSTANT = "Vroom";

    public IntegerPtr initializedInstanceVariable = new IntegerPtr() {{ v = 123; }};

    public IntegerPtr instanceVariable;

    public IntegerPtr _privateVariable;

    private IntegerPtr truePrivateVariable = new IntegerPtr();

    public VoidFunc<Integer> callback = new VoidFunc<Integer>() {
        @Override
        public void apply(Integer element) {

        }
    };

    public TestNode(Unit unit) {
        super("testNode", unit);
        Node.instantiatePublicFieldsForUnit(unit, TestNode.class, this);
    }
}
