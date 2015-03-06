package behaviors;

import model.Entity;

/**
 * Created by eric on 3/6/15.
 * Mixin for DieOnNoHpProcess
 */
public class DiesBehavior extends Behavior {

    LivingComponent livingComponent = null;

    public static final Class[] COMPONENTS = {
        LivingComponent.class
    };

    @Override
    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static final void mixin(Entity entity) {

        DiesBehavior diesBehavior = new DiesBehavior();
        diesBehavior.ensureRequirements(entity);

        entity.labels().add(Behaviors.BEHAVIOR_DIES_ON_NO_HP);

        diesBehavior.livingComponent =
                (LivingComponent)entity.cData.get(LivingComponent.class);
    }
}
