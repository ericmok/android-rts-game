package model;

/**
 * Created by eric on 11/9/14.
 */
public class Projectile extends Entity {

    public Projectile() {
        this.labels().add(Behaviors.UNIT_BASIC_PROJECTILE);

        WorldComponent worldComponent = new WorldComponent();
        this.cData.put(WorldComponent.class, worldComponent);

        VelocityComponent vc = new VelocityComponent();
        this.cData.put(VelocityComponent.class, vc);

        this.labels().add(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);
        DestinationComponent destinationComponent = new DestinationComponent();
        this.cData.put(DestinationComponent.class, destinationComponent);
    }
}
