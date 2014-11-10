package model;

/**
 * Created by eric on 11/9/14.
 */
public class Troop extends Entity {

    public Troop() {
        this.labels().add(Entity.UNIT_TROOP);
        this.labels().add(Entity.BEHAVIOR_DRAWN_AS_TROOP);

        WorldComponent worldComponent = new WorldComponent();
        this.cData.put(WorldComponent.class, worldComponent);

        this.labels().add(Entity.BEHAVIOR_GETS_SELECTED);
        SelectionComponent selectionComponent = new SelectionComponent();
        this.cData.put(SelectionComponent.class, selectionComponent);

        this.labels().add(Entity.BEHAVIOR_MOVES_TOWARD_DESTINATION);
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
