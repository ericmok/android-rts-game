package tenth.system;

import game.androidgame2.TimedProgress;

public class LivingComponent {
	
	public enum Transition {
		SPAWNING,
		DYING
	};
	
	public enum State {
		PRISTINE,
		ALIVE,
		DEAD
	};
	
	public Transition transition = null;

	public float millisecondTimeOfTransition;
	
	public TimedProgress transitionProgress = new TimedProgress();
	
	public State state = State.PRISTINE;
	
}
