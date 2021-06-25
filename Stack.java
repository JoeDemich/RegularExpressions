/** An instance of a class that implements this interface represents a stack
**  capable of holding items of the specified type T (the generic type 
**  parameter).  A stack is a container having a LIFO ("last-in-first-out")
**  insertion/removal pattern.
**
**  Author: R. McCloskey
**  Date: January 2020
*/
public interface Stack<T> {

   // observers
   // ---------

   /** Returns the number of items on this stack.
   */
   int sizeOf(); 

   /** Returns true iff there are no items on this stack.
   */
   boolean isEmpty(); 

   /** Returns true if this stack fails to have the capacity to store
   **  any more items (i.e., push() would fail), false otherwise.
   */
   boolean isFull(); 

   /** Returns (a reference to) the item at the top of this stack.
   **  pre: !this.isEmpty()
   */
   T topOf();


   // mutators
   // --------

   /* Removes all the items from this stack, leaving it empty.
   ** post: isEmpty()
   */
   void clear();

   /** Places the specified item onto the top of this stack.
   **  pre:  !isFull()
   **  post: Letting elem(k) refer to the k-th item on a stack, counting
   **    from the bottom starting at zero, and letting s refer to this
   **    stack before push() is applied:
   **      this.sizeOf() = s.sizeOf() + 1 &&
   **      this.elem(s.sizeOf()) = item &&
   **      for all k satisfying 0 <= k < s.sizeOf(), this.elem(k) = s.elem(k) 
   */
   void push(T item);


   /** Removes the item at the top of the stack, returning (a reference to) it.
   **  pre: !isEmpty()
   **  post: Letting elem(k) refer to the k-th item on a stack, counting 
   **    from the bottom starting at zero, and letting s refer to this 
   **    stack before pop() is applied:
   **      this.sizeOf() = s.sizeOf() - 1 &&
   **      for all k satisfying 0 <= k < sizeOf(), this.elem(k) = s.elem(k) 
   */
   T pop();

}