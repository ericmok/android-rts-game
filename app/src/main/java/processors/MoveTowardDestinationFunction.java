package processors;

import java.util.ArrayList;

import model.DestinationComponent;
import model.Entity;
import model.WorldComponent;
import model.SelectionComponent;
import utils.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public class MoveTowardDestinationFunction {

    private static Vector2 temp = new Vector2();

    public static void apply(ArrayList<Entity> entitiesThatNeedMoving, double dt) {

        for (int i = 0; i < entitiesThatNeedMoving.size(); i++) {

            Entity entity = entitiesThatNeedMoving.get(i);
            DestinationComponent dc = (DestinationComponent)entity.cData.get(DestinationComponent.class);

            if (!dc.hasDestination) {
                continue;
            }

            WorldComponent wc = (WorldComponent)entity.cData.get(WorldComponent.class);
            SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            Vector2.subtract(temp, dc.dest, wc.pos);

            // Reached destination
            if (temp.magnitude() < 0.01) {
                dc.hasDestination = false;
            }

            temp.setNormalized();
            temp.scale(dt, dt);

            wc.pos.translate(temp.x, temp.y);
            wc.rot.setDirection(temp);
        }
    }
}
