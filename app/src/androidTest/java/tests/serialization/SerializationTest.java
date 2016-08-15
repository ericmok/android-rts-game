package tests.serialization;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import noteworthyengine.units.Mech;

/**
 * Created by eric on 3/27/15.
 */
public class SerializationTest extends ApplicationTestCase<Application> {
    public SerializationTest() {
        super(Application.class);
    }

    public void testSerialization() {
        Mech mech = new Mech();
        String json = mech.json();

        try {
            JSONObject jsonObject = new JSONObject(json);
            assertNotNull(jsonObject.get("pos"));
            assertNotNull(jsonObject.get("rot"));
            assertTrue(json.contains("["));

            assertNotNull(jsonObject.get("pos"));
            assertNotNull(jsonObject.get("rot"));
            assertNotNull(jsonObject.get("animationName"));
        } catch (JSONException e) {
            e.printStackTrace();
            //this.assertTrue(false);
        }
    }
}
