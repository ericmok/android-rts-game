package behaviors;

import model.Entity;

/**
 * Created by eric on 3/5/15.
 * Mixin Space for MoveTowardsDestinationFunction
 *
 * Components: [world, velocity, destination]
 */
public class MoveTowardsDestinationBehavior extends Behavior {

    public WorldComponent worldComponent = null;
    public VelocityComponent velocityComponent = null;
    public DestinationComponent destinationComponent = null;

    // TODO: Consider using string keys for entity internal variable lookup
    public static final Class[] COMPONENTS = {
      WorldComponent.class, VelocityComponent.class, DestinationComponent.class
    };

    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static void mixin(Entity entity) {

        // TODO: Instead of putting this in labels, put this in a behaviors list
        entity.labels().add(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);

        MoveTowardsDestinationBehavior behaviorToAdd =
                new MoveTowardsDestinationBehavior();
        //entity.cData.put(MoveTowardsDestinationBehavior.class, behaviorToAdd);

        behaviorToAdd.ensureRequirements(entity);

        behaviorToAdd.worldComponent =
                (WorldComponent)entity.cData.get(WorldComponent.class);
        behaviorToAdd.velocityComponent =
                (VelocityComponent)entity.cData.get(VelocityComponent.class);
        behaviorToAdd.destinationComponent =
                (DestinationComponent)entity.cData.get(DestinationComponent.class);

    }

    // For isolatable mixins, we should have used strings instead of component classes
    // since the entities component store uses component classes for the keys
    //public static void mixin(Entity entity,
    //                         WorldComponent wc,
    //                         VelocityComponent vc,
    //                         DestinationComponent dc) {
    //}
}
