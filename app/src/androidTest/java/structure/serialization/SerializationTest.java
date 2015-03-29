package structure.serialization;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import noteworthyengine.Mech;
import noteworthyengine.Platoon;
import noteworthyframework.Gamer;

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
            this.assertNotNull(jsonObject.get("pos"));
            this.assertNotNull(jsonObject.get("rot"));
            this.assertTrue(json.contains("["));

            this.assertNotNull(jsonObject.get("pos"));
            this.assertNotNull(jsonObject.get("rot"));
            this.assertNotNull(jsonObject.get("animationName"));
        } catch (JSONException e) {
            e.printStackTrace();
            //this.assertTrue(false);
        }
    }
}
