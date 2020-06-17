/**
 * A HashObject is either an Integer, Long, or String object
 * that is used with the HashTable class to test linear
 * and double hashing.
 * @author michaelelliott
 *
 * @param <E>, a generic object
 */
public class HashObject<E> {
	private int probeCount;
	private int duplicateCount; // counts the number of times this hashObject has been attempted to be entered
	private E element; // the actual 'data' we are storing. Integer, String, or Long depending upon scenario
	
	public HashObject(E element) {
		probeCount = 0;
		duplicateCount = 0;
		this.element = element;
	}
	
	/**
	 * getKey method returns the key value of the HashObject
	 * which is the Integer, Long, or String.
	 * @return the Integer, Long, or String value of the HashObject
	 */
	public E getKey() {
		return this.element;
	}

	/**
	 * Example usage: x.equals(y)
	 * equals method checks if two HashObjects are equal by checking
	 * their Key's against each other. If they are equal, 
	 * x's duplicateCount will be incremented.
	 * 
	 * @return true if x.getKey() is the same as y.getKey(), else false
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object H) {
		boolean retVal = false;
		if (this.element.equals(((HashObject<E>) H).getKey())) {
			this.duplicateCount++;
			retVal = true;
		}
		return retVal;
	}
	
	/**
	 * setProbes method sets the number of probes for the HashObject
	 * @param probes, the number of probes it required to find a 
	 * spot in the HashTable for the first instance of this HashObject
	 */
	public void setProbes(int probes) {
		this.probeCount = probes;
	}
	
	/**
	 * toString returns a String of this HashObject and it's instance 
	 * variables.
	 * @return String of the element, duplicateCount and probeCount
	 */
	@Override
	public String toString() {
		return this.element +" "+duplicateCount+" "+probeCount;
	}
	
}
