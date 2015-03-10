package structure.reflection;

import noteworthyframework.Unit;

/**
 * Created by eric on 3/10/15.
 */
public class TestUnit extends Unit {
    public TestUnit() {
        TestNode testNode = new TestNode(this);
        TestNode2 testNode2 = new TestNode2(this);
    }
}
