package utils;

/**
 * Provides mutations that will normalize the vector.
 * Calling getDegrees() will cache the result.
 * Using appropriate orientation mutators will mark the
 * internal cache as dirty.
 */
public class Orientation extends Vector2 {

    /**
     * Though mathematically redundant, subsequent calls to getDegrees
     * will return cache instead as long as cacheDirty is not true.
     */
	private double cacheDegrees;

    /**
     * Mutations to orientation set this to true, so eager
     * mutations don't trigger premature calculation of degrees
     */
    private boolean cacheDirty = false;
	
	public Orientation() {
		this.cacheDegrees = 90.0;
		this.x = 0.0;
		this.y = 1.0;
	}
	
	public Orientation(double degree) {
		this.cacheDegrees = degree;
		this.setDegrees(degree);
	}
	
	/**
	 * Sets the orientation and also sets normalized
	 * @param x
	 * @param y
	 */
	public void setDirection(double x, double y) {
		this.x = x;
		this.y = y;
		this.setNormalized();
        cacheDirty = true;
	}

	/**
	 * Will normalize direction given
	 * @param direction
	 */
	public void setDirection(Vector2 direction) {
		this.copy(direction).setNormalized();
        cacheDirty = true;
	}
	
	public void setDegrees(double degrees) {
		this.x = Math.cos(Math.toRadians(degrees));
		this.y = Math.sin(Math.toRadians(degrees));
        this.cacheDegrees = degrees;
        cacheDirty = false;
	}
	
	public static void setVectorToDegree(Vector2 vectorToSet, double degrees) {
        double tempX = vectorToSet.x;
		vectorToSet.x = vectorToSet.x * Math.cos(Math.toRadians(degrees)) - vectorToSet.y * Math.sin(Math.toRadians(degrees));
		vectorToSet.y = tempX * Math.sin(Math.toRadians(degrees)) + vectorToSet.y * Math.cos(Math.toRadians(degrees));
	}
	
	public double getDegrees() {
        if (cacheDirty) {
            cacheDirty = false;
            return cacheDegrees = Orientation.getDegreesBaseX(this.x, this.y);
        }
        else {
            return cacheDegrees;
        }
	}

    public static double getDegrees(Vector2 base, Vector2 v2) {
        return Orientation.getDegrees(base.x, base.y, v2.x, v2.y);
    }
	
	public static double getDegrees(Vector2 v1) {
		return Orientation.getDegreesBaseX(v1.x, v1.y);
	}

    public static double getDegrees(Vector3 v1) {
        return Orientation.getDegreesBaseX(v1.x, v1.y);
    }

    /**
     * Gets degrees against standard vector (1,0).
     * This assumption makes it easier to faster to compute degrees by skipping
     * certain calculations.
     *
     * @param x1
     * @param y1
     * @return
     */
    public static double getDegreesBaseX(double x1, double y1) {
        double magnitude = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));

        if (magnitude == 0) {
            return 0;
        }

        // inverse cosine of result: [ v1 * (1,0) ] / [ magnitude v1 * (1) ]

        double ret = x1 / magnitude;
        ret = Math.toDegrees( Math.acos(ret) );

        if (y1 > 0) {
            return ret;
        }
        else {
            return -ret;
        }
    }

	/**
	 * Update: No longer need to normalize to use this method
     *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getDegrees(double x1, double y1, double x2, double y2) {
        double magnitude1 = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
        double magnitude2 = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0;
        }

        // inverse cosine of result: [ v1 * v2 ] / [ magnitude v1 * magnitude v2 ]

        double ret = (x1 * x2 + y1 * y2) / (magnitude1 * magnitude2);
        ret = Math.toDegrees( Math.acos(ret) );

        if (y2 - y1 > 0) {
            return ret;
        }
        else {
            return -ret;
        }
	}
	
	/**
	 * Adds the scaled vector to the orientation vector.
	 * A random component is added if the velocity is opposite the orientation
	 * @param vx
	 * @param vy
	 * @param turningRate
	 */
	public void addVector(double vx, double vy, double turningRate) {		
		this.setNormalized();
		
		if (vx == -x && vy == -y) {
		//if (vx == x && vy == y) {
			vx += (Math.random() > 0.5 ? -1 : 1) * 0.0001;
			vy += (Math.random() > 0.5 ? -1 : 1) * 0.0001;
		}
	
		this.x += vx * turningRate;
		this.y += vy * turningRate;
		this.setNormalized();
	}
	
	/**
	 * Deep copy without allocation.
	 * @param orientation
	 */
	public void copy(Orientation orientation) {
		this.cacheDegrees = orientation.getDegrees();
        this.cacheDirty = false;
		this.x = orientation.x;
		this.y = orientation.y;
	}
	
	public void getPerpendicular(Vector2 output) {
        double pointX = 0;
        if (x != 0) {
            pointX = (-y / x);
        }
		output.x = pointX;
		output.y = 1;
		output.setNormalized();
	}
	
	public void getPerpendicular(Vector3 output) {
		// bug took 2 days!
		if (x == 0) {
			output.x = 0;
			output.y = 1;
			output.z = 0;
			output.setNormalized2d();
			return;
		}
		
		double pointX = (-y / x);
		
		output.x = pointX;
		output.y = 1;
		output.z = 0;
		output.setNormalized2d();
	}
	
	public static void getPerpendicular(Vector3 output, Vector3 a) {
		// bug took 2 days!
		if (a.x == 0) {
			output.x = 0;
			output.y = 1;
			output.z = 0;
			output.setNormalized2d();
			return;
		}
		
		double pointX = (-a.y / a.x);
		output.x = pointX;
		output.y = 1;
		output.z = 1;
		output.setNormalized2d();
	}
}
