/* Author: R. McCloskey and Joseph Demich
** Collaborations: None.           
*/
import java.util.Random;

/* An instance of this class represents a composite regular expression 
** whose main operator is Kleene/star closure.
*/
public class RegExprStar extends RegularExpression {

   // instance variable
   // -----------------

   private RegularExpression r;

   // constructor
   // -----------

   /* Establishes this regular expression as being the one obtained by
   ** applying the Kleene/star operator to the given regular expression.
   */
   public RegExprStar(RegularExpression regExpr) { 
      r = regExpr;
   }


   // observer
   // --------

   /* Reports whether or not the given string is a member of the language
   ** represented by this regular expression.
   ** A string x is in the language of regular expression r^* iff x is the
   ** empty string or there exist non-empty strings y and z such that x = yz,
   ** y is a member of L(r), and z is a member of L(r^*).
   */
   @Override
   public boolean isMember(String x) {
      String partial = "";
      String lookAhead = "";
      
      if(r.isMember(x) || x.equals("N") || x.equals("")){
         return true;
      }
      else{
         for(int i = 1; i <= x.length()-1; i++){
            partial = x.substring(0, i);
            lookAhead = x.substring(0, i+1);
            
            
            if(r.isMember(partial) && !r.isMember(lookAhead)){
               if(i == x.length()-1){
                  return true;
               }
               else{
                  return r.isMember(x.substring(i));
               }
            }
         }
         return false;
      }
   }

   @Override
   public boolean isFinite() { 
      return false;
   }

   @Override
   public boolean isEmpty() { 
      return r.isEmpty();
   }

   @Override
   public int minLength() { 
      if(r.isEmpty()){
         return -1;
      }
      else{
         return 0;
      }
   }

   @Override
   public int maxLength() {
      return -1;
   }

   @Override
   public String randomMember(Random rand) {
      final int MAX_REPETITIONS = 6;
      if (r.isEmpty()) { return ""; }
      else {
         StringBuilder result = new StringBuilder("");
         int k = rand.nextInt(MAX_REPETITIONS + 1);
         for (int i=0; i != k; i++) {
            result.append(r.randomMember(rand));
         }
         return result.toString();
      }
   }

   @Override
   public String toString() {
      String rImage = r.toString();
      if (rImage.length() > 1) { 
         rImage = '(' + rImage + ')';
      }
      return rImage + RegExprSymbols.STAR_OP;
   }
   
   @Override
   public RegularExpression reverse() {
      return new RegExprStar(r.reverse());
   }
}