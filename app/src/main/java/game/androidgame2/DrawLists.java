package game.androidgame2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The GraphicsAdapter translates data structures to graphics data structures.
 */
public class DrawLists {
	
	public static final int MAX_SPRITES = Game.MAX_UNITS;

	/** Draw List for sprites  */
	public DoubleBufferredRewriteOnlyArray<DrawList2DItem> regularSprites;

	/**
	 * Animations that are managed by graphics loops. Will automatically clean up when progress is finished.<br/>
	 * <strong>This is a synchronized List.</strong><br/>
	 * TODO: Make this better async by adding an add queue?
	 */
	public List<TemporaryDrawList2DItem> temporarySprites;
	
	
	public DoubleBufferredRewriteOnlyArray<TextDrawItem> textDrawItems = new DoubleBufferredRewriteOnlyArray<TextDrawItem>(TextDrawItem.class, MAX_SPRITES);

	// Other circular buffers...
	
	public DrawLists() {

		regularSprites = new DoubleBufferredRewriteOnlyArray<DrawList2DItem>(DrawList2DItem.class, MAX_SPRITES);

		temporarySprites = Collections.synchronizedList(new ArrayList<TemporaryDrawList2DItem>(Game.MAX_UNITS)); 
	}
}
