package model;

import behaviors.Behaviors;
import utils.MemoryPool;

/**
 * Created by eric on 10/30/14.
 */
public class GameEntities {

    /**
     * Already scaffolded troops you can fetch and use.
     */
    public static MemoryPool<Troop> troopsMemoryPool = new MemoryPool<Troop>(Troop.class, 1024);
    public static MemoryPool<Projectile> projectilesMemoryPool = new MemoryPool<Projectile>(Projectile.class, 1024);

    public static MemoryPool<Buttons.AttackButton> attackButtonPool =
            new MemoryPool<Buttons.AttackButton>(Buttons.AttackButton.class, 1);

    public static MemoryPool<Buttons.DefendButton> defendButtonPool =
            new MemoryPool<Buttons.DefendButton>(Buttons.DefendButton.class, 1);

    public static void recycle(Entity entity) {
        if (entity.labels().contains(Behaviors.UNIT_TROOP)) {
            troopsMemoryPool.recycleMemory((Troop) entity);
        }
        if (entity.labels().contains(Behaviors.UNIT_BASIC_PROJECTILE)) {
            projectilesMemoryPool.recycleMemory((Projectile)entity);
        }
    }
}
