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

    public static void recycle(Unit unit) {
        // Get units not by class, since they will be a glob of mixins
        //if (unit.getClass().getSimpleName() == "Mech") {
            //mechMemoryPool.recycleMemory((Mech)unit);
        //}
    }
}
