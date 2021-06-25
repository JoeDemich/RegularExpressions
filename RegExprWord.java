import java.util.Random;

/* An instance of this class models a regular expression that represents
** a singleton set.  E.g., { abba }, { 00101 }.
*/
public class RegExprWord extends RegularExpression {

   // instance variable
   // -----------------

   private String word;

   // constructor
   // -----------

   public RegExprWord(String s) { word = s; }

   // observers
   // ---------

   @Override
   public boolean isMember(String x) { return x.equals(word); }

   @Override
   public boolean isFinite() { return true; }

   @Override
   public boolean isEmpty() { return false; }

   @Override
   public int minLength() { return word.length(); }

   @Override
   public int maxLength() { return word.length(); }

   @Override
   public String randomMember(Random rand) { return word; }

   @Override
   public String toString() { 
      if (word.length() == 0) { return "" + RegExprSymbols.LAMBDA; }
      else { return word; }
   }

   @Override
   public RegularExpression reverse() {
      return new RegExprWord(reverse(word));
   }

   private String reverse(String w) {
      if (w.length() == 0) { return w; }
      else { return reverse(w.substring(1)) + w.charAt(0); }
   }


}