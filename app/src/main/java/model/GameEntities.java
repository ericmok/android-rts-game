package model;

import structure.DrawList2DItem;
import utils.MemoryPool;

/**
 * Created by eric on 10/30/14.
 */
public class GameEntities {

    /**
     * Already scaffolded troops you can fetch and use.
     */
    public static MemoryPool<Troop> troopsMemoryPool = new MemoryPool<Troop>(Troop.class, 1024);


    public static void recycle(Entity entity) {
        if (entity.labels().contains(Entity.UNIT_TROOP)) {
            troopsMemoryPool.recycleMemory((Troop) entity);
        }
    }

    public static Entity buildAttackButton() {
        Entity button = new Entity();

        ButtonComponent bc = new ButtonComponent(Buttons.S_ATTACK);
        bc.position = 0;
        bc.texture = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
        bc.size.y = 0.6f;

        button.cData.put(ButtonComponent.class, bc);

        button.labels().add(Entity.UI_BUTTON);

        return button;
    }

    public static Entity buildDefendButton() {
        Entity button = new Entity();

        ButtonComponent bc = new ButtonComponent(Buttons.DEFEND);
        bc.position = 1;
        bc.texture = DrawList2DItem.ANIMATION_BUTTONS_DEFEND;
        bc.size.y = 0.6f;

        button.cData.put(ButtonComponent.class, bc);

        button.labels().add(Entity.UI_BUTTON);

        return button;
    }

}
