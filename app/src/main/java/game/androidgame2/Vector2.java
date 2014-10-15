package game.androidgame2;

public class Vector2 {
	public double x;
	public double y;
	
	public Vector2() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2(double d, double e) {
		this.x = d;
		this.y = e;
	}
	
	public static void subtract(Vector2 output, Vector2 lhs, Vector2 rhs) {
		output.x = lhs.x - rhs.x;
		output.y = lhs.y - rhs.y;
	}
	
	public void setNormalized() {
		double norm = Math.sqrt(x*x + y*y);
		if (norm != 0) {
			x = x / norm;
			y = y / norm;	
		}
	}
}
