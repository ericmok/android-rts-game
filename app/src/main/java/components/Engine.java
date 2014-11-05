package components;

/**
 * Created by eric on 10/30/14.
 */
public class Engine {

    /**
     * Entities organized by logic-y stuff
     */
    public DenormalizedDataSet<Entity> entityDenormalizer;

    public double gameTime = 0;

    public Engine() {
        entityDenormalizer = new DenormalizedDataSet<Entity>(32, 300);
    }

    public void addEntity(Entity entity) {
        entityDenormalizer.addDenormalizable(entity);
    }

    public void removeEntity(Entity entity) {
        entityDenormalizer.removeDenormalizable(entity);
    }
}
