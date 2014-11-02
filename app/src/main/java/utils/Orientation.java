package utils;

import game.androidgame2.Vector2;
import game.androidgame2.Vector3;

/**
 * TODO: Make fields private
 * TODO: Zero degrees problem
 *
 */
public class Orientation extends Vector2 {
	
	private double degrees;
	
	public Orientation() {
		this.degrees = 0.0;
		this.x = 0.0;
		this.y = 1.0;
	}
	
	public Orientation(double degree) {
		this.degrees = degree;
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
	}

	/**
	 * Will normalize direction given
	 * @param direction
	 */
	public void setDirection(Vector2 direction) {
		double norm = Math.sqrt(direction.x * direction.x) + (direction.y * direction.y);
		if (norm != 0) {
			this.x = direction.x / norm;
			this.y = direction.y / norm;
		}
	}
	
	public void setDegrees(double degrees) {
		this.degrees = degrees;
		this.x = Math.cos(Math.toRadians(degrees));
		this.y = Math.sin(Math.toRadians(degrees));
	}
	
	public static void setVectorToDegree(Vector3 vectorToSet, double degrees) {
		vectorToSet.x = vectorToSet.x * Math.cos(Math.toRadians(degrees)) - vectorToSet.y * Math.sin(Math.toRadians(degrees));
		vectorToSet.y = vectorToSet.x * Math.sin(Math.toRadians(degrees)) + vectorToSet.y * Math.cos(Math.toRadians(degrees));
	}
	
	public double getDegrees() {
//		if (this.x == 0) {
//			if (this.y > 0) {
//				return 90;
//			}
//			if (this.y < 0) {
//				return 270;
//			}
//			if (this.y == 0) {
//				return 0;
//			}
//		}
//		double ret = Math.toDegrees( Math.atan(this.y / this.x) );
//		if (this.x < 0) {
//			return ret - 180;
//		}
//		return ret;
		return Orientation.getDegrees(0, 0, this.x, this.y);
	}
	
	
	public static double getDegrees(Vector3 v1) {
		return Orientation.getDegrees(0, 0, v1.x, v1.y);
	}
	
	/** 
	 * TODO: Use acos instead of atan!!!
	 * Static version...
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getDegrees(double x1, double y1, double x2, double y2) {
		double xDelta = x2 - x1;
		double yDelta = y2 - y1; 
		
		double norm = Math.pow(xDelta, 2)+ Math.pow(yDelta, 2);
		if (norm != 0) {
			xDelta /= norm;
			yDelta /= norm;
		}
		
		if (xDelta < 0) {
			//xDelta = -xDelta;
		}
		
		if (xDelta == 0) {
			if (yDelta > 0) {
				return 90;
			}
			if (yDelta < 0) {
				return 270;
			}
			if (yDelta == 0) {
				return 0;
			}
		}
		double ret = Math.toDegrees( Math.atan(yDelta / xDelta) );
		
		if (xDelta < 0) {
			ret = -(180 - ret);
//			if (yDelta < 0) {
//				ret = (90 - ret) + 90;
//			}
//			else {
//				ret = 180 - ret;
//			}
		}
		
		return ret;
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
		this.degrees = orientation.degrees;
		this.x = orientation.x;
		this.y = orientation.y;
	}
	
	public void getPerpendicular(Vector2 output) {
        double pointX = 0;
        if (x == 0) {
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
