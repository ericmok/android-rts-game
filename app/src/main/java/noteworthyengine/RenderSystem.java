package noteworthyengine;

import noteworthyframework.*;
import structure.Line2dDef;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;

/**
 * Created by eric on 3/7/15.
 */
public class RenderSystem extends noteworthyframework.System {
    private DrawCompat drawCompat;
    private Sprite2dDef sprite2dDefTemp = new Sprite2dDef();

    public QueueMutationList<RenderNode> renderNodes = new QueueMutationList<RenderNode>(127);

    public final CameraSystem cameraSystem;

    public RenderSystem(DrawCompat drawCompat, CameraSystem cameraSystem) {
        this.drawCompat = drawCompat;
        this.drawCompat.setRenderSystem(this);
        this.cameraSystem = cameraSystem;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == RenderNode.class) {
            renderNodes.queueToAdd((RenderNode) node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == RenderNode.class) {
            renderNodes.queueToRemove((RenderNode) node);
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

            // If no cameras, then don't render anything...
            if (!(renderNode.renderLayer.v >= 0 && renderNode.renderLayer.v < cameraSystem.nodes.size())) {
                continue;
            }

            // This appears before the drawing so that the function has a chance
            // to operate on the renderNode before it is processed.
            renderNode.onDraw.apply(this);

//            DrawList2DItem drawList2DItem = spriteAllocater.takeNextWritable();
//            drawList2DItem.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
//            drawList2DItem.animationProgress = 0;
//            drawList2DItem.position.x = renderNode.coords.pos.x;
//            drawList2DItem.position.y = renderNode.coords.pos.y;
//            drawList2DItem.position.z = 0;
//            drawList2DItem.width = (float)renderNode.width.v;
//            drawList2DItem.height = (float)renderNode.height.v;

            sprite2dDefTemp.animationName = renderNode.animationName.v;
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

            sprite2dDefTemp.cameraIndex = cameraSystem.nodes.get(renderNode.renderLayer.v).index.v;

            drawCompat.drawCopySprite(sprite2dDefTemp);
        }

        drawCompat.endDraw();
    }

    public int getCameraIndex(int renderLayer) {
        return cameraSystem.nodes.get(renderLayer).index.v;
    }

    public Sprite2dDef defineNewSprite(Sprite2dDef toCopy, int renderLayer) {
        Sprite2dDef toFill = drawCompat.spriteAllocator.takeNextWritable();
        toFill.copy(toCopy);
        toFill.cameraIndex = getCameraIndex(renderLayer);
        return toFill;
    }

    public Sprite2dDef defineNewSprite(String animationName, int animationProgress,
                                       float x, float y, float z,
                                       float width, float height,
                                       float angle,
                                       int color, int renderLayer) {
        Sprite2dDef toFill = drawCompat.spriteAllocator.takeNextWritable();
        toFill.set(animationName, animationProgress, x, y, z, width, height, angle, color,
                getCameraIndex(renderLayer));
        return toFill;
    }

    public TemporarySprite2dDef beginNewTempSprite() {
        TemporarySprite2dDef temporarySprite2dDef = drawCompat.fetchTempSprite();
        return temporarySprite2dDef;
    }

    public void endNewTempSprite(TemporarySprite2dDef temporarySprite2dDef, int renderLayer) {
        temporarySprite2dDef.cameraIndex = getCameraIndex(renderLayer);
        drawCompat.drawTemporarySprite(temporarySprite2dDef);
    }

    public void drawLine(int cameraIndex, float x1, float y1, float x2, float y2, int width, int color) {
        Line2dDef line2dDef = drawCompat.declareLine2dDef();
        line2dDef.src.set(x1, y1);
        line2dDef.dest.set(x2, y2);
        line2dDef.cameraIndex = cameraIndex;
        line2dDef.color = color;
        line2dDef.width = width;
    }

//    public TemporarySprite2dDef defineNewTempSprite(Sprite2dDef toCopy, int cameraName) {
//        TemporarySprite2dDef temporarySprite2dDef = drawCompat.tempSpritesMemoryPool.fetchMemory();
//        temporarySprite2dDef.copy(toCopy);
//        temporarySprite2dDef.cameraIndex = getCameraIndex(cameraName);
//        drawCompat.drawTemporarySprite(temporarySprite2dDef);
//        return temporarySprite2dDef;
//    }
//
//    public TemporarySprite2dDef defineNewTempSprite(TemporarySprite2dDef toCopy, int cameraName) {
//        TemporarySprite2dDef temporarySprite2dDef = drawCompat.tempSpritesMemoryPool.fetchMemory();
//        temporarySprite2dDef.copy(toCopy);
//        temporarySprite2dDef.cameraIndex = getCameraIndex(cameraName);
//        drawCompat.drawTemporarySprite(temporarySprite2dDef);
//        return temporarySprite2dDef;
//    }
//
//    public TemporarySprite2dDef defineNewTempSprite(String animationName, int animationProgress,
//                                                    float x, float y, float z,
//                                                    float width, float height,
//                                                    float angle,
//                                                    int color, int cameraNodeIndex)  {
//        TemporarySprite2dDef temporarySprite2dDef = drawCompat.tempSpritesMemoryPool.fetchMemory();
//        temporarySprite2dDef.set(animationName, animationProgress, x, y, z, width, height, angle, color, getCameraIndex(cameraNodeIndex));
//        drawCompat.drawTemporarySprite(temporarySprite2dDef);
//        return temporarySprite2dDef;
//    }

}
