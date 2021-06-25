/* CMPS 260  Spring 2020  Prog. Assg. #1  Regular Expressions
** Author: R. McCloskey and Joseph Demich
** Collaborations: None.
** Known defects: None.               
*/
import java.util.Random;

/* An instance of this class models a composite regular expression 
** whose main operator is union (i.e. of the form alpha + beta).
*/
public class RegExprUnion extends RegularExpression {

   // instance variables
   // ------------------

   private RegularExpression alpha, beta;  // This regular expression is 
                                           // alpha + beta

   // constructor
   // -----------

   /* Establishes this regular expression as being the one obtained by
   ** applying the union operator to the given regular expressions.
   */
   public RegExprUnion(RegularExpression first,
                       RegularExpression second) {
      alpha = first;
      beta = second;
   }

   // observer
   // --------

   /* Reports whether or not the given string is a member of the language
   ** represented by this regular expression.
   ** A string x is a member of L(alpha + beta) iff either x is a member of 
   ** L(alpha) or x is a member of L(beta).
   */
   @Override
   public boolean isMember(String x) {
      boolean member = false;
      
      if(alpha.isMember(x) || beta.isMember(x)){
         member = true;
      }
      else if(x.equals("") || x.equals("N")){
         member = true;
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
      
      if(newExpression.length() > 0 || newExpression.equals("N") || newExpression.equals("NN")){
         empty = false;
      }
      
      return empty;
   }

   @Override
   public int minLength() {
      int length = 0;
      
      if(alpha.toString().equals("N")){
         length = beta.minLength();
      }
      else if(beta.toString().equals("N")){
         length = alpha.minLength();
      }
      else{
         length = Math.min(alpha.minLength(), beta.minLength());
      }

      return length;
   }

   @Override
   public int maxLength() {
      return Math.max(alpha.maxLength(), beta.maxLength());
   }

   @Override
   public String randomMember(Random rand) {
      if (rand.nextBoolean()) 
         { return alpha.randomMember(rand); }
      else
         { return beta.randomMember(rand); }
   }

   @Override
   public String toString() {
      return '(' + alpha.toString() + 
             ' ' + RegExprSymbols.UNION_OP + ' ' +
             beta.toString() + ')';
   }
   
   @Override
   public RegularExpression reverse() {
      return new RegExprUnion(alpha.reverse(), beta.reverse());
   }
}
