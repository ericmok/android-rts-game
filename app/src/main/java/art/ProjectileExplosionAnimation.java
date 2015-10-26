package art;

import android.graphics.Color;

import structure.TemporarySprite2dDef;

/**
 * Created by eric on 10/17/15.
 */
public class ProjectileExplosionAnimation extends TemporarySprite2dDef {
    public ProjectileExplosionAnimation() {
        super();

        this.animationName = Animations.ANIMATION_PROJECTILE_EXPLOSION;
        this.animationProgress = 0;
        this.width = 0.6f;
        this.height = 0.6f;
        this.angle = 90f;
        this.color = Color.argb(128, 255, 255, 255);
        this.cameraIndex = 0;
        this.progress.set(0, 1200, false);
    }
}
