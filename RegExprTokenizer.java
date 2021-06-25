import java.util.Iterator;

/* An instance of this class finds successive tokens in a
** regular expression supplied to the constructor.
*/
public class RegExprTokenizer implements Iterator<String> {

   private static final String OPERATORS =
      "" + RegExprSymbols.UNION_OP + RegExprSymbols.CONCAT_OP +
           RegExprSymbols.STAR_OP + RegExprSymbols.LEFT_PAREN +
           RegExprSymbols.RIGHT_PAREN;

   private static final String NON_LETTERS = 
      OPERATORS + RegExprSymbols.LAMBDA + RegExprSymbols.NULL_SET;

/*
   private static final int NO_MORE_TOKENS_CODE = -1;
   private static final int UNION_CODE = NON_LETTERS.indexOf(UNION_OPERATOR);
   private static final int CONCAT_CODE = NON_LETTERS.indexOf(CONCAT_OPERATOR);
   private static final int STAR_CODE = NON_LETTERS.indexOf(STAR_OPERATOR);
   private static final int LEFT_PAREN_CODE = NON_LETTERS.indexOf(LEFT_PAREN);
   private static final int RIGHT_PAREN_CODE = NON_LETTERS.indexOf(RIGHT_PAREN);
   private static final int LAMBDA_CODE = NON_LETTERS.indexOf(LAMBDA);
   private static final int NULL_SET_CODE = NON_LETTERS.indexOf(NULL_SET);
   private static final int WORD_CODE = -2;
*/

   // instance variables
   // ------------------

   private String rexpr;
   private final int N;       // length of the rexpr
   private int start, end;    // rexpr[start..end) is the next token
   private int nextTokenCode; // what kind of token is it

   // constructor
   // -----------

   public RegExprTokenizer(String s) { 
      rexpr = s;
      N = rexpr.length();   
      end = 0;
      findNextToken();
   }

   // observers
   // ---------

   /* Reports whether there are any more tokens to be iterated over.
   */
   public boolean hasNext() { return start != N; }

   /* Returns the position, within the given regular expression, at which
   ** the next token begins, in the case hasNext().  In the case !hasNext(),
   ** returns the length of the regular expression.
   */
   public int nextStart() { return start; }

   /* Returns the position, within the given regular expression, that
   ** immediately follows the next token ends, in the case hasNext().
   ** In the case !hasNext(), returns the length of the regular expression.
   */
   public int nextEnd() { return end; }


   /* Reports whether the next token is a regular expression operator.
   ** pre: hasNext()
   */
   public boolean hasNextOperator() {
      return start + 1 == end &&
             OPERATORS.indexOf(rexpr.charAt(start)) != -1;
   }

   /* Reports whether the next token is the union operator.
   ** pre: hasNext()
   */
   public boolean hasNextUnionOp() { 
      return start + 1 == end  &&  
             RegExprSymbols.UNION_OP == rexpr.charAt(start);
   }  

   /* Reports whether the next token is the concatenation operator.
   ** pre: hasNext()
   */
   public boolean hasNextConcatOp() {
      return start + 1 == end  &&  
             RegExprSymbols.CONCAT_OP == rexpr.charAt(start);
   }

   /* Reports whether the next token is the Kleene/star operator.
   ** pre: hasNext()
   */
   public boolean hasNextStarOp() {
      return start + 1 == end  &&  
             RegExprSymbols.STAR_OP == rexpr.charAt(start);
   }

   /* Reports whether the next token is a left parenthesis.
   ** pre: hasNext()
   */
   public boolean hasNextLeftParen() {
      return start + 1 == end  &&  
             RegExprSymbols.LEFT_PAREN == rexpr.charAt(start);
   }

   /* Reports whether the next token is a right parenthesis.
   ** pre: hasNext()
   */
   public boolean hasNextRightParen() {
      return start + 1 == end  &&  
             RegExprSymbols.RIGHT_PAREN == rexpr.charAt(start);
   }

   /* Reports whether the next token is the symbol representing
   ** the null/empty set.
   ** pre: hasNext()
   */
   public boolean hasNextNullSet() {
      return start + 1 == end  &&  
             RegExprSymbols.NULL_SET == rexpr.charAt(start);
   }

   /* Reports whether the next token is the symbol representing the
   ** empty string (commonly represented by the Greek letter lambda).
   ** pre: hasNext()
   */
   public boolean hasNextLambda() {
      return start + 1 == end  &&  
             RegExprSymbols.LAMBDA == rexpr.charAt(start);
   }

   /* Reports whether the next token is a sequence of (one or more) letters.
   ** (e.g., "b", "abbab") (in which the concatenation operators are implicit).
   ** pre: hasNext()
   */
   public boolean hasNextWord() { 
      return NON_LETTERS.indexOf(rexpr.charAt(start)) == -1;
   }

   // observer/mutator
   // ----------------

   /* Returns the next token.
   ** pre: hasNext()
   */
   public String next() {
      String result = rexpr.substring(start, end);
      findNextToken();
      return result;
   }

   /* Updates start, end, and nextTokenCode so that their values
   ** reflect the token following the "current" one, if there is one.
   */
   public void findNextToken() {
      start = end;
      advancePastWhiteSpace();
      end = start;
      if (start == N) {  
         // there are no more tokens; do nothing
      }
      else {
         char chAtStart = rexpr.charAt(start);
         //System.out.printf("chAtStart is $%c$;", chAtStart);
         nextTokenCode = NON_LETTERS.indexOf(chAtStart);
         //System.out.println("nextTokenCode is %d.", chAtStart);
         
         if (nextTokenCode != -1) { // We have a one-char token
            end = start + 1;
         }
         else {   // We have a word; find the end of it
            end = start + 1;
            advanceToEndOfWord();  // advance 'end' to end of current word
         }
         //System.out.println("Next token is " + rexpr.substring(start,end));
      }
   }

   // private
   // -------

   /* Increases 'end' until it hits the end of 'rexpr' or it points to a
   ** character that is whitespace or a one-char token symbol.
   */
   private void advanceToEndOfWord() {
      boolean keepGoing = true;
      while (keepGoing) {
         if (end == N) { keepGoing = false; }
         else {
            char ch = rexpr.charAt(end);
            //System.out.printf("Processing character $%c$;", ch);
            if (Character.isWhitespace(ch)) { 
               //System.out.println("It is a whitespace char.");
               keepGoing = false;
            }
            else if (isNonLetter(ch)) { 
               //System.out.println("It is a non-letter token.");
               keepGoing = false; 
            }
            else { 
               //System.out.println("It is a letter.");
               end = end + 1;
            }
         }
      }
   }

   /* Increases 'start' until it hits the end of string or a non-whitespace
   ** character.
   ** pre: 0 <= start < N
   */
   private void advancePastWhiteSpace() {
      while (start != N  &&  Character.isWhitespace(rexpr.charAt(start))) {
         start++;
      }
   }

   private boolean isNonLetter(char c) {
      return NON_LETTERS.indexOf(c) != -1;
   }

}