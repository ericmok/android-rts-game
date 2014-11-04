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

    public Vector2 copy(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public Vector2 set(double[] vec) {
        x = vec[0];
        y = vec[0];
        return this;
    }

    public Vector2 set(double d, double e) {
        x = d;
        y = e;
        return this;
    }

    public double[] getPacked() {
        double[] ret = new double[2];
        ret[0] = x;
        ret[1] = y;
        return ret;
    }

    public Vector2 scale(double d, double e) {
        x *= d;
        y *= e;
        return this;
    }

    public Vector2 translate(double d, double e) {
        x += d;
        y += e;
        return this;
    }

    public double distanceTo(Vector2 other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public double squaredDistanceTo(Vector2 other) {
        return (this.x * other.x + this.y * other.y);
    }

    public static void add(Vector2 output, Vector2 lhs, Vector2 rhs) {
        output.x = lhs.x + rhs.x;
        output.y = rhs.y + rhs.y;
    }

	public static void subtract(Vector2 output, Vector2 lhs, Vector2 rhs) {
		output.x = lhs.x - rhs.x;
		output.y = lhs.y - rhs.y;
	}

    public static void scaled(Vector2 output, Vector2 toScale, double factor) {
        output.x = toScale.x * factor;
        output.y = toScale.y * factor;
    }

    public static double dotProduct2d(Vector2 lhs, Vector2 rhs) {
        return lhs.x * rhs.x + lhs.y * rhs.y;
    }

	public Vector2 setNormalized() {
        Vector2.getNormalized(this, this);
        return this;
	}

    public static void getNormalized(Vector2 output, Vector2 toNormalize) {
        double norm = Math.sqrt(toNormalize.x * toNormalize.x + toNormalize.y * toNormalize.y);
        if (norm != 0) {
            output.x = toNormalize.x / norm;
            output.y = toNormalize.y / norm;
        }
        else {
            output.zero();
        }
    }

    public boolean equals(Vector2 other) {
        return (this.x == other.x && this.y == other.y);
    }

    public void zero() {
        this.x = 0; this.y = 0;
    }

    public double magnitude() {
       return this.distanceTo(this);
    }

    public void withClampMagnitude(double clamp) {
        double norm = this.magnitude();

        if (norm > clamp) {

            if (norm != 0) {
                this.x = this.x / norm;
                this.y = this.y / norm;
            }

            this.x = this.x * clamp;
            this.y = this.y * clamp;
        }
    }

    public String toString() {
        return "(" + String.format("%.3f", x) + "," + String.format("%.3f", y) + ")";
    }
}
