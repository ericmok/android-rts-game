package model;

import utils.VoidFunc;

/**
 * Created by eric on 11/9/14.
 */
public class Projectile extends Entity {


    public static final VoidFunc<Entity> DIE_ON_DESTINATION_REACHED = new VoidFunc<Entity>() {
        @Override
        public void apply(Entity element) {
            LivingComponent lc = (LivingComponent)element.cData.get(LivingComponent.class);
            lc.takeDamage(100);
        }
    };


    public Projectile() {
        this.labels().add(Behaviors.UNIT_BASIC_PROJECTILE);

        WorldComponent worldComponent = new WorldComponent();
        this.cData.put(WorldComponent.class, worldComponent);

        VelocityComponent vc = new VelocityComponent();
        this.cData.put(VelocityComponent.class, vc);
        vc.moveSpeed = 2;

        LivingComponent lc = new LivingComponent();
        this.cData.put(LivingComponent.class, lc);

        this.labels().add(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);
        DestinationComponent destinationComponent = new DestinationComponent();
        this.cData.put(DestinationComponent.class, destinationComponent);
        destinationComponent.onDestinationReached = Projectile.DIE_ON_DESTINATION_REACHED;

        this.labels().add(Behaviors.BEHAVIOR_DIES_ON_NO_HP);
    }
}
