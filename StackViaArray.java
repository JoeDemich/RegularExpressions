/** An instance of this class represents a stack capable of holding items
**  of the specified type T (the generic type parameter).
**  The implementation is based upon storing the stack items in an array.
**
**  Author: R. McCloskey
**  Date: January 2020
*/

public class StackViaArray<T> implements Stack<T> {

   // symbolic constant
   // -----------------

   private static final int INIT_CAPACITY_DEFAULT  = 8;


   // instance variables
   // ------------------

   protected int numItems;  // # items occupying the stack
   protected T[] items;     // holds (references to) the items on the stack


   // constructors
   // ------------

   /* Establishes this stack to be empty.
   */
   public StackViaArray(int initCapacity) {
      numItems = 0;
      items = (T[])(new Object[initCapacity]);
   }

   public StackViaArray() { this( INIT_CAPACITY_DEFAULT); }


   // observers
   // ---------

   public boolean isEmpty() { return sizeOf() == 0; }

   public boolean isFull() { return false; }

   public int sizeOf() { return numItems; }

   public T topOf() { return items[numItems-1]; }

   /** Returns a String containing the images of the items on this stack,
   **  (going from top to bottom) enclosed between square brackets and 
   **  separated from each other by spaces.
   **  Example:  "[ cat in the hat ]"
   */
   public String toString() {
      StringBuilder result = new StringBuilder("[ ");
      for (int i=0; i != numItems; i++) {
         result.append(item(i) + " ");
      }
      return result.append("]").toString();
   }

   /* Returns the k-th item on the stack, counting from the top
   ** starting at zero.
   ** pre: 0 <= k < sizeOf()
   */
   protected T item(int k) { return items[numItems - 1 - k]; }


   // mutators
   // --------

   public void clear() {
      // This loop is not necessary, but it could aid the garbage collector.
      for (int i=0; i != numItems; i++) {
         items[i] = null;
      }
      numItems = 0;
   }

   public void push( T item ) {
      if (numItems == items.length) {
         // items[] is full, so double its length by creating a new array
         // (having double the length), copying the values from items[]
         // into the new array, and then making items[] refer to the new array
         adjustArrayLength(2 * items.length);
      } 
      items[numItems] = item;
      numItems = numItems + 1;
   }


   public T pop() {
      T result = items[numItems-1];
      items[numItems-1] = null;  // to aid garbage collection
      numItems = numItems - 1;

      if (items.length >= 2 * INIT_CAPACITY_DEFAULT && 
          items.length >= 4 * numItems)
      {
         // The length of items[] is at least twice the default initial 
         // capacity and at least four times the stack's size, so cut the 
         // length of items[] in half.
         adjustArrayLength(items.length / 2);
      }
      return result;
   }


   // private  utility
   // ----------------

 
   /* Transfers the stack elements into a new array of the specified length
   ** and makes instance variable 'items' refer to that new array.
   ** pre: numItems <= newLength
   */
   private void adjustArrayLength(int newLength) {
      T[] newItems = (T[])(new Object[newLength]);
      for (int i=0; i != numItems; i++) { 
         newItems[i] = items[i];
      }
      items = newItems; 
   }
} 