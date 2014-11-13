package model;

/**
 * Created by eric on 11/9/14.
 */
public class Troop extends Entity {

    public Troop() {
        this.labels().add(Behaviors.UNIT_TROOP);
        this.labels().add(Behaviors.BEHAVIOR_DRAWN_AS_TROOP);

        WorldComponent worldComponent = new WorldComponent();
        this.cData.put(WorldComponent.class, worldComponent);

        VelocityComponent vc = new VelocityComponent();
        this.cData.put(VelocityComponent.class, vc);

        RadiusComponent radiusComponent = new RadiusComponent();
        this.cData.put(RadiusComponent.class, radiusComponent);

        this.labels().add(Behaviors.BEHAVIOR_AQUIRES_TARGET_IN_RANGE);
        this.labels().add(Behaviors.BEHAVIOR_TAKES_DAMAGE_ON_COLLISION);
        this.cData.put(MeleeAttackComponent.class, new MeleeAttackComponent());

        this.labels().add(Behaviors.BEHAVIOR_GETS_SELECTED);
        SelectionComponent selectionComponent = new SelectionComponent();
        this.cData.put(SelectionComponent.class, selectionComponent);

        this.labels().add(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);
        DestinationComponent destinationComponent = new DestinationComponent();
        this.cData.put(DestinationComponent.class, destinationComponent);

        AbilityComponent abilityComponent = new AbilityComponent();
        abilityComponent.abilities.add(Abilities.SPECIAL_ATTACK);
        this.cData.put(AbilityComponent.class, abilityComponent);

        LivingComponent livingComponent = new LivingComponent();
        livingComponent.hitPoints = 5;
        this.cData.put(LivingComponent.class, livingComponent);
    }
}
