package tenth.system;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO:
 * Allow filtering
 */
public class FilterArrayListWrapper<E> implements Iterable<E> {
	private ArrayList<E> list = new ArrayList<E>(3);
	private Filterable filter;
	
	private Iterator<E> itr = new Iterator<E>() {
		
		private int counter = -1;
		
		/** The next item to return */
		private int nextHit = -1;
		
		/** The next item to filter for */
		private int queryCounter = 0;
		
		private boolean tested = false;
		
		private boolean cachedHasNext = false;
		
		/**
		 * Not idempotent! 
		 */
		@Override
		public boolean hasNext() {
			if (tested) {
				return cachedHasNext;
			}
			while (queryCounter < list.size()) {
				if (filter.filter(list.get(queryCounter)) != null) {
					nextHit = queryCounter;
					queryCounter++;
					tested = true;
					cachedHasNext = true;
					return true;
				}
				queryCounter++;
			}
			cachedHasNext = false;
			return false;
		}

		/**
		 * Call hasNext first to filter!
		 */
		@Override
		public E next() {
			tested = false;
			if (cachedHasNext) {
				return list.get(nextHit);
			}
			return null;  
		}

		/**
		 * Call hasNext first!
		 */
		@Override
		public void remove() {
			if (tested) {
				if (cachedHasNext) {
					list.remove(nextHit);
					queryCounter -= 1;
					tested = false;
				}	
			}
		}
	};
	
	public FilterArrayListWrapper() {
		
	}
	
	public void setFilter(Filterable<E> filter) {
		this.filter = filter;
	}
	
	public Iterator iterator() {
		return itr;
	}

	public static interface Filterable<E> {
		public E filter(E item);
	}
}
