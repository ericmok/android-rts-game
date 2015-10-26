package art;

import android.graphics.Color;

import structure.TemporarySprite2dDef;

/**
 * Created by eric on 3/27/15.
 */
public class Animations {

    public static final String ANIMATION_SPLASH_SPLASH = "Animations/Splash/Splash";
    public static final String ANIMATION_SPLASH_LOADER = "Animations/Splash/Loader";

    public static final String ANIMATION_SMOKE_GUNPOWDER = "Animations/Smoke/Gunpowder";
    public static final SmokeGunpowderAnimation ANIMATION_SMOKE_GUNPOWDER_DEF = new SmokeGunpowderAnimation();

    public static final String ANIMATION_TROOPS_IDLING = "Animations/Troops/Idling";
    public static final String ANIMATION_TROOPS_MOVING = "Animations/Troops/Moving";
    public static final String ANIMATION_TROOPS_DYING = "Animations/Troops/Dying";
    public static final TroopsDyingAnimation ANIMATION_TROOPS_DYING_DEF = new TroopsDyingAnimation(0, 0);

    public static final String ANIMATION_TROOPS_SELECTED = "Animations/Troops/Selected";

    public static final String ANIMATION_TROOPS_TARGETED = "Animations/Troops/Targeted";

    public static final String ANIMATION_TROOPS_SWING = "Animations/Troops/Swing";
    public static final String ANIMATION_TROOPS_COOLDOWN = "Animations/Troops/Cooldown";

    public static final String ANIMATION_TROOPS_PROJECTILE = "Animations/Troops/Projectile";

    public static final String ANIMATION_MINE_IDLING = "Animations/Mine/Idling";
    public static final String ANIMATION_MINE_EXPLODING = "Animations/Mine/Exploding";

    public static final String ANIMATION_PROJECTILE_BASIC = "Animations/Projectiles/Basic";
    public static final String ANIMATION_PROJECTILE_EXPLOSION = "Animations/Projectiles/Explosion";
    public static final ProjectileExplosionAnimation ANIMATION_PROJECTILE_EXPLOSION_DEF = new ProjectileExplosionAnimation();

    public static final String ANIMATION_ZUG_IDLING = "Animations/Zug/Idling";

    public static final String ANIMATION_ENEMY_TROOPS_IDLING = "Animations/EnemyTroops/Idling";
    public static final String ANIMATION_ENEMY_TROOPS_MOVING = "Animations/EnemyTroops/Moving";

    public static final String ANIMATION_CAPITAL_SHIPS_IDLING = "Animations/CapitalShips/Idling";
    public static final String ANIMATION_CAPITAL_SHIPS_DYING = "Animations/CapitalShips/Dying";

    public static final String ANIMATION_TRIGGER_FIELDS_EXISTING = "Animations/TriggerFields/Existing";

    public static final String ANIMATION_RETICLE_TAP = "Animations/Reticle/Tap";

    public static final String ANIMATION_WAYPOINTS_MOVE = "Animations/Waypoints/Move";

    public static final String ANIMATION_BUTTONS_ACTIVATED = "Animations/Buttons/Activated";

    public static final String ANIMATION_BUTTONS_PLAY = "Animations/Buttons/Play";

    public static final String ANIMATION_BUTTONS_MOVE = "Animations/Buttons/Move";
    public static final String ANIMATION_BUTTONS_ATTACK = "Animations/Buttons/Attack";

    public static final String ANIMATION_BUTTONS_DEFEND = "Animations/Buttons/Defend";

    public static final String ANIMATIONS_WIN_DEFEAT_WIN = "Animations/WinDefeat/Win";
    public static final String ANIMATIONS_WIN_DEFEAT_DEFEAT = "Animations/WinDefeat/Defeat";
}
