package behaviors;

import model.Entity;

/**
 * Created by eric on 3/6/15.
 */
public class GetsSelectedBehavior extends Behavior {

    SelectionComponent selectionComponent = null;

    public static final Class[] COMPONENTS = {
        SelectionComponent.class
    };

    @Override
    public Class[] getComponents() {
        return COMPONENTS;
    }

    public static final void mixin(Entity entity) {
        entity.labels().add(Behaviors.BEHAVIOR_GETS_SELECTED);

        GetsSelectedBehavior behaviorToAdd= new GetsSelectedBehavior();
        behaviorToAdd.ensureRequirements(entity);

        behaviorToAdd.selectionComponent =
                (SelectionComponent)entity.cData.get(SelectionComponent.class);
    }
}
