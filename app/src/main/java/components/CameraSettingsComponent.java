package components;

import android.opengl.Matrix;

/**
 * Created by eric on 10/31/14.
 */
public class CameraSettingsComponent extends Component {
    public float[] transform = new float[16];

    public CameraSettingsComponent() {
        reset();
    }

    public void reset() {
        Matrix.setIdentityM(transform, 0);
    }

    public void set(float posX, float posY, float zoom) {
        reset();
        Matrix.translateM(transform, 0, -posX, -posY, 0);
        Matrix.scaleM(transform, 0, zoom, zoom, 1);
    }
}
