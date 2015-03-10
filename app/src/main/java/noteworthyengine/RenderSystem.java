package noteworthyengine;

import noteworthyframework.*;
import structure.Sprite2dDef;

/**
 * Created by eric on 3/7/15.
 */
public class RenderSystem extends noteworthyframework.System {
    public DrawCompat drawCompat;
    private Sprite2dDef sprite2dDefTemp = new Sprite2dDef();

    public QueueMutationList<RenderNode> renderNodes = new QueueMutationList<RenderNode>(127);

    public RenderSystem(DrawCompat drawCompat) {
        this.drawCompat = drawCompat;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == RenderNode.class) {
            renderNodes.queueToAdd((RenderNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == RenderNode.class) {
            renderNodes.queueToRemove((RenderNode)node);
        }
    }

    @Override
    public void flushQueues() {
        renderNodes.flushQueues();
    }

    public void step(double ct, double dt) {

        // TODO:
        // ArrayList<Unit> unitsToDraw = engineData.unitsByNodes.getListForLabel(RenderNode.class);

        drawCompat.beginDraw();

        for (int i = 0; i < renderNodes.size(); i++) {
            RenderNode renderNode = renderNodes.get(i);

            if (!renderNode.isActive) {
                continue;
            }

            renderNode.onDraw.apply(this);

//            DrawList2DItem drawList2DItem = spriteAllocater.takeNextWritable();
//            drawList2DItem.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
//            drawList2DItem.animationProgress = 0;
//            drawList2DItem.position.x = renderNode.coords.pos.x;
//            drawList2DItem.position.y = renderNode.coords.pos.y;
//            drawList2DItem.position.z = 0;
//            drawList2DItem.width = (float)renderNode.width.v;
//            drawList2DItem.height = (float)renderNode.height.v;

            sprite2dDefTemp.animationName = renderNode.animationName;
            sprite2dDefTemp.animationProgress = renderNode.animationProgress.v;

            if (renderNode.isGfxInterpolated.v == 1) {
                sprite2dDefTemp.oldPosition.x = renderNode.gfxOldPosition.x;
                sprite2dDefTemp.oldPosition.y = renderNode.gfxOldPosition.y;
                sprite2dDefTemp.oldPosition.z = renderNode.z.v;
                sprite2dDefTemp.isGfxInterpolated = true;
            } else {
                sprite2dDefTemp.isGfxInterpolated = false;
            }

            sprite2dDefTemp.position.x = renderNode.coords.pos.x;
            sprite2dDefTemp.position.y = renderNode.coords.pos.y;
            sprite2dDefTemp.position.z = renderNode.z.v;
            sprite2dDefTemp.angle = (float)renderNode.coords.rot.getDegrees();

            sprite2dDefTemp.color = renderNode.color.v;
            sprite2dDefTemp.width = renderNode.width.v;
            sprite2dDefTemp.height = renderNode.height.v;

            drawCompat.drawSprite(sprite2dDefTemp);
        }

        drawCompat.endDraw();
    }
}
