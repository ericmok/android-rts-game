package components;

import android.opengl.Matrix;

/**
 * Created by eric on 10/31/14.
 */
public class CameraSettingsComponent extends Component {
    public float x;
    public float y;

    public float scale;

    public CameraSettingsComponent() {
        reset();
    }

    public void reset() {
        x = 0;
        y = 0;
        scale = 1;
    }

    public void set(float posX, float posY, float zoom) {
        x = posX;
        y = posY;
        scale = zoom;
    }
}
