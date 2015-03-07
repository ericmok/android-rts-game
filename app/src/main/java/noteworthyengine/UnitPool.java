package noteworthyengine;

import utils.MemoryPool;

/**
 * Created by eric on 3/7/15.
 */
public class UnitPool {
    /**
     * Already scaffolded troops you can fetch and use.
     */
    public static MemoryPool<Mech> mechMemoryPool = new MemoryPool<Mech>(Mech.class, 1024);
    public static MemoryPool<Troopy> troopyMemoryPool = new MemoryPool<Troopy>(Troopy.class, 1024);

    public static void recycle(Unit unit) {

        if (unit.name == Troopy.NAME) {
            troopyMemoryPool.recycleMemory((Troopy)unit);
        }
        if (unit.name == Mech.NAME) {
            mechMemoryPool.recycleMemory((Mech)unit);
        }

        // Get units not by class, since they will be a glob of mixins
        //if (unit.getClass().getSimpleName() == "Mech") {
            //mechMemoryPool.recycleMemory((Mech)unit);
        //}
    }
}
