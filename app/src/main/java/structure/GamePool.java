package structure;

import networking.Command;
import networking.CommandHistory;
import utils.MemoryPool;
import utils.Vector2;
import utils.Vector3;

public class GamePool {

	public MemoryPool<TriggerField> triggerFields;
	
	public MemoryPool<DrawList2DItem> drawItems;
	
	public MemoryPool<TemporaryDrawList2DItem> temporaryDrawItems;

    public MemoryPool<Vector2> vector2s;

	public MemoryPool<Vector3> vector3s;
	
	public MemoryPool<TimedProgress> timedProgresses;

    public MemoryPool<Command> commands;

	public GamePool() {

	}
	
	public void allocate() {

		drawItems = new MemoryPool<DrawList2DItem>(DrawList2DItem.class, 1024);
		
		temporaryDrawItems = new MemoryPool<TemporaryDrawList2DItem>(TemporaryDrawList2DItem.class, 1024);
		
		triggerFields = new MemoryPool<TriggerField>(TriggerField.class, 512);

        vector2s = new MemoryPool<Vector2>(Vector2.class, 512);

		vector3s = new MemoryPool<Vector3>(Vector3.class, 512);
		
		timedProgresses = new MemoryPool<TimedProgress>(TimedProgress.class, 512);

        commands = new MemoryPool<Command>(Command.class, CommandHistory.NUMBER_COMMANDS_IN_A_GAME);
	}
	
	/**
	 * Should belong to one of the pools
	 * @param objectToRecycle
	 */
	public void recycle(Object objectToRecycle, Class cls) {

//		// TODO: String allocations
//		//if (cls.getSimpleName().equalsIgnoreCase( Troop.class.getSimpleName())) {
//		if (cls.getName().equalsIgnoreCase( Troop.class.getName())) {
//			//Log.i("RECYCLED", "RECYCLED");
//			troops.recycleMemory((Troop)objectToRecycle);
//		}
	}
	
}
