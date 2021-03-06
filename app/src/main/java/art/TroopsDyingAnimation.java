package art;

import android.graphics.Color;

import structure.TemporarySprite2dDef;

/**
 * Created by eric on 3/28/15.
 */
public class TroopsDyingAnimation extends TemporarySprite2dDef {
    public TroopsDyingAnimation(float x, float y) {
        super();
        this.animationName = Animations.ANIMATION_TROOPS_DYING;
        this.animationProgress = 0;
        this.width = 0.9f;
        this.height = 0.9f;
        this.angle = 90f;
        this.color = Color.WHITE;
        this.cameraIndex = 0;
        this.progress.set(0, 1000, false);

        this.position.set(x, y, 0);
    }
}
