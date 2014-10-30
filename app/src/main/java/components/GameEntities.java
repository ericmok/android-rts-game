package components;

import java.util.ArrayList;

/**
 * Created by eric on 10/30/14.
 */
public class GameEntities {
    public static Entity buildTroop() {
        Entity troop = new Entity();
        PositionComponent positionComponent = new PositionComponent();
        troop.data.put(PositionComponent.class, positionComponent);
        return troop;
    }
}
