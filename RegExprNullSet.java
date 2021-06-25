import java.util.Random;

/* An instance of this class models a primitive regular expression 
** describing the language having no members.
*/
public class RegExprNullSet extends RegularExpression {

   @Override
   public boolean isMember(String x) { return false; }

   @Override
   public boolean isFinite() { return true; }

   @Override
   public boolean isEmpty() { return true; }

   @Override
   public int minLength() { return -1; }

   @Override
   public int maxLength() { return -1; }

   @Override
   public String randomMember(Random rand) { return null; }

   @Override
   public String toString() { return "" + RegExprSymbols.NULL_SET; }
   
   @Override
   public RegularExpression reverse() { return this; }

}