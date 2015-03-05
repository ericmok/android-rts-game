package model;

import behaviors.Behaviors;
import behaviors.ButtonComponent;
import structure.DrawList2DItem;

/**
 * Created by eric on 11/3/14.
 */
public class Buttons {
    public static final String S_ATTACK = "S_ATTACK";
    public static final String DEFEND = "DEFEND";

    public static class AttackButton extends Entity {

        public AttackButton() {
            ButtonComponent bc = new ButtonComponent(Buttons.S_ATTACK);
            bc.position = 0;
            bc.texture = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
            bc.size.y = 0.6f;

            this.cData.put(ButtonComponent.class, bc);

            this.labels().add(Behaviors.UI_BUTTON);
        }

    }

    public static class DefendButton extends Entity {

        public DefendButton() {
            ButtonComponent bc = new ButtonComponent(Buttons.DEFEND);
            bc.position = 1;
            bc.texture = DrawList2DItem.ANIMATION_BUTTONS_DEFEND;
            bc.size.y = 0.6f;

            this.cData.put(ButtonComponent.class, bc);

            this.labels().add(Behaviors.UI_BUTTON);

        }
    }

}
