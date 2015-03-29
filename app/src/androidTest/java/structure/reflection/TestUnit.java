package structure.reflection;

import noteworthyengine.RenderNode;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/10/15.
 */
public class TestUnit extends Unit {

    public TestNode testNode = new TestNode(this);
    public TestNode2 testNode2 = new TestNode2(this);
    public RenderNode renderNode = new RenderNode(this);

    public TestUnit() {
    }
}
