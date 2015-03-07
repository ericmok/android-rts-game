package structure;

import android.app.Application;
import android.test.ApplicationTestCase;

import noteworthyengine.Mech;
import noteworthyengine.Unit;

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
    }
}
