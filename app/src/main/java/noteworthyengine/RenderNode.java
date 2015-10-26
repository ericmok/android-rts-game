package noteworthyengine;

import android.graphics.Color;

import noteworthyframework.Coords;
import utils.DoublePtr;
import utils.FloatPtr;
import utils.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.StringPtr;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 3/7/15.
 */
public class RenderNode extends Node {

    public static final int RENDER_LAYER_FOREGROUND = 0;
    public static final int RENDER_LAYER_GUI = 1;

    public static final VoidFunc<RenderSystem> _DO_NOTHING = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem element) {

        }
    };

    public static final String _NAME = "renderNode";
    public String _name = _NAME;

    public Coords coords;
    public Vector2 gfxOldPosition;
    public DoublePtr z;

    public IntegerPtr isGfxInterpolated = new IntegerPtr() {{ v = 0; }};

    public IntegerPtr color = new IntegerPtr() {{ v = Color.WHITE; }};

    public FloatPtr width = new FloatPtr() {{ v = 1; }};
    public FloatPtr height = new FloatPtr() {{ v = 1; }};

    public StringPtr animationName;
    public IntegerPtr animationProgress;
    public IntegerPtr team;

    public IntegerPtr renderLayer = new IntegerPtr() {{
        v = RENDER_LAYER_FOREGROUND;
    }};

    public VoidFunc<RenderSystem> onDraw = _DO_NOTHING;

    public RenderNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, RenderNode.class, this);
    }

    public RenderNode(String name, Unit unit) {
        super(name, unit);
        Node.instantiatePublicFieldsForUnit(unit, RenderNode.class, this);
    }

    public void set(double x, double y, double z,
                    float width, float height,
                    double angle,
                    int color,
                    String animationName, int animationProgress,
                    int renderLayer) {
        this.isGfxInterpolated.v = 0;
        this.coords.pos.set(x, y);
        this.z.v = z;
        this.width.v = width;
        this.height.v = height;
        this.coords.rot.setDegrees(angle);
        this.color.v = color;
        this.animationName.v = animationName;
        this.animationProgress.v = animationProgress;
        this.renderLayer.v = renderLayer;
    }

    public void setInterpolated(double oldX, double oldY) {
        this.isGfxInterpolated.v = 1;
        this.gfxOldPosition.set(oldX, oldY);
    }
}
