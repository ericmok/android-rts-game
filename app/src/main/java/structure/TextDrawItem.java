package structure;

import android.graphics.Color;

public class TextDrawItem {
	public StringBuilder stringBuilder = new StringBuilder(64);
	public Vector3 position = new Vector3();
	public float angle = 0;
	public float height = 0.1f;
	
	public int color = Color.WHITE;
}
