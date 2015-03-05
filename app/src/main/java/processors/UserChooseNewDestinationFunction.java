package processors;

import java.util.ArrayList;

import behaviors.DestinationComponent;
import structure.GameCamera;
import structure.GameInput;

import model.Entity;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 11/3/14.
 */
public class UserChooseNewDestinationFunction {

    private static Vector2 temp = new Vector2();

    /**
     * For each unit, the destination point is set the touch position in world coordinates
     * @param unitsToChange
     * @param gameCamera
     * @param gameInput
     */
    public static void apply(ArrayList<Entity> unitsToChange, GameCamera gameCamera, GameInput gameInput, VoidFunc<Entity> fn) {

        for (int i = 0; i < unitsToChange.size(); i++) {
            Entity entity = unitsToChange.get(i);
            DestinationComponent dc = (DestinationComponent) entity.cData.get(DestinationComponent.class);

            gameCamera.getScreenToWorldCoords(temp, gameInput.touchPosition);

            dc.dest.copy(temp);
            dc.hasDestination = true;

            fn.apply(entity);
        }
    }

}
