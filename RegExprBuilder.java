/* RegExprBuilder.java
** This class has static methods for checking the syntactic validity of 
** a given regular expression and for translating a regular expression
** into an instance of the class RegularExpression.  Such an object
** can be used for distinguishing between strings that are members of
** the language described by the regular expression and those that are
** not.
*/
public class RegExprBuilder {

   // class constants
   // ---------------
   private static final int OPERAND_CODE = 0;
   private static final int STAR_OP_CODE = 1;
   private static final int BINARY_OP_CODE = 2;
   private static final int LEFT_PAREN_CODE = 3;

   /* Reports whether or not the given string is a syntactically valid
   ** regular expression.
   */
   public static boolean isValid(String s) {
      RegExprTokenizer tokenizer = new RegExprTokenizer(s);
      Stack<Integer> stack = new StackViaArray<Integer>();
      boolean goodSoFar = true;
      while (goodSoFar && tokenizer.hasNext()) {
         //System.out.printf("Next token is $%s$", 
         //                  s.substring(tokenizer.nextStart(),
         //                              tokenizer.nextEnd())
         //                 );
         if (tokenizer.hasNextLeftParen()) { 
            //System.out.println("  ... left paren");
            goodSoFar = handleLeftParen(stack, tokenizer);
         }
         else if (tokenizer.hasNextWord() || 
                  tokenizer.hasNextLambda() ||
                  tokenizer.hasNextNullSet()) { 
            //System.out.println("  ... word");
            goodSoFar = handleWord(stack, tokenizer);
         }
         else if (tokenizer.hasNextUnionOp() || tokenizer.hasNextConcatOp()) {
            //System.out.println("  ... binary operator");
            goodSoFar = handleBinaryOp(stack, tokenizer);
         }
         else if (tokenizer.hasNextStarOp()) {
            //System.out.println("  ... star operator");
            goodSoFar = handleStarOp(stack, tokenizer);
         }
         else if (tokenizer.hasNextRightParen()) {
            //System.out.println("  ... right paren");
            goodSoFar = handleRightParen(stack, tokenizer);
         }
         else {
            goodSoFar = false;
            int i = tokenizer.nextStart();
            int j = tokenizer.nextEnd();
            System.out.println("Unknown type of token!:" + s.substring(i,j));
         }
         tokenizer.findNextToken();
      }
      if (stack.sizeOf() != 1) {
         goodSoFar = false;
         System.out.printf("Error: Stack ends with size %d\n", stack.sizeOf());
      }
      else if (stack.topOf() != OPERAND_CODE) {
         goodSoFar = false;
         System.out.println("Error: Stack ends with non-operand at top");
      }
      return goodSoFar;  // && stack.sizeOf() == 1 && stack.topOf() == OPERAND_CODE;
   }

   /* Reports whether, syntactically, it makes sense for a left parenthesis
   ** to be the next token and, if so, pushes one onto the given stack.
   */
   private static boolean handleLeftParen(Stack<Integer> stk,
                                          RegExprTokenizer tok) {
      boolean result;
      if (stk.isEmpty() || 
          stk.topOf() == LEFT_PAREN_CODE ||
          stk.topOf() == BINARY_OP_CODE)
      {
         result = true;
         stk.push(LEFT_PAREN_CODE);
      }
      else {
         System.out.println("Error at '(' at position " + tok.nextStart());
         result = false;
      }
      return result;
   }

   /* Reports whether, syntactically, it makes sense for a word to be the
   ** next token and, if so, takes appropriate action on the given stack.
   */
   private static boolean handleWord(Stack<Integer> stk,
                                     RegExprTokenizer tok) {
      boolean result;
      if (stk.isEmpty() || stk.topOf() == LEFT_PAREN_CODE) {
         result = true;
         stk.push(OPERAND_CODE);
      }
      else if (stk.topOf() == BINARY_OP_CODE) {
         stk.pop();
         if (!stk.isEmpty() &&  stk.topOf() == OPERAND_CODE) {
            result = true;
            stk.pop();
            stk.push(OPERAND_CODE);
         }
         else {
            System.out.println("Error at word at position " + tok.nextStart());
            result = false;
         }
      }
      else {
         System.out.println("Error at word at position " + tok.nextStart());
         result = false;
      }
      return result;
   }

   /* Reports whether, syntactically, it makes sense for a binary operator
   ** to be the next token and, if so, it pushes it onto the stack.
   */
   private static boolean handleBinaryOp(Stack<Integer> stk,
                                         RegExprTokenizer tok) {
      boolean result;
      if (!stk.isEmpty() &&  stk.topOf() == OPERAND_CODE) {
         result = true;
         stk.push(BINARY_OP_CODE);
      }
      else {
         System.out.println("Error at binary operator at position " + 
                            tok.nextStart());
         result = false;
      }
      return result;
   }

   /* Reports whether, syntactically, it makes sense for a star operator
   ** (which is a unary suffix operator) to be the next token and, if so, 
   ** it takes appropriate action on the given stack.
   */
   private static boolean handleStarOp(Stack<Integer> stk,
                                       RegExprTokenizer tok) {
      boolean result;
      if (!stk.isEmpty() &&  stk.topOf() == OPERAND_CODE) {
         stk.pop();
         result = handleWord(stk, tok);
         //result = true;
      }
      else {
         System.out.println("Error at * at position " + tok.nextStart());
         result = false;
      }
      return result;
   }

   /* Reports whether, syntactically, it makes sense for a right parenthesis
   ** to be the next token and, if so, takes appropriate actions on the
   ** given stack.  The stack must have an operand and a left paren, which
   ** are replaced by an operand.
   */
   private static boolean handleRightParen(Stack<Integer> stk,
                                           RegExprTokenizer tok) {
      boolean result;
      if (!stk.isEmpty() && stk.topOf() == OPERAND_CODE) {
         stk.pop();
         if (!stk.isEmpty()  &&  stk.topOf() == LEFT_PAREN_CODE) { 
            //result = true;
            //stk.push(OPERAND_CODE);
            stk.pop();
            result = handleWord(stk, tok);
         }
         else {
            System.out.println("Error at ) at position " + tok.nextStart());
            result = false;
         }
      }
      else {
         System.out.println("Error at ) at position " + tok.nextStart());
         result = false;
      }
      return result;
   }

   /* Returns an instance of the appropriate child class of RegularExpression
   ** corresponding to the given string.
   ** pre: isValid(s)
   */
   public static RegularExpression parse(String s) {
      Stack<RegularExpression> operandStk = new StackViaArray<RegularExpression>();
      Stack<Character> operatorStk = new StackViaArray<Character>();
      RegExprTokenizer tokenizer = new RegExprTokenizer('(' + s + ')');
      
      while (tokenizer.hasNext()) {
         if (tokenizer.hasNextWord()) {
            operandStk.push(new RegExprWord(tokenizer.next()));
         }
         else if (tokenizer.hasNextLambda()) {
            tokenizer.findNextToken();
            operandStk.push(new RegExprWord(""));
         }
         else if (tokenizer.hasNextNullSet()) {
            tokenizer.findNextToken();
            operandStk.push(new RegExprNullSet());
         }
         else if (tokenizer.hasNextLeftParen()) { 
            tokenizer.findNextToken();
            operatorStk.push(RegExprSymbols.LEFT_PAREN);
         }
         else if (tokenizer.hasNextRightParen()) { 
            tokenizer.findNextToken();
            while (operatorStk.topOf() != RegExprSymbols.LEFT_PAREN)
            {
               char operator = operatorStk.pop();
               applyOp(operator, operandStk);
            }
            operatorStk.pop();  // Pop the left parenthesis
         }
         else if (tokenizer.hasNextOperator()) {
            char thisOp = tokenizer.next().charAt(0);
            while (!operatorStk.isEmpty() &&
                   precedenceVal(operatorStk.topOf()) >= precedenceVal(thisOp))
            {
               char operator = operatorStk.pop();
               applyOp(operator, operandStk);
            }
            operatorStk.push(thisOp);
         }
         else {
            assert false : "This should never happen!!";
         }
      }
      assert operandStk.sizeOf() == 1 : "Operand stack size not 1 at end";
      assert operatorStk.isEmpty() : "Operator stack not empty at end";
      return operandStk.topOf();
   }

   // private
   // -------

   /* Applies the given operator to the top one or two operands on 
   ** the given stack (according to whether the operator is unary
   ** or binary, respectively) and replaces that/those operand(s) on
   ** the stack by the resulting regular expression. 
   */
   private static void applyOp(char operator, 
                               Stack<RegularExpression> operandStack) {

      if (operator == RegExprSymbols.UNION_OP) {
         RegularExpression s = operandStack.pop();
         RegularExpression r = operandStack.pop();
         operandStack.push(new RegExprUnion(r,s));
      }
      else if (operator == RegExprSymbols.CONCAT_OP) {
         RegularExpression s = operandStack.pop();
         RegularExpression r = operandStack.pop();
         operandStack.push(new RegExprConcat(r,s));
      }
      else if (operator == RegExprSymbols.STAR_OP) {
         RegularExpression r = operandStack.pop();
         operandStack.push(new RegExprStar(r));
      } 
   }

   private static int precedenceVal(char op) {
      String PRECEDENCE = 
         "" + RegExprSymbols.LEFT_PAREN + RegExprSymbols.UNION_OP + 
              RegExprSymbols.CONCAT_OP + RegExprSymbols.STAR_OP;

      return PRECEDENCE.indexOf(op);
   }
   
}