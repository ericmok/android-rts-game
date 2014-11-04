package processors;

import java.util.ArrayList;

import components.DestinationComponent;
import components.Entity;
import components.PositionComponent;
import components.SelectionComponent;
import game.androidgame2.GameSettings;
import game.androidgame2.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public class MoveTowardDestinationFunction {

    private static Vector2 temp = new Vector2();

    public static void apply(ArrayList<Entity> entitiesThatNeedMoving, long elapsedTime) {

        for (int i = 0; i < entitiesThatNeedMoving.size(); i++) {

            Entity entity = entitiesThatNeedMoving.get(i);
            DestinationComponent dc = (DestinationComponent)entity.cData.get(DestinationComponent.class);

            if (!dc.hasDestination) {
                continue;
            }

            PositionComponent pc = (PositionComponent)entity.cData.get(PositionComponent.class);
            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            Vector2.subtract(temp, dc.dest, pc.pos);

            // Reached destination
            if (temp.magnitude() < 0.01) {
                dc.hasDestination = false;
            }

            temp.setNormalized();
            temp.scale(GameSettings.UNIT_TIME_MULTIPLIER * elapsedTime, GameSettings.UNIT_TIME_MULTIPLIER * elapsedTime);

            pc.pos.translate(temp.x, temp.y);
        }
    }
}
