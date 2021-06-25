import java.util.Random;

public abstract class RegularExpression {

   // observers
   // ---------

   /* Reports whether the given string is a member of the language
   ** described by this regular expression.
   */
   public abstract boolean isMember(String x); 

   /* Reports whether the language described by this regular expression
   ** has finitely many members.
   */
   public abstract boolean isFinite();

   /* Reports whether the language described by this regular expression
   ** is empty (i.e., has no members).
   */
   public abstract boolean isEmpty();

   /* Reports the length of a shortest length string that is a member
   ** of the language described by this regular expression
   ** pre: !isEmpty()
   */
   public abstract int minLength();

   /* Reports the length of a longest length string that is a member
   ** of the language described by this regular expression.
   ** pre: isFinite()
   */
   public abstract int maxLength();


   // generator
   // ---------

   /* Returns a pseudo-randomly generated member of the language
   ** described by this regular expression.
   ** pre: !isEmpty()
   */
   public abstract String randomMember(Random rand);
   
   /* Returns a regular expression that describes the reverse of
   ** the language described by this one.
   */
   public abstract RegularExpression reverse();
}