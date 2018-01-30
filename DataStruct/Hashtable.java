/*
  Data Structures 2017
  Valtteri Vuori
  415642

  This class represents the Hash table.
 */

package DataStruct;

public class Hashtable {

    // MARK: Attributes
    private Hashbucket hTable[];
    private int capacity;
    private int size;

    // The load factor of the hashtable. In this exercise it will be a static 75%, as according to multiple
    // sources it is a recommended value.
    private double loadfactor;


    // MARK: Constructors

    // The default constructor which gets called always at the beginning of Tira2017.java
    public Hashtable() {
        capacity = 8;
        hTable = new Hashbucket[capacity];
        loadfactor = 0.75;
        size = 0;
    }

    // Constructor to increase the capacity of the array to given parameter value
    private Hashtable(int capacity) {
        this.capacity = capacity;
        hTable = new Hashbucket[capacity];
        loadfactor = 0.75;
        size = 0;
    }

    // MARK: Actions

    /**
     * Produces a hash value for the given key. Since we're
     * dealing with ints already, no need to do anything super complicated
     * Only thing is that with negative numbers we'll convert it to a positive number and thus a positive hash.
     */
    private int hash(int key) {
        return Math.abs(key) % capacity;
    }

    /**
     * Rehashes the given hashtable.
     * Only called when moving to a newer, different sized hashtable.
     */
    private void rehash(Hashtable newHash) {
        for (int i = 0; i < capacity; i++) {

            // See if the bucket has anything to move
            if (hTable[i] != null) {
                Hashbucket temp = hTable[i];

                // Move the content(s) of the hashbucket to the new table's
                // corresp. hashbucket
                while (temp != null) {
                    newHash.put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }

        // Overwrite the current values with fresh ones.
        this.hTable = newHash.hTable;
        this.capacity = newHash.capacity;
    }


    /**
     * Finds if key exists in the hashtable.
     * Returns a boolean value depending on the outcome.
     * Code is pretty self explanatory in this method and the next one, so no comments there.
     */
    public boolean contains(int key) {
        int hash = hash(key);
        if (hTable[hash] == null)
            return false;
        else {
            Hashbucket temp = hTable[hash];
            while (temp != null && temp.key != key) temp = temp.next;
            return temp != null;
        }
    }

    /**
     * Searches the hashtable for the given key.
     */
    public int get(int key) {
        int hash = hash(key);
        if (hTable[hash] == null)
            return 0;
        else {
            Hashbucket temp = hTable[hash];
            while (temp.key != key) temp = temp.next;
            return temp.value;
        }
    }

    /**
     * Inserts a key-value pair into the hashtable using the hash-function.
     * Rehashes the hashtable if needed based on loadfactor and capacity.
     */
    public void put(int key, int value) {

        // Check if trying to add a value that already exists.
        if (contains(key)) {

            System.out.println("Error! The value is already in place. Moving on.");

        // If not
        } else {

            // If the size exceeds the load factor, generate a
            // twice as big hashtable and rehash it immediately.
            if (size >= capacity * loadfactor)
                rehash(new Hashtable(capacity * 2));

            // Add a new value to the hashtable.
            int hash = hash(key);
            if (hTable[hash] == null) {
                hTable[hash] = new Hashbucket(key, value);
                size++;
            } else {
                Hashbucket temp = hTable[hash];
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = new Hashbucket(key, value);
                size++;
            }
        }
    }


    /**
     * Removes a key-value pair from the hashtable based on the given key.
     * Returns the value if removed, otherwise a 0.
     */
    public int remove(int key) {
        int hash = hash(key);

        // If such (hashed) key exists...
        if (hTable[hash] != null) {
            Hashbucket current = hTable[hash].next;
            Hashbucket previous = hTable[hash];
            if (key == previous.key){
                hTable[hash] = current;
                size--;
                return previous.value;
            }
            else {
                while (current != null && current.key != key) {
                    current = current.next;
                    previous = previous.next;
                }
                // In the unlikely case that with the correct hash no correct value is found
                // we'll do a check.
                if (current != null) {
                    previous.next = current.next;
                    size--;
                    return current.value;
                }
            }
        // And if not, we'll return a zero.
        } else {
            return 0;
        }
        return 0;
    }

    // MARK: Getters

    // Returns an enumeration of the keys in this hashtable.
    public int[] keys() {
        int secondIndex = 0;
        int array[] = new int[size];
        for (int k = 0; k < capacity; k++) {
            if (hTable[k] != null) {
                Hashbucket temp = hTable[k];
                while (temp != null) {
                    array[secondIndex] = temp.key;
                    temp = temp.next;
                    secondIndex++;
                }
            }
        }
        return array;
    }

    // See if the table is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Look at the size of the table. (Note that size =/= capacity !)
    public int size() {
        return size;
    }

}