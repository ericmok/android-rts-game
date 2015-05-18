package noteworthyengine;

import noteworthyframework.*;
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
            if (!(renderNode.cameraType.v >= 0 && renderNode.cameraType.v < cameraSystem.nodes.size())) {
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

            //sprite2dDefTemp.cameraIndex = renderNode.cameraType.v;
            sprite2dDefTemp.cameraIndex = cameraSystem.nodes.get(renderNode.cameraType.v).index.v;

            drawCompat.drawCopySprite(sprite2dDefTemp);
        }

        drawCompat.endDraw();
    }

    public int getCameraId(int name) {
        return cameraSystem.nodes.get(name).index.v;
    }

    public Sprite2dDef defineNewSprite(Sprite2dDef toCopy, int cameraName) {
        Sprite2dDef toFill = drawCompat.spriteAllocator.takeNextWritable();
        toFill.copy(toCopy);
        toFill.cameraIndex = getCameraId(cameraName);
        return toFill;
    }

    public Sprite2dDef defineNewSprite(String animationName, int animationProgress,
                                       float x, float y, float z,
                                       float width, float height,
                                       float angle,
                                       int color, int cameraNodeIndex) {
        Sprite2dDef toFill = drawCompat.spriteAllocator.takeNextWritable();
        toFill.set(animationName, animationProgress, x, y, z, width, height, angle, color,
                getCameraId(cameraNodeIndex));
        return toFill;
    }

    public TemporarySprite2dDef beginNewTempSprite() {
        TemporarySprite2dDef temporarySprite2dDef = drawCompat.fetchTempSprite();
        return temporarySprite2dDef;
    }

    public void endNewTempSprite(TemporarySprite2dDef temporarySprite2dDef, int cameraName) {
        temporarySprite2dDef.cameraIndex = getCameraId(cameraName);
        drawCompat.drawTemporarySprite(temporarySprite2dDef);
    }

//    public TemporarySprite2dDef defineNewTempSprite(Sprite2dDef toCopy, int cameraName) {
//        TemporarySprite2dDef temporarySprite2dDef = drawCompat.tempSpritesMemoryPool.fetchMemory();
//        temporarySprite2dDef.copy(toCopy);
//        temporarySprite2dDef.cameraIndex = getCameraId(cameraName);
//        drawCompat.drawTemporarySprite(temporarySprite2dDef);
//        return temporarySprite2dDef;
//    }
//
//    public TemporarySprite2dDef defineNewTempSprite(TemporarySprite2dDef toCopy, int cameraName) {
//        TemporarySprite2dDef temporarySprite2dDef = drawCompat.tempSpritesMemoryPool.fetchMemory();
//        temporarySprite2dDef.copy(toCopy);
//        temporarySprite2dDef.cameraIndex = getCameraId(cameraName);
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
//        temporarySprite2dDef.set(animationName, animationProgress, x, y, z, width, height, angle, color, getCameraId(cameraNodeIndex));
//        drawCompat.drawTemporarySprite(temporarySprite2dDef);
//        return temporarySprite2dDef;
//    }

}
