package game.androidgame2;

import android.graphics.Color;

public class DrawListTextItem {
	public StringBuilder text;
	
	public Vector3 position = new Vector3();
	public float width = 1.0f;
	public float height = 1.0f;
	public float angle = 0.0f;
	
	public int color = Color.WHITE;
	
	public void resetColor() {
		color = Color.WHITE;
	}
}
