package components;

/**
 * Created by eric on 10/30/14.
 */
public class Engine {

    /**
     * Entities organized by logic-y stuff
     */
    public DenormalizedDataSet componentsDenormalizer;

    /**
     * Entities organized by metadata and category-ish stuff, like troops, tanks, player type
     */
    public DenormalizedDataSet tagsDenormalizer;

    public Engine() {
        componentsDenormalizer = new DenormalizedDataSet(16, 300);
        tagsDenormalizer = new DenormalizedDataSet(16, 300);
    }

    public void addEntity(Entity entity) {
        componentsDenormalizer.addDenormalizable(entity.getComponentLabeler());
        tagsDenormalizer.addDenormalizable(entity.getTagLabeler());
    }

    public void removeEntity(Entity entity) {
        componentsDenormalizer.removeDenormalizable(entity.getComponentLabeler());
        tagsDenormalizer.removeDenormalizable(entity.getTagLabeler());
    }
}
