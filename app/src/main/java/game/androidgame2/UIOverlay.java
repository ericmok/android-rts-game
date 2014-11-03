package game.androidgame2;

import android.graphics.Color;

import java.util.ArrayList;

import components.ButtonComponent;
import components.Entity;

/**
 * Created by eric on 11/3/14.
 */
public class UIOverlay {

    public static final float FRACTIONAL_PADDING = 0.1f;
    public static final float PADDING = 1.0f + FRACTIONAL_PADDING;
    public static final float SIZE_NORMALIZER = 0.5f;

    public ArrayList<Entity> buttons = new ArrayList<Entity>(8);

    public void draw(GameCamera gameCamera, RewriteOnlyArray<DrawList2DItem> sprites) {

        float offset = 0;

        for (int i = 0; i < buttons.size(); i++) {
            Entity button = buttons.get(i);
            ButtonComponent bc = (ButtonComponent)button.cData.get(ButtonComponent.class);

            float initialX = -(float)gameCamera.aspectRatio + ((float)bc.size.x/2);
            float initialY = -1 + ((float)bc.size.y/2);

            DrawList2DItem sprite = sprites.takeNextWritable();
            sprite.animationName = bc.texture;
            sprite.position.x = 0 + gameCamera.x + (initialX / (float)gameCamera.scale);
            sprite.position.y = 0 + gameCamera.y + (initialY / (float)gameCamera.scale) + offset * i;
            sprite.width = SIZE_NORMALIZER * (float)bc.size.x / (float)gameCamera.scale;
            sprite.height = SIZE_NORMALIZER * (float)bc.size.y / (float)gameCamera.scale;
            sprite.animationProgress = 0;
            sprite.color = Color.WHITE;

            offset += SIZE_NORMALIZER * PADDING * bc.size.y / (float)gameCamera.scale;
        }
    }
}
