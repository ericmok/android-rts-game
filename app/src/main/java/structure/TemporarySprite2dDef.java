package structure;

public class TemporarySprite2dDef extends Sprite2dDef {
	
	public TimedProgress progress = new TimedProgress();
	
	public TemporarySprite2dDef() {
		super();
	}

    public void copy(TemporarySprite2dDef other) {
        super.copy(other);
        //this.progress.copy(other.progress);
    }
}
