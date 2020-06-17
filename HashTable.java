
/**
 * HashTable is an array of HashObjects
 * @author michaelelliott
 *
 * @param <HashObject>
 */
@SuppressWarnings({ "hiding" })
public class HashTable<HashObject> {
	private boolean isLinear;
	private int size;
	private HashObject[] table;
	
	/**
	 * Constructs a HashTable for linear probing
	 * @param size, size of the HashTable
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int size) {
		this.size = size;
		table = (HashObject[]) new Object[size];
		isLinear = true;
		
	}
	/**
	 * Constructs a HashTable for double hashing
	 * @param size, size of the HashTable
	 * @param i, any integer, it's a trigger to notify the constructor to make 
	 * a double hashing HashTable
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int size, int i) {
		table = (HashObject[]) new Object[size]; 
		isLinear = false;
		this.size = size;
		
	}
	/**
	 * Sets a HashObject into the HashTable at a specific position
	 * @param position, the position of the array to set the object
	 * @param obj, the object to be set into the array
	 */
	public void set(int position, HashObject obj) {
		table[position] = obj;
	}
	
	/**
	 * Get's a HashObject from a specific position in the HashTable
	 * Get can return null.
	 * @param position, the position in the array to get the HashObject, if it exists
	 * @return null if unoccupied, else the HashObject at the position
	 */
	public HashObject get(int position) {
		return table[position];
	}
	
	/**
	 * Checks if the HashTable is a linear hashing table
	 * or a double hashing table. Could be expanded with enumerations.
	 * @return true for linear, else false
	 */
	public boolean isLinear() {
		return isLinear;
	}
	
	/**
	 * The maximal size of the HashTable, not the number of
	 * elements currently contained.
	 * @return the capacity of the HashTable.
	 */
	public int getSize() {
		return size;
	}
}
