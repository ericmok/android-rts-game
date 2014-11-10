package model;

/**
 * Created by eric on 11/9/14.
 */
public class Projectile extends Entity {

    public Projectile() {
        this.labels().add(Entity.UNIT_BASIC_PROJECTILE);

        WorldComponent worldComponent = new WorldComponent();
        this.cData.put(WorldComponent.class, worldComponent);

        this.labels().add(Entity.NODE_MOVE_TOWARD_DESTINATION);
        DestinationComponent destinationComponent = new DestinationComponent();
        this.cData.put(DestinationComponent.class, destinationComponent);
    }
}
