package structure.reflection;

import android.app.Application;
import android.test.ApplicationTestCase;

import noteworthyengine.Mech;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/7/15.
 */
public class ReflectionTest extends ApplicationTestCase<Application> {
    public ReflectionTest() {
        super(Application.class);

        //Unit mech = Mech.createMech();
        Unit mech = new Mech();
        assertNotNull(mech.field("coords"));
        assertNotNull(mech.field("velocity"));
        assertNotNull(mech.field("crowdSpeed"));
        assertTrue((Double)mech.field("crowdSpeed") == 1);

        assertNull(mech.field("_name"));
        assertNull(mech.field("NAME"));

        ReflectionClass c = new ReflectionClass();
        Object test = c;
        assertTrue(test.getClass() == ReflectionClass.class);

        TestUnit testUnit = new TestUnit();

        assertNotNull(testUnit.field("instanceVariable"));
        assertNotNull(testUnit.field("initializedInstanceVariable"));

        assertNull(testUnit.field("_privateVariable"));
        assertNull(testUnit.field("truePrivateVariable"));

        assertNull(testUnit.field("CONSTANT"));

        //assertNull(testUnit.field("isActive"));
    }
}