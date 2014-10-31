package game.androidgame2;

public class DoubleBufferredRewriteOnlyArray<E> {
	
	private RewriteOnlyArray<E> buffer1;
	private RewriteOnlyArray<E> buffer2;
	
	/**
	 * List that is actively written to
	 */
	private RewriteOnlyArray<E> activeList;
	
	private Class cls;
	
	private boolean writeLock = false;
	public Object isUpdatedMutex;
	private boolean isUpdated = false;
	
	public DoubleBufferredRewriteOnlyArray(Class cls, int capacity) {
		this.cls = cls;
		
		buffer1 = new RewriteOnlyArray<E>(cls, capacity);
		buffer2 = new RewriteOnlyArray<E>(cls, capacity);
		
		activeList = buffer1;
		
		isUpdatedMutex = new Object();
	}
	
	// TODO: Return null if locked by someone else
	public RewriteOnlyArray<E> lockWritableBuffer() {
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
	
	private RewriteOnlyArray<E> otherList(RewriteOnlyArray<E> list) {
		if (list == buffer1) {
			return buffer2;
		}
		else {
			return buffer1;
		}
	}
	
	public synchronized RewriteOnlyArray<E> swapBuffer() {
		
		synchronized(isUpdatedMutex) {
			
			// If active list is updated since last swap
			// Swap activeList pointer
			// return the active list that was just updated
			if (isUpdated) {
				RewriteOnlyArray<E> temp = activeList;
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
