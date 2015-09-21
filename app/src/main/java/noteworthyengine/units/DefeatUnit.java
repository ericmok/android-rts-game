package noteworthyengine.units;

import art.Animations;

/**
 * Created by eric on 5/15/15.
 */
public class DefeatUnit extends WinUnit {

    public DefeatUnit() {
        super();
        this.renderNode.animationName.v = Animations.ANIMATIONS_WIN_DEFEAT_DEFEAT;
    }
}
