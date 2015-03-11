package structure;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * <p>
 *     A collection of objects where objects are never removed but only rewritten.
 * </p>
 * <p>
 *     You cannot add an entry, you can only rewrite an existing entry. Make sure
 *     when you rewrite the entry, that is error-free!
 * </p>
 *
 */
public class RewriteOnlyArray<E> {
	
	private Class<E> cls;
	private E[] data;
	private int lastIndex;
	
	private int singleIteratorIndex;
	
	public RewriteOnlyArray(Class<E> cls, int capacity) {
		this.cls = cls;
		lastIndex = -1;
		
		singleIteratorIndex = -1;
		
		if (capacity < 1) {
			capacity = 1024;
		}
		
		data = (E[])Array.newInstance(cls, capacity);
		
		for (int i = 0; i < data.length; i++) {
			try {
				data[i] = cls.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public E get(int index) {
		return data[index];
	}
	
	/**
	 * Swaps the element in current index with the last element and reduce the size.<br/>
	 * This has the effect of making the item invisible.
	 * @param index
	 */
	public void remove(int index) {
		E toDelete = data[index];
		// swap
		data[index] = data[data.length - 1];
		data[data.length - 1] = toDelete;
		lastIndex -= 1;
	}
	
	public E takeNextWritable() {
		lastIndex = lastIndex + 1;
		E ret = data[lastIndex];
		return ret;
	}
	
	public void resetWriteIndex() {
		lastIndex = -1;
	}
	
	public int size() {
		return lastIndex + 1;
	}
	
	/**
	 * Checks if internal iterator counter is less than lastIndex 
	 */
	public boolean canIterateNext() {
		return singleIteratorIndex < lastIndex;
	}
	
	/**
	 * Get item under internal iterator index
	 */
	public E getNextIteratorItem() {
		singleIteratorIndex += 1;
		return data[singleIteratorIndex];
	}
	
	/**
	 * Reset iterator
	 */
	public void resetIterator() {
		singleIteratorIndex = -1;
	}
	
	
	/**
	 * Avoid using since it allocates iterator from heap
	 */
	public Iterator<E> iterator() {
		return 	new Iterator<E>() {
			private int currentIndex = -1;
			
			public boolean hasNext() {
				return currentIndex < lastIndex;
			}
			
			public E next() {
				currentIndex += 1;
				return data[currentIndex];
			}
			
			/**
			 * Can't remove from RewritableArray, you can only rewrite
			 */
			public void remove() { }
			
		};
	}

    /**
     * Swaps elements by indices
     * @param index
     * @param otherIndex
     */
    public void swap(int index, int otherIndex) {
        E temp = data[index];
        data[index] = data[otherIndex];
        data[otherIndex] = temp;
    }

    /// Use this only if the generic type is comparable
    public void sort() {
        Arrays.sort(data, 0, lastIndex + 1);
    }

    /// Use this only if the generic type is comparable
    public void sort(int startIndex, int endIndex) {
        Arrays.sort(data, Math.min(startIndex, 0), Math.max(endIndex, lastIndex + 1));
    }

    public void sort(int startIndex, int endIndex, Comparator comparator) {
        Arrays.sort(data, Math.min(startIndex, 0), Math.max(endIndex, lastIndex + 1), comparator);
    }
    
    public void sort(Comparator comparator) {
        Arrays.sort(data, 0, lastIndex + 1, comparator);
    }
}
