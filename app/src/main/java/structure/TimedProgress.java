package structure;

public class TimedProgress {
	public String name;
	
	/**
	 * Range from
	 * 1/100 - 100/100
	 */
	public float progress;
	
	/** 
	 * Number of milliseconds for the operation to happen
	 */
	public float duration;
	
	/**
	 * Does the progress loop over?
	 */
	public boolean recurrent;
	
	private int directionMultiplier = 1;
	
	/**
	 * If true, progress updates goes up, otherwise goes down
	 */
	public boolean direction = true;
	
	
	public TimedProgress() {
		name = "Unnamed";
		progress = 1;
		duration = 1000.0f;
		recurrent = false;
		
		direction = true;
	}
	
	public TimedProgress(String name) {
		this.name = name;
		progress = 1;
		duration = 1000.0f;
		recurrent = false;
	}
	
	public TimedProgress(String name, float duration, boolean recurrent) {
		this.name = name;
		progress = 1;
		duration = duration;
		recurrent = recurrent;		
	}
	
	public void resetProgress() {
		this.progress = 1;
	}
	
	public float getProgress() {
		return this.progress;
	}
	
	/**
	 * Update the progress by a rate given by the duration for the progress to finish
	 * If progress goes over 100, then loop over.
	 * The process is continuous.
	 * 
	 * @param elapsedTime
	 * @return
	 */
	public float update(long elapsedTime) {
		
		if (direction) {
			directionMultiplier = 1;
		}
		else {
			directionMultiplier = -1;
		}
		
		progress = progress + directionMultiplier * (100.0f * (elapsedTime/this.duration));
		if (recurrent == true && progress > 100.0f) {
			progress = (progress % 100.0f);			
		}
		else if (recurrent == false && progress > 100.0f) {
			progress = 100.0f;
		}
		return this.progress;
	}

    public void set(float progress, float duration, boolean recurrent) {
        this.progress = progress;
        this.duration = duration;
        this.recurrent = recurrent;
        this.directionMultiplier = 1;
        this.direction = true;
    }

    public void reset() {
        this.progress = 1;
        this.duration = 1000.0f;
        this.recurrent = false;
        this.directionMultiplier = 1;
        this.direction = true;
    }

    public void copy(TimedProgress other) {
        this.name = other.name;
        this.progress = other.progress;
        this.duration = other.duration;
        this.recurrent = other.recurrent;
        this.directionMultiplier = other.directionMultiplier;
        this.direction = other.direction;
    }
}
