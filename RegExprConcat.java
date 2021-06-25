/* Authors: R. McCloskey and Joseph Demich
** Collaborations: None.              
*/
import java.util.Random;

/* An instance of this class moedles a composite regular expression 
** whose main operator is concatenation.
*/
public class RegExprConcat extends RegularExpression {

   // instance variables
   // ------------------

   private RegularExpression alpha, beta;

   // constructor
   // -----------

   /* Establishes this regular expression as being the one obtained by
   ** applying the concatenation operator to the given regular expressions.
   */
   public RegExprConcat(RegularExpression first,
                        RegularExpression second) {
      alpha = first;
      beta = second;
   }


   // observers
   // ---------

   /* Reports whether or not the given string is a member of the language
   ** represented by this regular expression.
   ** A string x is a member of L(alpha.beta) iff there exist strings 
   ** y and z such that 
   ** (1) x = yz,
   ** (2) y is a member of L(alpha), and 
   ** (3) z is a member of L(beta).
   */
   @Override
   public boolean isMember(String x) {
      boolean member = false;
      String firstSeg, lastSeg;
      
      for(int i = 0; i < x.length(); i++){
         firstSeg = x.substring(0, i);
         lastSeg = x.substring(i);
         if(alpha.isMember(firstSeg) && beta.isMember(lastSeg)){
            member = true;
         }
      }
      
      return member;
   }

   @Override
   public boolean isFinite() {
      String newExpression = alpha.toString() + beta.toString();
      boolean noStarFound = true;
      
      for(int i = 0; i < newExpression.length(); i++){
         if(newExpression.charAt(i) == RegExprSymbols.STAR_OP){
            noStarFound = false;
         }
      }
      return noStarFound;
   }

   @Override
   public boolean isEmpty() {
      String newExpression = alpha.toString() + beta.toString();
      boolean empty = true;
      
      if(newExpression.length() > 0){
         empty = false;
      }
      
      return empty;
   }

   @Override
   public int minLength() {
      return alpha.minLength() + beta.minLength();
   }

   @Override
   public int maxLength() {
      return alpha.maxLength() + beta.maxLength();
   }

   @Override
   public String randomMember(Random rand) {
      return alpha.randomMember(rand) + beta.randomMember(rand);
   }

   @Override
   public String toString() {
      return alpha.toString() + RegExprSymbols.CONCAT_OP + beta.toString();
   }
   
   @Override
   public RegularExpression reverse() {
      return new RegExprConcat(beta.reverse(), alpha.reverse());
   }
}