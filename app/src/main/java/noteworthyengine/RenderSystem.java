package noteworthyengine;

import java.util.ArrayList;

import structure.DrawList2DItem;
import structure.RewriteOnlyArray;
import structure.TemporaryDrawList2DItem;

/**
 * Created by eric on 3/7/15.
 */
public class RenderSystem extends System {
    public EngineDataPack engineDataPack;
    public DrawCompat drawCompat;
    private DrawList2DItem drawList2DItemTemp = new DrawList2DItem();

    public QueueMutationList<RenderNode> renderNodes = new QueueMutationList<RenderNode>(127);

    public RenderSystem(EngineDataPack engineDataPack, DrawCompat drawCompat) {
        this.engineDataPack = engineDataPack;
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
        // ArrayList<Unit> unitsToDraw = engineDataPack.unitsByNodes.getListForLabel(RenderNode.class);

        drawCompat.beginDraw();

        for (int i = 0; i < renderNodes.size(); i++) {
            RenderNode renderNode = (RenderNode)renderNodes.get(i);

//            DrawList2DItem drawList2DItem = spriteAllocater.takeNextWritable();
//            drawList2DItem.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
//            drawList2DItem.animationProgress = 0;
//            drawList2DItem.position.x = renderNode.coords.pos.x;
//            drawList2DItem.position.y = renderNode.coords.pos.y;
//            drawList2DItem.position.z = 0;
//            drawList2DItem.width = (float)renderNode.width.v;
//            drawList2DItem.height = (float)renderNode.height.v;

            drawList2DItemTemp.animationName = renderNode.animationName;
            drawList2DItemTemp.animationProgress = renderNode.animationProgress.v;

            drawList2DItemTemp.position.x = renderNode.coords.pos.x;
            drawList2DItemTemp.position.y = renderNode.coords.pos.y;
            drawList2DItemTemp.position.z = renderNode.z.v;
            drawList2DItemTemp.angle = (float)renderNode.coords.rot.getDegrees();

            drawList2DItemTemp.color = renderNode.color.v;
            drawList2DItemTemp.width = renderNode.width.v;
            drawList2DItemTemp.height = renderNode.height.v;

            drawCompat.drawSprite(drawList2DItemTemp);
        }

        drawCompat.endDraw();
    }
}
