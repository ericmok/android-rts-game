package noteworthyframework;

import java.util.List;

import noteworthyengine.RenderSystem;
import structure.DoubleBufferredRewriteOnlyArray;
import structure.Line2dDef;
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

    public DoubleBufferredRewriteOnlyArray<Line2dDef> linesToDrawDoubleBuffer;
    public RewriteOnlyArray<Line2dDef> linesAllocator;

    private final Game game;
    private RenderSystem renderSystem;

    public DrawCompat(Game game) {
        this.game = game;
        tempSprites = game.graphics.drawLists.temporarySprites;
        textDrawItem = game.graphics.drawLists.textDrawItems;
        tempSpritesMemoryPool = game.gamePool.temporaryDrawItems;

        linesToDrawDoubleBuffer = game.graphics.drawLists.linesToDraw;
    }

    public void setRenderSystem(RenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }

    public void beginDraw() {
        spriteAllocator = game.graphics.drawLists.regularSprites.lockWritableBuffer();
        spriteAllocator.resetWriteIndex();

        linesAllocator = linesToDrawDoubleBuffer.lockWritableBuffer();
        linesAllocator.resetWriteIndex();
    }

    public void endDraw() {
        game.graphics.drawLists.regularSprites.unlockWritableBuffer();
        game.graphics.drawLists.regularSprites.finalizeUpdate();

        linesToDrawDoubleBuffer.unlockWritableBuffer();
        linesToDrawDoubleBuffer.finalizeUpdate();
    }

    public void drawCopySprite(Sprite2dDef sprite2dDef) {
        Sprite2dDef toFill = spriteAllocator.takeNextWritable();
        toFill.copy(sprite2dDef);
    }

    public TemporarySprite2dDef fetchTempSprite() {
        TemporarySprite2dDef temporarySprite2dDef = tempSpritesMemoryPool.fetchMemory();
        return  temporarySprite2dDef;
    }

    public void recycleTempSprite(TemporarySprite2dDef inTempSprite) {
        tempSpritesMemoryPool.recycleMemory(inTempSprite);
    }

    public void drawTemporarySprite(TemporarySprite2dDef inTempSprite) {
        tempSprites.add(inTempSprite);
    }

    public Line2dDef declareLine2dDef() {
        return linesAllocator.takeNextWritable();
    }

}
