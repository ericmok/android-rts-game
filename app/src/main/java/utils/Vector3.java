package utils;

public class Vector3 implements JsonSerializable {
	public double x = 0;
	public double y = 0;
	public double z = 0;
	
	private float[] array;
	
	public Vector3() {
		array = new float[4];
	}
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		array = new float[4];
	}
	
	public void set(float[] vector) {
		this.x = vector[0];
		this.y = vector[1];
		this.z = vector[2];
	}
	
	public double[] getPacked() {
		double[] ret = new double[3];
		ret[0] = x;
		ret[1] = y;
		ret[2] = z;
		return ret;
	}
	
	public void scale(double d, double e, double sz) {
		x *= d;
		y *= e;
		z *= sz;
	}
	
	public void translate(float tx, float ty, float tz) {
		this.x += tx;
		this.y += ty;
		this.z += tz;
	}
	
	public double getSquaredDistance2d(Vector3 other) {
		return ( this.x * other.x + this.y * other.y );
	}
	
	/**
	 * Returns an internal 4 element float array that represents this vector
	 */
	public float[] asFloatArray() {
		array[0] = (float)x;
		array[1] = (float)y;
		array[2] = (float)z;
		array[3] = 1;
		return array;
	}
	
	public static void add(Vector3 output, Vector3 lhs, Vector3 rhs) {
		output.x = lhs.x + rhs.x;
		output.y = lhs.y + rhs.y;
		output.z = lhs.z + rhs.z;		
	}
	
	public static void subtract(Vector3 output, Vector3 lhs, Vector3 rhs) {
		output.x = lhs.x - rhs.x;
		output.y = lhs.y - rhs.y;
		output.z = lhs.z - rhs.z;
	}
	
	public static void scaled(Vector3 output, Vector3 toScale, double factor) {
		output.x = toScale.x * factor;
		output.y = toScale.y * factor;
		output.z = toScale.z * factor;
	}
	
	public static double dotProduct2d(Vector3 lhs, Vector3 rhs) {
		return lhs.x * rhs.x + lhs.y * rhs.y;
	}
	
	public static double dotProduct(Vector3 lhs, Vector3 rhs) {
		return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
	}
	
	public void setNormalized2d() {
		Vector3.getNormalized2d(this, this);
	}
	
	/**
	 * TODO: Fix all instances where i use this function
	 * Normalizes in 2d, disregarding the 3rd coordinate.
	 * @param output z value takes in normalized's original z value!
	 * @param toNormalize 
	 */
	public static void getNormalized2d(Vector3 output, Vector3 toNormalize) {
		double norm = toNormalize.x * toNormalize.x + toNormalize.y * toNormalize.y;
		norm = Math.sqrt(norm);
		
		if (norm != 0) {
			output.x = toNormalize.x / norm;
			output.y = toNormalize.y / norm;
			output.z = toNormalize.z;	
		}
	}

	public static double getDistanceBetween2d(Vector3 a, Vector3 b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
	public static double getDistanceBetween(Vector3 a, Vector3 b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
	}
	
	public static double getSquaredDistanceBetween2d(Vector3 a, Vector3 b) {
		return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
	}
	
	public static double getSquaredDistanceBetween(Vector3 a, Vector3 b) {
		return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2);
	}
	
	public Vector3 clone() {
		Vector3 clone = new Vector3(this.x, this.y, this.z);
		return clone;
	}
	
	public void copy(Vector3 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public boolean equals(Vector3 other) {
		return Vector3.equals(this, other);
	}
	
	public static boolean equals(Vector3 lhs, Vector3 rhs) {
		return (lhs.x == rhs.x) && (lhs.y == rhs.y) && (lhs.z == rhs.z);
	}
	
	public void zero() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public static void zero(Vector3 vec) {
		vec.zero();
	}
	
	public double getMagnitude2d() {
		return Math.sqrt((x*x) + (y*y));
	}
	
	/**
	 * Checks if magnitude is greater than clamp. Set to clamp if it is.
	 * @param clamp
	 */
	public void clampMagnitude2d(double clamp) {

		double norm = this.getMagnitude2d();
		
		if (norm > clamp) {
			if (norm != 0) {
				this.x = this.x / norm;
				this.y = this.y / norm;	
			}
			
			this.x = this.x * clamp;
			this.y = this.y * clamp;
		}
	}
	
	/**
	 * GC Unsafe
	 */
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

    public String json() {
        return "[" + x + "," + y + "," + z + "]";
    }
}
