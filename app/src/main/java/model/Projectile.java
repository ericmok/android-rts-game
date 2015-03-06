package model;

import behaviors.Behaviors;
import behaviors.CastingProjectileCooldownBehavior;
import behaviors.DestinationComponent;
import behaviors.LivingComponent;
import behaviors.MoveTowardsDestinationBehavior;
import behaviors.VelocityComponent;
import behaviors.WorldComponent;
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

        MoveTowardsDestinationBehavior.mixin(this);
        CastingProjectileCooldownBehavior.mixin(this);

        DestinationComponent destinationComponent = (DestinationComponent)this.cData.get(DestinationComponent.class);
        destinationComponent.onDestinationReached = Projectile.DIE_ON_DESTINATION_REACHED;

        this.labels().add(Behaviors.BEHAVIOR_DIES_ON_NO_HP);

        reset();
    }

    public void reset() {
        WorldComponent wc = (WorldComponent)cData.get(WorldComponent.class);
        wc.pos.zero();
        wc.rot.setDegrees(0);

        VelocityComponent vc = (VelocityComponent)cData.get(VelocityComponent.class);
        vc.moveSpeed = 2;

        LivingComponent lc = (LivingComponent)cData.get(LivingComponent.class);
        lc.hitPoints = 1;

        DestinationComponent dc = (DestinationComponent)cData.get(DestinationComponent.class);
        dc.hasDestination = false;
    }
}
