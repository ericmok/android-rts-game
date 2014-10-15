package game.androidgame2;

public class ConcurrentCircularBufferWithRewritableArray<E> {
	
	private RewritableArray<E> buffer1;
	private RewritableArray<E> buffer2;
	
	/**
	 * List that is actively written to
	 */
	private RewritableArray<E> activeList;
	
	private Class cls;
	
	private boolean writeLock = false;
	public Object isUpdatedMutex;
	private boolean isUpdated = false;
	
	public ConcurrentCircularBufferWithRewritableArray(Class cls, int capacity) {
		this.cls = cls;
		
		buffer1 = new RewritableArray<E>(cls, capacity);
		buffer2 = new RewritableArray<E>(cls, capacity);
		
		activeList = buffer1;
		
		isUpdatedMutex = new Object();
	}
	
	// TODO: Return null if locked by someone else
	public RewritableArray<E> lockWritableBuffer() {
		writeLock = true;
		return activeList;
	}
	
	public void unlockWritableBuffer() {
		writeLock = false;
	}
	
	public void finalizeUpdate() {
		synchronized (isUpdatedMutex) {
			isUpdated = true;
		}
	}
	
	private RewritableArray<E> otherList(RewritableArray<E> list) {
		if (list == buffer1) {
			return buffer2;
		}
		else {
			return buffer1;
		}
	}
	
	public synchronized RewritableArray<E> swapBuffer() {
		
		synchronized(isUpdatedMutex) {
			
			// If active list is updated since last swap
			// Swap activeList pointer
			// return the active list that was just updated
			if (isUpdated) {
				RewritableArray<E> temp = activeList;
				activeList = otherList(activeList);
				isUpdated = false;
				return temp;
			}
			else {
				
				// If not updated, just return the non-active list
				return otherList(activeList);
			}
		}
	}
}
