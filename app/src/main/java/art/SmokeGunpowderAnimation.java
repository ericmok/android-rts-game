package art;

import android.graphics.Color;

import structure.TemporarySprite2dDef;

/**
 * Created by eric on 3/28/15.
 */
public class SmokeGunpowderAnimation extends TemporarySprite2dDef {

    public SmokeGunpowderAnimation() {
        super();
        this.animationName = Animations.ANIMATION_SMOKE_GUNPOWDER;
        this.progress.duration = 900;
        this.position.z = 1;
        this.width = 0.6f;
        this.height = 0.6f;
        this.angle = 0;
        this.color = Color.argb(50, 255, 255, 255);
    }
}
