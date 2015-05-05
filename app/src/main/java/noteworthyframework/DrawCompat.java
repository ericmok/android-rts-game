package noteworthyframework;

import java.util.List;

import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import structure.DoubleBufferredRewriteOnlyArray;
import structure.Sprite2dDef;
import structure.Game;
import structure.RewriteOnlyArray;
import structure.TemporarySprite2dDef;
import structure.TextDrawItem;
import utils.MemoryPool;

/**
 * Created by eric on 3/7/15.
 * TODO: Make abstract class
 */
public class DrawCompat {

    public RewriteOnlyArray<Sprite2dDef> spriteAllocator;
    public List<TemporarySprite2dDef> tempSprites;
    public DoubleBufferredRewriteOnlyArray<TextDrawItem> textDrawItem;
    public MemoryPool<TemporarySprite2dDef> tempSpritesMemoryPool;

    private final Game game;
    private RenderSystem renderSystem;

    public DrawCompat(Game game) {
        this.game = game;
        tempSprites = game.graphics.drawLists.temporarySprites;
        textDrawItem = game.graphics.drawLists.textDrawItems;
        tempSpritesMemoryPool = game.gamePool.temporaryDrawItems;
    }

    public void setRenderSystem(RenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }

    public void beginDraw() {
        spriteAllocator = game.graphics.drawLists.regularSprites.lockWritableBuffer();
        spriteAllocator.resetWriteIndex();
    }

    public void endDraw() {
        game.graphics.drawLists.regularSprites.unlockWritableBuffer();
        game.graphics.drawLists.regularSprites.finalizeUpdate();
    }

    public void drawSprite(Sprite2dDef sprite2dDef) {
        Sprite2dDef toFill = spriteAllocator.takeNextWritable();
        //toFill.cameraIndex = renderSystem.cameraSystem.nodes.get(toFill.cameraIndex).index.v;
        toFill.copy(sprite2dDef);
    }

    public TemporarySprite2dDef defineNewTempSprite(int cameraNodeIndex) {
        TemporarySprite2dDef temporarySprite2dDef = tempSpritesMemoryPool.fetchMemory();
        temporarySprite2dDef.cameraIndex = renderSystem.cameraSystem.nodes.get(cameraNodeIndex).index.v;
        return temporarySprite2dDef;
    }

    public Sprite2dDef defineNewSprite(int cameraNodeIndex) {
        Sprite2dDef toFill = spriteAllocator.takeNextWritable();
        toFill.cameraIndex = renderSystem.cameraSystem.nodes.get(cameraNodeIndex).index.v;
        return toFill;
    }

    public Sprite2dDef defineNewSprite(String animationName, int animationProgress,
                           float x, float y, float z,
                           float width, float height,
                           float angle,
                           int color, int cameraNodeIndex) {
        Sprite2dDef toFill = spriteAllocator.takeNextWritable();
        toFill.set(animationName, animationProgress, x, y, z, width, height, angle, color,
                renderSystem.cameraSystem.nodes.get(cameraNodeIndex).index.v);
        return toFill;
    }

    // TODO:
    public void drawTemporarySprite(TemporarySprite2dDef inTempSprite) {
        TemporarySprite2dDef temporarySprite2dDef = tempSpritesMemoryPool.fetchMemory();
        temporarySprite2dDef.copy(inTempSprite);
        tempSprites.add(temporarySprite2dDef);
    }


}
