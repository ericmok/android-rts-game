package structure;

import android.graphics.Color;

import utils.Orientation;
import utils.Vector2;
import utils.Vector3;

public class TextDrawItem {
	public StringBuilder stringBuilder = new StringBuilder(64);
	public Vector3 position = new Vector3();
	//public float angle = 0;
	public float height = 0.1f;

	public Orientation textDirection = new Orientation() {{
		this.setDegrees(0);
	}};

	public int cameraIndex = 0;

	public int color = Color.WHITE;
}
