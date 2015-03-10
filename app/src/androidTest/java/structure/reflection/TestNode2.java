package structure.reflection;

import noteworthyengine.IntegerPtr;
import noteworthyengine.Node;
import noteworthyengine.Unit;

/**
 * Created by eric on 3/10/15.
 */
public class TestNode2 extends Node {

    public IntegerPtr initializedInstanceVariable;

    public IntegerPtr mixinVariable;

    public TestNode2(Unit unit) {
        super("testNode2", unit);
        Node.instantiatePublicFieldsForUnit(unit, TestNode2.class, this);
    }
}
