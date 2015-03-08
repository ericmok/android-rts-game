package noteworthyengine;

import org.json.JSONException;

import java.util.ArrayList;

import model.DenormalizedDataSet;

/**
 * Created by eric on 3/6/15.
 */
public class EngineDataPack {

    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(8);

//    public ArrayList<Unit> unitsToAdd = new ArrayList<Unit>(127);
//    public ArrayList<Unit> unitsToRemove = new ArrayList<Unit>(127);
//    public ArrayList<Unit> units = new ArrayList<Unit>(127);

    public QueueMutationList<Unit> units = new QueueMutationList<Unit>(127);

    // TODO: Systems go here
    public QueueMutationList<MovementNode> movementNodes = new QueueMutationList<MovementNode>(127);
    public QueueMutationList<RenderNode> renderNodes = new QueueMutationList<RenderNode>(127);

    /// Global filters on units
    //
    // public DenormalizedDataSet<Unit, Integer> filters = new DenormalizedDataSet<Unit, Integer>(32, 127);

    /// Get units as grouped by nodes. To be used for systems
    //public DenormalizedDataSet<Unit, Class> unitsByNodes = new DenormalizedDataSet<Unit, Class>(32, 127);

    public double gameTime = 0;

    public Gamer currentGamer;

    public EngineDataPack() {
    }

    public void addGamer(Gamer gamer) {
        gamers.add(gamer);
    }
    public void removeGamer(Gamer gamer) {
        gamers.remove(gamer);
    }

//    public void eachGamerAddQueuedUnits() {
//        for (int i = 0; i < gamers.size(); i++) {
//            Gamer player = gamers.get(i);
//            player.addQueued();
//        }
//    }
//
//    public void eachGanerRemoveQueuedUnits() {
//        for (int i = 0; i < gamers.size(); i++) {
//            Gamer player = gamers.get(i);
//            player.removeQueued();
//        }
//    }
//
//    public void queueUnitToAdd(Unit entity) {
//        //entity.event = Entity.Event.ADDED;
//        unitsToAdd.add(entity);
//    }
//
//    public void queueUnitToRemove(Unit entity) {
//        //entity.event = Entity.Event.REMOVED;
//        this.unitsToRemove.add(entity);
//    }

//    public void addQueuedUnits() {
////        for (int j = 0; j < unitsToAdd.size(); j++) {
////            Unit toAdd = unitsToAdd.get(j);
////
////            // Add entity to denormalization mechanism TODO:
////            //this.filters.addDenormalizable(toAdd);
////            //this.unitsByNodes.addDenormalizable(toAdd);
////
////            this.addUnit(toAdd);
////        }
////        unitsToAdd.clear();
//    }
//
//    public void removeQueuedUnits() {
//        for (int j = 0; j < unitsToRemove.size(); j++) {
//            Unit toRemove = unitsToRemove.get(j);
//
//            // Remove entity from denormalization mechanism
//            //this.filters.removeDenormalizable(toRemove);
//            //this.unitsByNodes.removeDenormalizable(toRemove);
//
//            // TODO: Recycle
//            UnitPool.recycle(toRemove);
//        }
//        unitsToRemove.clear();
//    }

    public void addNode(Node node) {
        if (node.getClass() == MovementNode.class) {
            //this.movementNodes.items.add((MovementNode) node);
            this.movementNodes.queueToAdd((MovementNode) node);
        }
        if (node.getClass() == RenderNode.class) {
            //this.movementNodes.items.add((MovementNode) node);
            this.renderNodes.queueToAdd((RenderNode) node);
        }
    }

    public void removeNode(Node node) {
        if (node.getClass() == MovementNode.class) {
            //this.movementNodes.items.remove((MovementNode) node);
            this.movementNodes.queueToRemove((MovementNode) node);
        }
        if (node.getClass() == RenderNode.class) {
            //this.movementNodes.items.add((MovementNode) node);
            this.renderNodes.queueToRemove((RenderNode) node);
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
        movementNodes.flushQueues();
        renderNodes.flushQueues();
    }

    public void loadFromJson(String json) throws JSONException {
        EngineDataPackLoader.loadFromJson(this, json);
    }
}
