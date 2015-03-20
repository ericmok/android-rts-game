package noteworthyframework;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 */
public class BaseEngine {

    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(8);

    public QueueMutationList<Unit> units = new QueueMutationList<Unit>(127);

    // TODO: Systems go here?
    private ArrayList<System> systems = new ArrayList<System>(8);

    // TODO: Move into system? Denormalize?
    //public QueueMutationList<MovementNode> movementNodes = new QueueMutationList<MovementNode>(127);
    //public QueueMutationList<RenderNode> renderNodes = new QueueMutationList<RenderNode>(127);

    /// Global filters on units
    //
    // public DenormalizedDataSet<Unit, Integer> filters = new DenormalizedDataSet<Unit, Integer>(32, 127);

    /// Get units as grouped by nodes. To be used for systems
    //public DenormalizedDataSet<Unit, Class> unitsByNodes = new DenormalizedDataSet<Unit, Class>(32, 127);

    public double gameTime = 0;

    public int frameNumber = 0;

    public Gamer currentGamer;

    //private EngineDataLoader engineDataLoader;

    public BaseEngine() {
        //this.engineDataLoader = engineDataLoader;
    }

    public void addGamer(Gamer gamer) {
        gamers.add(gamer);
    }
    public void removeGamer(Gamer gamer) {
        gamers.remove(gamer);
    }

    public void addNode(Node node) {
        for (int i = 0; i < systems.size(); i++) {
            systems.get(i).addNode(node);
        }
    }

    public void removeNode(Node node) {
        for (int i = 0; i < systems.size(); i++) {
            systems.get(i).removeNode(node);
        }
    }

    public void addUnit(Unit unit) {
        //units.items.add(unit);
        units.queueToAdd(unit);

        for (int i = 0; i < unit.nodeList.size(); i++) {
            Node node = unit.nodeList.get(i);
            addNode(node);
        }
    }

    public void removeUnit(Unit unit) {
        //units.items.remove(unit);
        units.queueToRemove(unit);

        for (int i = 0; i < unit.nodeList.size(); i++) {
            Node node = unit.nodeList.get(i);
            removeNode(node);
        }

        UnitPool.recycle(unit);
    }

    public void flushQueues() {
        units.flushQueues();

        for (int i = 0; i < systems.size(); i++) {
            systems.get(i).flushQueues();
        }
    }

    public void addSystem(System system) {
        system.setBaseEngine(this);
        systems.add(system);
    }

    public void initialize() {
        for (int i = 0; i < this.systems.size(); i++) {
            System system = this.systems.get(i);
            system.initialize();
            system.flushQueues();
        }
    }

    public void step(double ct, double dt) {

        this.flushQueues();

        for (int i = 0; i < this.systems.size(); i++) {
            System system = this.systems.get(i);

            //system.flushQueues();
            system.step(ct, dt);
        }

        //this.flushQueues();

        this.frameNumber += 1;
    }

//    public void loadFromJson(String json) throws JSONException {
//        //dataLoader.loadFromJson(this, json);
//        engineDataLoader.loadFromJson(this, json);
//    }
}
