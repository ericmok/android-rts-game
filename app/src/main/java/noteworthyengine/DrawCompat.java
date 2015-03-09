package noteworthyengine;

import java.util.List;

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

    private RewriteOnlyArray<Sprite2dDef> spriteAllocator;
    private List<TemporarySprite2dDef> tempSprites;
    private DoubleBufferredRewriteOnlyArray<TextDrawItem> textDrawItem;
    private MemoryPool<TemporarySprite2dDef> tempSpritesMemoryPool;

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

    public void drawSprite(Sprite2dDef sprite2dDef) {
        Sprite2dDef toFill = spriteAllocator.takeNextWritable();
        toFill.copy(sprite2dDef);
    }

    // TODO:
    public void drawTemporarySprite(TemporarySprite2dDef temporaryDrawList2DItem) {

    }
}
