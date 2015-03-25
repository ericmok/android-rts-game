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

    public static final String ANIMATION_SPLASH_SPLASH = "Animations/Splash/Splash";
    public static final String ANIMATION_SPLASH_LOADER = "Animations/Splash/Loader";

    public static final String ANIMATION_SMOKE_GUNPOWDER = "Animations/Smoke/Gunpowder";
	public static final String ANIMATION_TROOPS_IDLING = "Animations/Troops/Idling";
	public static final String ANIMATION_TROOPS_MOVING = "Animations/Troops/Moving";
	public static final String ANIMATION_TROOPS_DYING = "Animations/Troops/Dying";
	public static final String ANIMATION_TROOPS_SELECTED = "Animations/Troops/Selected";

    public static final String ANIMATION_TROOPS_TARGETED = "Animations/Troops/Targeted";

    public static final String ANIMATION_TROOPS_SWING = "Animations/Troops/Swing";
    public static final String ANIMATION_TROOPS_COOLDOWN = "Animations/Troops/Cooldown";

    public static final String ANIMATION_TROOPS_PROJECTILE = "Animations/Troops/Projectile";

    public static final String ANIMATION_MINE_IDLING = "Animations/Mine/Idling";
    public static final String ANIMATION_MINE_EXPLODING = "Animations/Mine/Exploding";

    public static final String ANIMATION_PROJECTILE_BASIC = "Animations/Projectiles/Basic";
    public static final String ANIMATION_PROJECTILE_EXPLOSION = "Animations/Projectiles/Explosion";

	public static final String ANIMATION_ENEMY_TROOPS_IDLING = "Animations/EnemyTroops/Idling";
	public static final String ANIMATION_ENEMY_TROOPS_MOVING = "Animations/EnemyTroops/Moving";
	
	public static final String ANIMATION_CAPITAL_SHIPS_IDLING = "Animations/CapitalShips/Idling";
	public static final String ANIMATION_CAPITAL_SHIPS_DYING = "Animations/CapitalShips/Dying";
	
	public static final String ANIMATION_TRIGGER_FIELDS_EXISTING = "Animations/TriggerFields/Existing";
	
	public static final String ANIMATION_RETICLE_TAP = "Animations/Reticle/Tap";
	
	public static final String ANIMATION_WAYPOINTS_MOVE = "Animations/Waypoints/Move";

    public static final String ANIMATION_BUTTONS_PLAY = "Animations/Buttons/Play";

	public static final String ANIMATION_BUTTONS_MOVE = "Animations/Buttons/Move";
	public static final String ANIMATION_BUTTONS_ATTACK = "Animations/Buttons/Attack";

    public static final String ANIMATION_BUTTONS_DEFEND = "Animations/Buttons/Defend";

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
	public String animationName = ANIMATION_TROOPS_IDLING; 
	public int animationProgress = 0;
	
	public Sprite2dDef() {
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
    }

    @Override
    public int compareTo(Sprite2dDef o) {
        return this.animationName.compareTo(o.animationName);
    }
}
