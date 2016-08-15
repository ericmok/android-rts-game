package tests.reflection;

import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.IntegerPtr;

/**
 * Created by eric on 3/10/15.
 */
public class TestNode2 extends Node {

    public IntegerPtr initializedInstanceVariable;

    public IntegerPtr mixinVariable;

    public TestNode2(Unit unit) {
        super(TestNode2.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, TestNode2.class, this);
    }
}
