package structure;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ArrayList wrapper that adds functional filter capability.
 * Each iteration.next() call returns a unit that passes the condition.
 * The iterator.hasNext() call is cached so conditions won't be re-evaluated for
 * next units unnecessarily.
 *
 * The conditional returns null if the condition is false or it can
 * operate on the node that passes the condition.
 *
 * TODO: Deprecate usage, it is allocation unsafe
 */
public class UnitArrayList<T> extends ArrayList<T> {
	
	public UnitArrayList(int maxUnits) {
		super(maxUnits);
	}

	public Iterator iterator(final Filterable filter) {
		final UnitArrayList self = this;
		
		return new Iterator<T>() {

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
					while (queryCounter < self.size()) {
						if (filter.filter(self.get(queryCounter)) != null) {
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
				public T next() {
					tested = false;
					if (cachedHasNext) {
						return (T)self.get(nextHit);
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
							self.remove(nextHit);
							queryCounter -= 1;
							tested = false;
						}	
					}
				}
		};
	}
	
	public interface Filterable<T> {
		public T filter(T toFilter);
	}
}
