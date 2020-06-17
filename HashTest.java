import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
/**
 * HashTest is the driver program for a test of hash tables for various sizes
 * of alpha, where alpha = n/m, m is size of table, n is number of objects.
 * 
 * HashTest usage: $ java HashTest <input type> <load factor> [<debug level>]
 * 
 * input type: 0 for random integers, 1 for current time milliseconds, 2 for 
 * strings from "word-file"
 * 
 * load factor: a double between 0 and 1 inclusive, the load factor is how full
 * the hash table will be when the simulation is stopped.
 * 
 * debug level: OPTIONAL , empty or zero results in just a printout to the console
 * 1 results in two dump files being created of each tabel, the linear table and the
 * double hashing table. Each line of the dump file is the location in the table
 * (table[i]) and the element that is in the table at that position. If nothing is 
 * in the table at that position the position is skipped so that the dump files 
 * contain only non-empty locations in the table.
 * @author michaelelliott
 *
 */
public class HashTest {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		int totalInserts = 0;

		if(args.length == 2 || args.length == 3) {
			try {
				double loadFactor = Double.parseDouble(args[1]);
				int debugLevel = 0;
				int m = findPrimes();
				double n = (loadFactor*m); // n = alpha * m = loadFactor * m = number of objects to put in the list
				int duplicatesLinear = 0;
				int duplicatesDouble = 0;
				int probesLinear = 0;
				int probesDouble = 0;
				HashTable<HashObject> linearTable = new HashTable<HashObject>(m);
				HashTable<HashObject> doubleHashingTable = new HashTable<HashObject>(m,1);
				if(args.length == 3) {
					debugLevel = Integer.parseInt(args[2]);	
					if(debugLevel != 1 && debugLevel != 0) {
						throw new Exception();
					}
				}
				if (args[0].equals("1")) {
					/* build a HashTest with random integer type objects */
					System.out.println("A good table size is found: " + m + "\nData source type: Random Integer\n");
					Random rand = new Random();				
					int i = 0;
					while (i < n) {
						totalInserts++;
						int key = rand.nextInt();
						HashObject<Integer> hashObj = new HashObject<Integer>(key);
						HashObject<Integer> hashObj2 = new HashObject<Integer>(key);
						
						int finder = hashSearch(linearTable, hashObj);
						int finder2 = hashSearch(doubleHashingTable, hashObj2);
						if(finder > 0) {
							duplicatesLinear++;
						}
						else if (finder < 0) {
							int tempProbes = hashInsert(linearTable,hashObj);
							probesLinear += tempProbes;
							hashObj.setProbes(tempProbes);
							i++;
						}
						else {
							/* table is full */
							totalInserts--;
						}
						if(finder2 > 0) {
							duplicatesDouble++;
						}
						else if (finder2 < 0){
							int tempProbes = hashInsert(doubleHashingTable,hashObj2);
							probesDouble += tempProbes;
							hashObj2.setProbes(tempProbes);
						}
						else {
							/* table is full */
						}
					}
				}
				else if (args[0].equals("2")) {
					/* HashTest using System.currentTimeMillis() */
					int i = 0;
					System.out.println("A good table size is found: " + m + "\nData source type: System.currentTimeMillis\n");
					while(i < n) {	
						totalInserts++;
						long key = System.currentTimeMillis();
						HashObject<Long> hashObj = new HashObject<Long>(key);
						HashObject<Long> hashObj2 = new HashObject<Long>(key);
						int finder = hashSearch(linearTable, hashObj);
						int finder2 = hashSearch(doubleHashingTable, hashObj2);
						if(finder > 0) {
							duplicatesLinear++;
						}
						else if (finder < 0){
							int tempProbes = hashInsert(linearTable,hashObj);
							probesLinear += tempProbes;
							hashObj.setProbes(tempProbes);
							i++;
						}
						else {
							/* linearTable is full, do something, but not increase probes or duplicates */
							totalInserts--;
						}
						if(finder2 > 0) {
							duplicatesDouble++;
						}
						else if (finder2 < 0) {
							int tempProbes = hashInsert(doubleHashingTable,hashObj2);
							probesDouble += tempProbes;
							hashObj2.setProbes(tempProbes);
						}
						else {
							/* doubleHashingTable is full, do something, but not increase probes or duplicates */
						}
					}
				}
				else if (args[0].equals("3")) {
					/* HashTest using strings, one per line, from word-list */
					System.out.println("A good table size is found: " + m + "\nData source type: word-list\n");
					int i = 0;
					File file = new File("word-list");
					Scanner lineScan = new Scanner(file); /* read one line at a time, file is formatted for one word per line */
					while( i < n || !lineScan.hasNextLine()) {
						totalInserts++;
						String key = lineScan.nextLine();
						HashObject<String> hashObj = new HashObject<String>(key);
						HashObject<String> hashObj2 = new HashObject<String>(key);
						int finder = hashSearch(linearTable, hashObj);
						int finder2 = hashSearch(doubleHashingTable, hashObj2);
						if(finder > 0) {
							duplicatesLinear++;
						}
						else if (finder < 0) {
							int tempProbes = hashInsert(linearTable,hashObj);
							probesLinear += tempProbes;
							hashObj.setProbes(tempProbes);
							i++;
						}
						else {
							/* linearTable is full, do something, but not increase probes or duplicates */
							totalInserts--;
						}
						if(finder2 > 0) {
							duplicatesDouble++;
						}
						else if (finder2 < 0) {
							int tempProbes = hashInsert(doubleHashingTable,hashObj2);
							probesDouble += tempProbes;
							hashObj2.setProbes(tempProbes);
						}
						else {
							/* doubleHashingTable is full, do something, but not increase probes or duplicates */
						}
					}
					lineScan.close();
				}
				else {
					printUsage();
				}
				/* Print statement to console */
				double avgLinear = ((double)probesLinear)/((double)n);
				double avgDouble = ((double)probesDouble)/((double)n);
				System.out.println("Using Linear Hashing....\nInput " + totalInserts + " elements, of which " + duplicatesLinear + 
						" duplicates\nload factor = " + loadFactor + ", Avg. no. of probes " + avgLinear);
				System.out.println("\nUsing Double Hashing....\nInput " + totalInserts + " elements, of which " + duplicatesDouble +
						" duplicates\nload factor = " + loadFactor + ", Avg. no. of probes " + avgDouble);

				/* Write to dump files */
				if(debugLevel == 1) {
					File linear_dump = new File("linear-dump");
					File double_dump = new File("double-dump");
					linear_dump.delete();
					double_dump.delete();
					linear_dump.createNewFile();
					double_dump.createNewFile();
					PrintWriter linearWriter = new PrintWriter(new FileWriter(linear_dump));
					PrintWriter doubleWriter = new PrintWriter(new FileWriter(double_dump));
					for(int i = 0; i < linearTable.getSize(); i++) {						
						/* access linearTable[i], write that HashObject's toString() and table[i] to linear-dump */
						/* access doubleHashingTable[i], write that HashObjects toString() and table[i] to double-dump */
						if(linearTable.get(i) != null) {
							String linearStr = "table["+i+"]: " +linearTable.get(i).toString();
							linearWriter.println(linearStr);
						}
						if(doubleHashingTable.get(i) != null) {	
							String doubleStr = "table["+i+"]: " +doubleHashingTable.get(i).toString();
							doubleWriter.println(doubleStr);	
						}
					}
					linearWriter.close();
					doubleWriter.close();
				}
			}
			catch(Exception e) {
				printUsage();
				System.out.println(e.toString());
			}
		}
		else { 
			/* user had too many or too few arguments */
			printUsage();
		}		
	}
	/**
	 * Finds twin primes using square and multiply method 
	 * Returning the highest of two twin primes, the driver class
	 * will pass the highest twin to each program as its size for 
	 * the HashTable. The HashTable will know to use the prime and 
	 * its twin, retVal - 2.
	 * @return the highest of two twin primes.
	 */
	private static int findPrimes() {
		/*For prime number in range 95500 to 96000, return twin primes. */
		int p = 95501;
		boolean findFlag = false;
		int retVal = 0;
		while(p < 96000 && findFlag == false) {
			/*  is [ a ** (p-1) % p ] == 1 ? if yes, check with new a, if yes again
			 *  check p+2, if yes twice for p+2, p and p+2 are twin primes */ 			
			boolean check1 = baseEquals1(p);
			boolean check2 = false;
			boolean check3 = false;
			boolean check4 = false;
			if(check1 == true) {
				check2 = baseEquals1(p);
				if(check2 == true) {
					check3 = baseEquals1(p+2);
					if(check3 == true) {
						check4 = baseEquals1(p+2);
						if(check4 == true) {
							//found twin primes!
							findFlag = true;
							retVal = p + 2;
						}
					}
				}
			}
			p+=2;
		}
		return retVal;
	}
	/**
	 * private helper method to declutter the above findPrimes method
	 * Does square and multiply algorithm, with modulo, to find 
	 * if the number fed into it is most likely a prime. 
	 * @param p, the integer to be tested for 'primeness'
	 * @return boolean retVal, whether or not the a^(p-1)%p == 1;
	 */
	private static boolean baseEquals1(int p) {
		boolean retVal = false;		// assumes failure
		Random rando = new Random();
		int a = rando.nextInt(p);
		String binaryInt = Integer.toBinaryString(p-1);
		int length = binaryInt.length();
		long base = a; // this will be for j = 0;
		for(int j = 1; j < length; j++) {
			/* j is the location in the binaryInt */
			if(binaryInt.charAt(j)/1 == 48) { // the divide by 1 converts the char into an integer i believe.
				/* char is a zero */
				base *= base;
				base = base%p;
			}
			else {
				/* char is a 1 */
				base *= base;
				base *= a;
				base = base%p;
			}
		}
		if(base == 1) {
			retVal = true;
		}
		return retVal;
	}
	
	
	/** 
	 * primaryHash method, calculates the primary hash from the HashObject's key
	 * @param T, The hashTable that the Object is going into
	 * @param o, The hashObject to calculate the primary key for
	 * @return primaryHashValue
	 */
	@SuppressWarnings("rawtypes")
	private static int primaryHash(HashTable T, HashObject o) {
		int primaryHashValue = o.getKey().hashCode() % T.getSize();
		if(primaryHashValue < 0 ) {
			primaryHashValue += T.getSize();
		}
		return primaryHashValue;
	}
	/**
	 * secondaryHash method for double hashing. Calculates the secondary
	 * hash from the objects key.  A long is needed because of integer overflow
	 * for larger tables.
	 * @param T, the HashTable that the object is going into
	 * @param o, the HashObject to calculate the secondaryHash for
	 * @return the secondaryHashValue
	 */
	@SuppressWarnings("rawtypes")
	private static long secondaryHash(HashTable T, HashObject o) {
		long secondaryHashValue = 1 + o.getKey().hashCode()%(T.getSize()-2);
		if(secondaryHashValue < 0) {
			secondaryHashValue += (T.getSize()-2);
		}
		return secondaryHashValue;
	}
	
	/**
	 * hashInsert method checks the given HashTable to see if it is a 
	 * linear hashing table or a double hashing table, then implements
	 * the correct hashing strategy on the HashObject and hashes the object
	 * into the table.
	 * @param T
	 * @param k
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static int hashInsert(HashTable<HashObject> T, HashObject k) {
		int probes = 0;
		/* if T is linear, do linear probe and insert, else, do doubleHashing probe and insert */
		if (T.isLinear()) {
			boolean flag = false;
			int i = 0;
			while (i < T.getSize() && flag == false) {
				probes++;
				int j = (primaryHash(T,k) + i)%(T.getSize());
				if(T.get(j) == null) {
					T.set(j, k);
					flag = true;
				}
				else {
					i++;
				}	
			}
		}
		else { //double hashing insert
			int i = 0;
			boolean flag = false;
			while( i < T.getSize() && flag == false) {
				probes++;
				int j = (primaryHash(T,k)%T.getSize() + (int) (i*secondaryHash(T,k)%T.getSize())) %T.getSize();
				if(T.get(j) == null) {
					T.set(j, k);
					flag = true;
				}
				else {
					i++;
				}
			}	
		}
		return probes;
	}
	/**
	 * hashSearch, searches the specified HashTable for the HashObject k, 
	 * if there is an object at the hashCode value for the HashObject, the 
	 * hashSearch will check if they are equal by checking the 2 hashObject's key's
	 * @param T HashTable to search
	 * @param k HashObject to look for in T
	 * @return -1: T.get(j) == null; 0 if table is full, +1 if k is already in the list 
	 */
	@SuppressWarnings({ "rawtypes" })
	private static int hashSearch(HashTable<HashObject> T, HashObject k) {
		int retVal = 0; // true means the HashObject is in the HashTable already. hashSearch finds it.
		boolean flag = false;
		int i = 0; 
		if(T.isLinear()) {
			while (i < T.getSize() && flag == false) {
				int j = (primaryHash(T,k) + i)%T.getSize();
				if(T.get(j) == null) {
					/* The spot in the HashTable is empty, meaning the object with that hashCode has never been in the table */
					flag = true;
					retVal = -1;
				}
				
				else if (T.get(j).equals(k)) {
					/* T.get(j) is not null, and we must check if it equals the hashObject k.getKey() */
					flag = true;
					retVal = 1;
				}
				i++;
			}
		}
		else { // double hashing search
			while (i < T.getSize() && flag == false) {
				int j = (primaryHash(T,k)%T.getSize() + (int) (i*secondaryHash(T,k)%T.getSize())) %T.getSize();
				if(T.get(j) == null) {
					flag = true;
					retVal = -1;
				}
				else if (T.get(j).equals(k)) {
					retVal = 1;
					flag = true;
				}
				i++;
			}
		}
		return retVal;
	}
	
	/**
	 * usage method prints out a usage message for user.
	 */
	private static void printUsage() {
		System.out.println("Usage: java HashTest <input type> <load factor> [<debug level>]");
	}

}
