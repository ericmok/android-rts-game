package structure;

import android.opengl.Matrix;

import noteworthyengine.GameSettings;
import utils.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public abstract class GameCamera {

    private int cameraIndex = 0;

    public float[] viewMatrix = new float[16];
    public float[] projectionMatrix = new float[16];

    private float[] viewProjectionMatrix = new float[16];

    public GameCamera(int index) {
        this.cameraIndex = index;

        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);
        //Matrix.scaleM(cameraMatrix, 0, 0.07f, 0.07f, 1);
        Matrix.translateM(viewMatrix, 0, 0, 0, -2);
        Matrix.setIdentityM(viewProjectionMatrix, 0);
    }

    /**
     * TODO: Delegate multiplication to shaders?
     * Invalidates the cached viewProjectionMatrix
     */
    public void invalidate() {
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    public int getCameraIndex() {
        return cameraIndex;
    }

    public void setCameraIndex(int index) {
        this.cameraIndex = index;
    }

    public abstract void configure(float width, float height);

    public abstract void unProject(Vector2 touchCoords);

    public float[] getViewProjectionMatrix() {
        return viewProjectionMatrix;
    }
}
