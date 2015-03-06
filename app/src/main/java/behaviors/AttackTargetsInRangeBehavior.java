package behaviors;

import model.Entity;

/**
 * Created by eric on 3/5/15.
 * Mixin Space for AttackTargetInRangeProcess
 */
public class AttackTargetsInRangeBehavior extends Behavior {

    public static final Class[] COMPONENTS = {
        WorldComponent.class,
        MeleeAttackComponent.class,
        LivingComponent.class
    };

    @Override
    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static void mixin(Entity entity) {
        entity.labels().add(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);
        entity.labels().add(Behaviors.BEHAVIOR_ATTACKS_TARGET_IN_RANGE);

        AttackTargetsInRangeBehavior attackTargetsInRangeBehavior = new AttackTargetsInRangeBehavior();
        attackTargetsInRangeBehavior.ensureRequirements(entity);
    }
}
