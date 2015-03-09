package structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The GraphicsAdapter translates data structures to graphics data structures.
 */
public class DrawLists {
	
	public static final int MAX_SPRITES = Game.MAX_UNITS;

	/** Draw List for sprites  */
	public DoubleBufferredRewriteOnlyArray<Sprite2dDef> regularSprites;

	/**
	 * Animations that are managed by graphics loops. Will automatically clean up when progress is finished.<br/>
	 * <strong>This is a synchronized List.</strong><br/>
	 * TODO: Make this better async by adding an add queue?
	 */
	public List<TemporarySprite2dDef> temporarySprites;

    /**
     * A list specific for for text.
     */
	public DoubleBufferredRewriteOnlyArray<TextDrawItem> textDrawItems;

	// Other circular buffers...
	
	public DrawLists() {

		regularSprites = new DoubleBufferredRewriteOnlyArray<Sprite2dDef>(Sprite2dDef.class, MAX_SPRITES);

		temporarySprites = Collections.synchronizedList(new ArrayList<TemporarySprite2dDef>(Game.MAX_UNITS));

        textDrawItems = new DoubleBufferredRewriteOnlyArray<TextDrawItem>(TextDrawItem.class, MAX_SPRITES);
	}
}
