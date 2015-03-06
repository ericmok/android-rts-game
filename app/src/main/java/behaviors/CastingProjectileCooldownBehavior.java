package behaviors;

import model.Entity;

/**
 * Created by eric on 3/5/15.
 */
public class CastingProjectileCooldownBehavior extends Behavior {

    ProjectileCasterComponent projectileCasterComponent = null;
    WorldComponent worldComponent = null;
    LivingComponent livingComponent = null;
    VelocityComponent velocityComponent = null;
    DestinationComponent destinationComponent = null;

    public static final Class[] COMPONENTS = {
        ProjectileCasterComponent.class,
        WorldComponent.class,
        VelocityComponent.class,
        DestinationComponent.class,
        LivingComponent.class
    };

    @Override
    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static void mixin(Entity entity) {

        // TODO: Instead of putting this in labels, put this in a behaviors list
        entity.labels().add(Behaviors.BEHAVIOR_CASTS_PROJECTILE);

        // TODO: Add to entity behaviors
        CastingProjectileCooldownBehavior behaviorToAdd =
                new CastingProjectileCooldownBehavior();

        behaviorToAdd.ensureRequirements(entity);

        behaviorToAdd.projectileCasterComponent =
                (ProjectileCasterComponent)entity.cData.get(ProjectileCasterComponent.class);
        behaviorToAdd.worldComponent =
                (WorldComponent)entity.cData.get(WorldComponent.class);
        behaviorToAdd.velocityComponent =
                (VelocityComponent)entity.cData.get(VelocityComponent.class);
        behaviorToAdd.destinationComponent =
                (DestinationComponent)entity.cData.get(DestinationComponent.class);
        behaviorToAdd.livingComponent =
                (LivingComponent)entity.cData.get(LivingComponent.class);
    }
}
