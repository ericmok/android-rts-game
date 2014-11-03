package game.androidgame2;

import android.graphics.Color;

import java.util.ArrayList;

import components.ButtonComponent;
import components.Entity;

/**
 * Created by eric on 11/3/14.
 */
public class UIOverlay {

    public static final float PADDING = 1.1f;

    public ArrayList<Entity> buttons = new ArrayList<Entity>(8);

    public void draw(GameCamera gameCamera, RewriteOnlyArray<DrawList2DItem> sprites) {

        float initialY = -0.9f;
        float offset = 0;

        for (int i = 0; i < buttons.size(); i++) {
            Entity button = buttons.get(i);
            ButtonComponent bc = (ButtonComponent)button.cData.get(ButtonComponent.class);

            DrawList2DItem sprite = sprites.takeNextWritable();
            sprite.animationName = bc.texture;
            sprite.position.x = 0 + gameCamera.x;
            sprite.position.y = 0 + gameCamera.y + (initialY / (float)gameCamera.scale) + offset * i;
            sprite.width = (float)bc.size.x / (float)gameCamera.scale;
            sprite.height = (float)bc.size.y / (float)gameCamera.scale;
            sprite.animationProgress = 0;
            sprite.color = Color.WHITE;

            offset += PADDING * bc.size.y / (float)gameCamera.scale;
        }
    }
}
