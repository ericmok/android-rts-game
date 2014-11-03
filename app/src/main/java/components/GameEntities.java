package components;

/**
 * Created by eric on 10/30/14.
 */
public class GameEntities {

    /**
     * Construct an entity representing a troop
     * @param owner For example, Engine.TAG_PLAYER_OWNED
     * @param leader For example, Engine.TAG_LEADER
     * @return
     */
    public static Entity buildTroop(int owner, int leader) {
        Entity troop = new Entity();
        PositionComponent positionComponent = new PositionComponent();
        troop.cData.put(PositionComponent.class, positionComponent);

        SelectionComponent selectionComponent = new SelectionComponent();
        troop.cData.put(SelectionComponent.class, selectionComponent);
        troop.getLabels().add(Entity.LOGIC_SELECTION);

        DestinationComponent destinationComponent = new DestinationComponent();
        troop.cData.put(DestinationComponent.class, destinationComponent);
        troop.getLabels().add(Entity.LOGIC_DESTINATION_MOVEMENT);

        troop.getLabels().add(Entity.LOGIC_UNIT_DRAW);

        troop.getLabels().add(owner);
        troop.getLabels().add(leader);

        troop.getLabels().add(Entity.TAG_TROOP_TYPE);

        AbilityComponent ac = new AbilityComponent();
        ac.abilities.add(Abilities.SPECIAL_ATTACK);

        return troop;
    }

}
