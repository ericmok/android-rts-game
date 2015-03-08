package noteworthyengine;

import java.util.List;

import structure.DrawList2DItem;
import structure.Game;
import structure.RewriteOnlyArray;
import structure.TemporaryDrawList2DItem;

/**
 * Created by eric on 3/7/15.
 */
public class DrawCompat {

    private RewriteOnlyArray<DrawList2DItem> spriteAllocator;
    private List<TemporaryDrawList2DItem> tempSprites;
    private Game game;

    public DrawCompat(Game game) {
        this.game = game;
    }

    public final void drawBatch() {
        spriteAllocator = game.graphics.drawLists.regularSprites.lockWritableBuffer();

        //

        game.graphics.drawLists.regularSprites.unlockWritableBuffer();
        game.graphics.drawLists.regularSprites.finalizeUpdate();
    }
}
