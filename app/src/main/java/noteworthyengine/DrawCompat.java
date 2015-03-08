package noteworthyengine;

import java.util.List;

import structure.DoubleBufferredArrayList;
import structure.DoubleBufferredRewriteOnlyArray;
import structure.DrawList2DItem;
import structure.Game;
import structure.RewriteOnlyArray;
import structure.TemporaryDrawList2DItem;
import structure.TextDrawItem;
import utils.MemoryPool;

/**
 * Created by eric on 3/7/15.
 * TODO: Make abstract class
 */
public class DrawCompat {

    private RewriteOnlyArray<DrawList2DItem> spriteAllocator;
    private List<TemporaryDrawList2DItem> tempSprites;
    private DoubleBufferredRewriteOnlyArray<TextDrawItem> textDrawItem;
    private MemoryPool<TemporaryDrawList2DItem> tempSpritesMemoryPool;

    private Game game;

    public DrawCompat(Game game) {
        this.game = game;
        tempSprites = game.graphics.drawLists.temporarySprites;
        textDrawItem = game.graphics.drawLists.textDrawItems;
        tempSpritesMemoryPool = game.gamePool.temporaryDrawItems;
    }

    public void beginDraw() {
        spriteAllocator = game.graphics.drawLists.regularSprites.lockWritableBuffer();
        spriteAllocator.resetWriteIndex();
    }

    public void endDraw() {
        game.graphics.drawLists.regularSprites.unlockWritableBuffer();
        game.graphics.drawLists.regularSprites.finalizeUpdate();
    }

    public void drawSprite(DrawList2DItem drawList2DItem) {
        DrawList2DItem toFill = spriteAllocator.takeNextWritable();
        toFill.copy(drawList2DItem);
    }

    // TODO:
    public void drawTemporarySprite(TemporaryDrawList2DItem temporaryDrawList2DItem) {

    }
}
