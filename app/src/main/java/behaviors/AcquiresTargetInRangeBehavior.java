package behaviors;

import model.Entity;

/**
 * Created by eric on 3/5/15.
 */
public class AcquiresTargetInRangeBehavior extends Behavior {

    WorldComponent worldComponent = null;
    VelocityComponent velocityComponent = null;
    MeleeAttackComponent meleeAttackComponent = null;

    public static final Class[] COMPONENTS = {
        WorldComponent.class,
        VelocityComponent.class,
        MeleeAttackComponent.class // Todo: Attack range
    };

    @Override
    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static void mixin(Entity entity) {
        entity.labels().add(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);

        AcquiresTargetInRangeBehavior behaviorToAdd = new AcquiresTargetInRangeBehavior();
        behaviorToAdd.ensureRequirements(entity);

        behaviorToAdd.worldComponent =
                (WorldComponent)entity.cData.get(WorldComponent.class);
        behaviorToAdd.velocityComponent =
                (VelocityComponent)entity.cData.get(VelocityComponent.class);
        behaviorToAdd.meleeAttackComponent =
                (MeleeAttackComponent)entity.cData.get(MeleeAttackComponent.class);
    }
}
