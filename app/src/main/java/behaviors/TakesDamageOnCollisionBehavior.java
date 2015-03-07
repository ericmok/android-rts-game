package behaviors;

import model.Entity;

/**
 * Created by eric on 3/6/15.
 */
public class TakesDamageOnCollisionBehavior extends Behavior {

    RadiusComponent radiusComponent = null;

    public static final Class[] COMPONENTS = {
        RadiusComponent.class
    };

    @Override
    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static void mixin(Entity entity) {
        entity.labels().add(Behaviors.BEHAVIOR_TAKES_DAMAGE_ON_COLLISION);

        TakesDamageOnCollisionBehavior behaviorToAdd = new TakesDamageOnCollisionBehavior();

        behaviorToAdd.ensureRequirements(entity);
        behaviorToAdd.radiusComponent =
                (RadiusComponent)entity.cData.get(RadiusComponent.class);
    }
}
