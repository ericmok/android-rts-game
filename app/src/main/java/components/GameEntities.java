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
        troop.data.put(PositionComponent.class, positionComponent);

        troop.getComponentLabeler().getLabels().add(Entity.LOGIC_UNIT_DRAW);
        troop.getComponentLabeler().getLabels().add(Entity.LOGIC_SELECTION);

        troop.getTagLabeler().getLabels().add(owner);
        troop.getTagLabeler().getLabels().add(leader);
        troop.getTagLabeler().getLabels().add(Entity.TAG_TROOP_TYPE);

        return troop;
    }

    public static Entity buildCamera() {
        Entity camera = new Entity();
        CameraSettingsComponent csm = new CameraSettingsComponent();
        camera.data.put(CameraSettingsComponent.class, csm);

        camera.getComponentLabeler().getLabels().add(Entity.LOGIC_CAMERA);

        return camera;
    }
}
