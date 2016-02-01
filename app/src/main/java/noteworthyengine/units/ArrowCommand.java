package noteworthyengine.units;

import android.graphics.Color;

import art.Constants;
import noteworthyengine.DecayNode;
import noteworthyengine.FieldNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 3/8/15.
 */
public class ArrowCommand extends Unit {

    public static final String NAME = "ArrowCommand";

    //public static final float WIDTH = 3f;
    //public static final float HEIGHT = 3f;

    public static final String ANIMATION_FIELD_ARROW_EXISTING = "Animations/FieldArrows/Existing";
    public static final int RENDER_ALPHA = 128;

    FieldNode fieldNode;
    DecayNode decayNode;
    RenderNode renderNode;

    public ArrowCommand() {
        this.name = NAME;

        fieldNode = new FieldNode(this);
        fieldNode._fieldArrowNode = new FieldNode.FieldArrowNode(this);

        decayNode = new DecayNode(this);
        decayNode.timeToLive.v = 7;

        renderNode = new RenderNode(this);
        renderNode.animationName.v = ANIMATION_FIELD_ARROW_EXISTING;
        renderNode.color.v = Color.WHITE;
        renderNode.width.v = (float)fieldNode._fieldArrowNode.fieldArrowInfluenceRadius.v;
        renderNode.height.v = (float)fieldNode._fieldArrowNode.fieldArrowInfluenceRadius.v;
        renderNode.onDraw = onDraw;
    }

    public void set(Gamer gamer, double x, double y, double rx, double ry) {
        this.fieldNode.gamer.v = gamer;
        this.fieldNode._fieldArrowNode.coords.pos.set(x, y);
        this.fieldNode._fieldArrowNode.coords.rot.setDirection(rx, ry);

        int color = Constants.colorForTeam(gamer.team);

        this.renderNode.color.v = Color.argb(RENDER_ALPHA,
                Color.red(color),
                Color.green(color),
                Color.blue(color));
    }

    public VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem element) {
            //renderNode.color.v = Gamer.TeamColors.get(fieldNode.gamer.v.team);
        }
    };
}
