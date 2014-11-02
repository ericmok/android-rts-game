package game.androidgame2;

import android.util.Log;

public class GamePool {
	
	public MemoryPool<Troop> troops;
	
	public MemoryPool<TriggerField> triggerFields;
	
	public MemoryPool<DrawList2DItem> drawItems;
	
	public MemoryPool<TemporaryDrawList2DItem> temporaryDrawItems;

    public MemoryPool<Vector2> vector2s;

	public MemoryPool<Vector3> vector3s;
	
	public MemoryPool<TimedProgress> timedProgresses;

	public GamePool() {

	}
	
	public void allocate() {
		troops = new MemoryPool<Troop>(Troop.class, 1024);

		drawItems = new MemoryPool<DrawList2DItem>(DrawList2DItem.class, 2048);
		
		temporaryDrawItems = new MemoryPool<TemporaryDrawList2DItem>(TemporaryDrawList2DItem.class, 2048);
		
		triggerFields = new MemoryPool<TriggerField>(TriggerField.class, 512);

        vector2s = new MemoryPool<Vector2>(Vector2.class, 512);

		vector3s = new MemoryPool<Vector3>(Vector3.class, 512);
		
		timedProgresses = new MemoryPool<TimedProgress>(TimedProgress.class, 512);
		
	}
	
	/**
	 * Should belong to one of the pools
	 * @param objectToRecycle
	 */
	public void recycle(Object objectToRecycle, Class cls) {
		
		// TODO: String allocations
		//if (cls.getSimpleName().equalsIgnoreCase( Troop.class.getSimpleName())) {
		if (cls.getName().equalsIgnoreCase( Troop.class.getName())) {
			//Log.i("RECYCLED", "RECYCLED");
			troops.recycleMemory((Troop)objectToRecycle);
		}
	}
	
}
