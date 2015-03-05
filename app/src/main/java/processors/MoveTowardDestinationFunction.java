package processors;

import java.util.ArrayList;

import behaviors.DestinationComponent;
import model.Entity;
import behaviors.VelocityComponent;
import behaviors.WorldComponent;
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
            VelocityComponent vc = (VelocityComponent)entity.cData.get(VelocityComponent.class);
            //SelectionComponent sc = (SelectionComponent)entity.cData.get(SelectionComponent.class);

            Vector2.subtract(vc.velocity, dc.dest, wc.pos);

            // Reached destination
            if (vc.velocity.magnitude() < 0.01) {
                dc.hasDestination = false;
                dc.onDestinationReached.apply(entity);
            }

            vc.velocity.setNormalized();
            vc.velocity.scale(vc.moveSpeed * dt, vc.moveSpeed * dt);

            wc.pos.translate(vc.velocity.x, vc.velocity.y);
            wc.rot.setDirection(vc.velocity);
        }
    }
}
