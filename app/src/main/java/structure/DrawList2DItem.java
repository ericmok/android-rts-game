package structure;

import android.graphics.Color;

import utils.Vector3;

/**
 * An item to be renderable by the SimpleShaderBatch draw2d function <br/>
 * <strong>Pending Refactor</strong>: Should the graphics or the game loop be responsible for deciding the texture and sprites? <br/>
 * If the physics takes too long, the graphics thread should handle it. But if draw items require splitting into multiple sprites then what?<br/>
 * A problem: The TextureLoader belongs in the graphics, but we don't have access to it in game logic. So we hardcode some animation strings.
 */
public class DrawList2DItem {

	public static final String ANIMATION_TROOPS_IDLING = "Animations/Troops/Idling";
	public static final String ANIMATION_TROOPS_MOVING = "Animations/Troops/Moving";
	public static final String ANIMATION_TROOPS_DYING = "Animations/Troops/Dying";
	public static final String ANIMATION_TROOPS_SELECTED = "Animations/Troops/Selected";

    public static final String ANIMATION_TROOPS_TARGETED = "Animations/Troops/Targeted";

    public static final String ANIMATION_TROOPS_SWING = "Animations/Troops/Swing";
    public static final String ANIMATION_TROOPS_COOLDOWN = "Animations/Troops/Cooldown";

    public static final String ANIMATION_TROOPS_PROJECTILE = "Animations/Troops/Projectile";

	public static final String ANIMATION_ENEMY_TROOPS_IDLING = "Animations/EnemyTroops/Idling";
	public static final String ANIMATION_ENEMY_TROOPS_MOVING = "Animations/EnemyTroops/Moving";
	
	public static final String ANIMATION_CAPITAL_SHIPS_IDLING = "Animations/CapitalShips/Idling";
	public static final String ANIMATION_CAPITAL_SHIPS_DYING = "Animations/CapitalShips/Dying";
	
	public static final String ANIMATION_TRIGGER_FIELDS_EXISTING = "Animations/TriggerFields/Existing";
	
	public static final String ANIMATION_RETICLE_TAP = "Animations/Reticle/Tap";
	
	public static final String ANIMATION_WAYPOINTS_MOVE = "Animations/Waypoints/Move";

	public static final String ANIMATION_BUTTONS_MOVE = "Animations/Buttons/Move";
	public static final String ANIMATION_BUTTONS_ATTACK = "Animations/Buttons/Attack";

    public static final String ANIMATION_BUTTONS_DEFEND = "Animations/Buttons/Defend";
	
	public Vector3 position = new Vector3();
	
	public float width = 1.0f;
	public float height = 1.0f;
	
	public float angle = 0.0f;

	public int color = Color.WHITE;
	
	/**
	 * Use string constants for GC friendliness. Denotes the animation to be played
	 * in terms of the location of the assets for the animation
	 */
	public String animationName = ANIMATION_TROOPS_IDLING; 
	public int animationProgress = 0;
	
	public DrawList2DItem() {
	}
}
