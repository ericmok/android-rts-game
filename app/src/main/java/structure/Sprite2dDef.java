package structure;

import android.graphics.Color;

import utils.Vector2;
import utils.Vector3;

/**
 * An item to be renderable by the SimpleShaderBatch draw2d function <br/>
 * <strong>Pending Refactor</strong>: Should the graphics or the game loop be responsible for deciding the texture and sprites? <br/>
 * If the physics takes too long, the graphics thread should handle it. But if draw items require splitting into multiple sprites then what?<br/>
 * A problem: The TextureLoader belongs in the graphics, but we don't have access to it in game logic. So we hardcode some animation strings.
 */
public class Sprite2dDef implements Comparable<Sprite2dDef> {

    /// Old position is used to interpolate positions on the gfx thread
    public Vector3 oldPosition = new Vector3();

    /// New position, update this using update function instead, if you want interpolation
    public Vector3 position = new Vector3();

    /// If turned on, the oldPosition and position will interpolate, incurs extra function calls
    public boolean isGfxInterpolated = false;

    /// This value should interpolate oldPosition and position in the gfx
    /// thread if isGfxInterpolated is true
    public Vector3 gfxInterpolation = new Vector3();

    public float width = 1.0f;
    public float height = 1.0f;

    public float angle = 0.0f;

    public int color = Color.WHITE;

    public int cameraIndex = 0;

	/**
	 * Use string constants for GC friendliness. Denotes the animation to be played
	 * in terms of the location of the assets for the animation
	 */
    public String animationName = null;
    public int animationProgress = 0;
	
	public Sprite2dDef() {
	}

    public void set(String animationName, int animationProgress,
                    float x, float y, float z,
                    float width, float height,
                    float angle,
                    int color,
                    int cameraIndex) {
        this.animationName = animationName;
        this.animationProgress = animationProgress;
        this.isGfxInterpolated = false;
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.color = color;
        this.cameraIndex = cameraIndex;
    }

    public void setInterpolationOn(float oldX, float oldY, float oldZ) {
        this.isGfxInterpolated = true;
        this.oldPosition.x = oldX;
        this.oldPosition.y = oldY;
        this.oldPosition.z = oldZ;
    }

    public void reset() {
        oldPosition.zero();
        position.zero();
        isGfxInterpolated = false;

        width = 1.0f;
        height = 1.0f;
        angle = 0;
        color = Color.WHITE;
        cameraIndex = 0;
    }

    /**
     * Mutates gfxInterpolation vector
     * @param value Value between 0 and 1
     */
    public Vector3 calculateGfxInterpolation(double value) {
        gfxInterpolation.x = value * (position.x- oldPosition.x) + oldPosition.x;
        gfxInterpolation.y = value * (position.y - oldPosition.y) + oldPosition.y;
        gfxInterpolation.z = position.z; // Don't interp this
        return gfxInterpolation;
    }

    public void copy(Sprite2dDef other) {
        this.position.copy(other.position);
        this.width = other.width;
        this.height = other.height;
        this.angle = other.angle;
        this.color = other.color;

        this.animationName = other.animationName;
        this.animationProgress = other.animationProgress;

        this.gfxInterpolation.copy(other.gfxInterpolation);
        this.isGfxInterpolated = other.isGfxInterpolated;
        this.oldPosition.copy(other.oldPosition);

        this.cameraIndex = other.cameraIndex;
    }

    @Override
    public int compareTo(Sprite2dDef o) {
        return this.animationName.compareTo(o.animationName);
    }
}
