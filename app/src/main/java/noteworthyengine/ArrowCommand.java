package noteworthyengine;

import android.graphics.Color;

import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 3/8/15.
 */
public class ArrowCommand extends Unit {

    public static final String NAME = "ArrowCommand";

    public static final float WIDTH = 3f;
    public static final float HEIGHT = 3f;

    FieldNode fieldNode;
    RenderNode renderNode;

    public ArrowCommand() {
        this.name = NAME;

        fieldNode = new FieldNode(this);
        fieldNode._fieldArrowNode = new FieldNode.FieldArrowNode(this);

        renderNode = new RenderNode(this);
        renderNode.animationName = Sprite2dDef.ANIMATION_TRIGGER_FIELDS_EXISTING;
        renderNode.color.v = Color.WHITE;
        renderNode.width.v = WIDTH;
        renderNode.height.v = HEIGHT;
        renderNode.onDraw = onDraw;
    }

    public void set(Gamer gamer, double x, double y, double rx, double ry) {
        this.fieldNode.gamer.v = gamer;
        this.fieldNode._fieldArrowNode.coords.pos.set(x, y);
        this.fieldNode._fieldArrowNode.coords.rot.setDirection(rx, ry);
        this.renderNode.color.v = Gamer.TeamColors.get(gamer.team);
    }

    public VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem element) {
            renderNode.color.v = Gamer.TeamColors.get(fieldNode.gamer.v.team);
        }
    };
}
