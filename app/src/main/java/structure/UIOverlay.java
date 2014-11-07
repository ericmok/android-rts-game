package structure;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

import model.ButtonComponent;
import model.Entity;
import utils.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public class UIOverlay {

    public static final float FRACTIONAL_PADDING = 0.1f;
    public static final float PADDING = 1.0f + FRACTIONAL_PADDING;
    public static final float SIZE_NORMALIZER = 0.5f;

    public ArrayList<Entity> buttons = new ArrayList<Entity>(8);

    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();
    private Vector2 temp3 = new Vector2();

    public void processInput(GameCamera gameCamera,
                             int currentGesture, GameInput gameInput) {

        if (currentGesture == GameInput.GESTURE_ON_SINGLE_TAP_UP) {
            for (int i = 0; i < buttons.size(); i++) {
                ButtonComponent bc = (ButtonComponent)buttons.get(i).cData.get(ButtonComponent.class);

                Vector2 touchToWorldCoords = temp;
                gameCamera.getTouchToWorldCords(touchToWorldCoords, gameInput.touchPosition);

                //Log.i("BUTTON", "" + "," + bc.name + " " + bc.touchPoint + " touchPosition[" + temp + "]");
                //Log.i("BUTTON", "dist " + bc.touchPoint.distanceTo(temp) + " again: " + temp.distanceTo(bc.touchPoint));

                // The button's trigger box is bigger as the camera zooms out
                if (Math.abs(touchToWorldCoords.x - bc.touchPoint.x) < 0.5 * SIZE_NORMALIZER * bc.size.x / gameCamera.scale &&
                        Math.abs(touchToWorldCoords.y - bc.touchPoint.y) < 0.5 * SIZE_NORMALIZER * bc.size.y / gameCamera.scale) {
                    Log.i("BUTTON", "ACTIVATE " + "," + bc.name + "," + bc.touchPoint + " touchPosition[" + gameInput.touchPosition + "]");
                }
            }
        }
    }

    public void draw(GameCamera gameCamera, RewriteOnlyArray<DrawList2DItem> sprites) {
        float offset = 0;

        for (int i = 0; i < buttons.size(); i++) {
            Entity button = buttons.get(i);
            ButtonComponent bc = (ButtonComponent)button.cData.get(ButtonComponent.class);
            bc.position = i;

            Vector2 buttonInWorldCoords = temp;
            Vector2 buttonInScreenCoords = temp2;
            Vector2 buttonWorldSize = temp3;

            // Calculate the origin for the button. The origin should be on lower left
            float screenOriginX = -(float)gameCamera.aspectRatio + ((float)bc.size.x/2);
            float screenOriginY = -1 + ((float)bc.size.y/2);

            buttonInScreenCoords.set(screenOriginX, screenOriginY);

            gameCamera.getTouchToWorldCords(buttonInWorldCoords, buttonInScreenCoords);

            buttonWorldSize.set(bc.size.x * SIZE_NORMALIZER / gameCamera.scale,
                                bc.size.y * SIZE_NORMALIZER / gameCamera.scale);

            bc.touchPoint.x = buttonInWorldCoords.x;
            bc.touchPoint.y = buttonInWorldCoords.y + offset * i;

            DrawList2DItem sprite = sprites.takeNextWritable();
            sprite.animationName = bc.texture;
            sprite.position.x = buttonInWorldCoords.x;
            sprite.position.y = buttonInWorldCoords.y + offset * i;
            sprite.width = (float)buttonWorldSize.x;
            sprite.height = (float)buttonWorldSize.y;
            sprite.animationProgress = 0;
            sprite.color = Color.WHITE;

            offset += SIZE_NORMALIZER * PADDING * bc.size.y / (float)gameCamera.scale;
        }
    }
}
