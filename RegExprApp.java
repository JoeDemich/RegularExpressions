import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;

/* RegExprApp.java
** Java application that, making use of several classes that together
** provide an implementation of regular expressions, responds to commands
** entered by a user who, after entering a regular expression, wishes to
** obtain information about the language that it represents.  Specifically, 
** this application reports whether that language is empty vs. nonempty, 
** finite vs. infinite, the length of the shortest member(s), the length of 
** the longest member(s) (if finite), and whether a specified string is a 
** member.  There is also a command by which the user can request that a 
** "random" member of the language be generated.
*/
public class RegExprApp {

   private static char PAD = '|';
   private static char QUIT = 'q';
   private static char HELP = 'h';
   private static char NEW_REXPR = 'n';
   private static char DISPLAY_REXPR = 'd';
   private static char PRINT_STATS = 's';
   private static char GENERATE_RANDOM = 'g';
   private static char MEMBERSHIP_TEST = 'm';
   private static char REVERSE = 'r';

   private static String NO_EXPR = "There is no current regular expression.";

   private static Scanner input;
   private static boolean echo;
   private static Random rand = new Random();

   public static void main(String[] args) {
      input = getScanner(args);
      RegularExpression regExpr = null;
      boolean keepGoing = true;

      printHelp();
      do {
         String commandLine = getString("\n> ");
         char command = Character.toLowerCase(commandLine.charAt(0));
         String commandArg;
         //if (commandLine.length() > 1) 
         //   { 
         commandArg = commandLine.substring(1).trim(); 
         // }
         //else
         //   { commandArg = ""; }

         if (command == QUIT) { keepGoing = false; }
         else if (command == HELP) { printHelp(); }
         else if (command == NEW_REXPR) {
            String rexprStr;
            if (commandArg.length() == 0) {
               rexprStr = getString("Enter regular expression: ");
            }
            else {
               rexprStr = commandArg;
            }
            if (!RegExprBuilder.isValid(rexprStr)) {
               System.out.println("Invalid syntax");
            }
            else {
               regExpr = RegExprBuilder.parse(rexprStr);
               System.out.printf("New regular expression is %s\n", regExpr);
            }
         }
         else if (command == DISPLAY_REXPR) {
            if (regExpr == null) { System.out.println(NO_EXPR); }
            else { System.out.println(regExpr); }
         }
         else if (command == PRINT_STATS) {
            if (regExpr == null) { System.out.println(NO_EXPR); }
            else { printStats(regExpr); }
         }
         else if (command == GENERATE_RANDOM) {
            if (regExpr == null) { System.out.println(NO_EXPR); }
            else {
               Random rdm;
               if (commandArg.length() == 0)  // use "resident" Random object
                  { rdm = rand; }
               else   // command argument used as seed for Random object
                  { rdm = new Random(Integer.parseInt(commandArg)); }
               System.out.printf("Random member: %c%s%c\n", 
                                 PAD, regExpr.randomMember(rdm), PAD);
            }
         }
         else if (command == MEMBERSHIP_TEST) {
            if (regExpr == null) { System.out.println(NO_EXPR); }
            else { 
               String answer = regExpr.isMember(commandArg) ? "" : "NOT ";
               System.out.printf("The string %c%s%c is %sa member.\n",
                                 PAD, commandArg, PAD, answer);
            }
         }
         else if (command == REVERSE) {
            if (regExpr == null) { System.out.println(NO_EXPR); }
            else { 
               RegularExpression reversal = regExpr.reverse();
               System.out.printf("Reverse is %s\n", reversal);
            }        
     
         }
         else {
            System.out.println("Unrecognized command; enter 'h' for help");
         }
      }
      while (keepGoing);

      System.out.println("Goodbye.");
   }

   private static void printHelp() {
      System.out.println("Commands:");
      System.out.println("---------");
      int padLen = 3;
      printCommand(QUIT + ": to quit.", padLen);
      printCommand(HELP + ": for this list.", padLen);
      printCommand(NEW_REXPR + " <regular expression>: " +
                   "to establish a new regular expression.", padLen);
      printCommand("Example: " + NEW_REXPR + " aba.(ba)* + bba", padLen+2);
      printCommand(DISPLAY_REXPR + ": to display the current regular expression",
                   padLen);
      printCommand(MEMBERSHIP_TEST + " <string>:" +
                   " to test string for membership", padLen);
      printCommand("Example: " + MEMBERSHIP_TEST + " bbabaab", padLen+2);
      printCommand(PRINT_STATS + ": to display stats about the " +
                   "current regular expression.", padLen);
      printCommand(GENERATE_RANDOM + " [seed]: to display a random member " +
                   "of the language.", padLen);
      printCommand(REVERSE + ": to display reverse of current rexpr.", padLen);
   }

   private static void printCommand(String s, int padLength) {
      for (int i=0; i != padLength; i++) { System.out.print(' '); }
      System.out.println(s);
   }

   private static void printStats(RegularExpression r) {
      System.out.printf("Image: %s\n", r.toString());
      if (r.isEmpty()) { 
         System.out.println("Has no members.");
      }
      else {
         System.out.printf("Shortest member has length %d\n", r.minLength());
         if (r.isFinite()) {
            System.out.printf("Longest member has length %d\n", r.maxLength());
         }
         else {
            System.out.println("Has infinitely many members.");
         }
         //System.out.printf("Random member: %s\n", r.randomMember(rand));
      }
   }

   /* Displays the given prompt and returns the next line of input,
   ** unless it is empty, in which case it repeats.
   */
   private static String getString(String prompt) {
      String response = "";
      boolean keepGoing = true;
      do {
         System.out.print(prompt);
         response = input.nextLine().trim();
         if (echo) { System.out.println(response); }
         if (response.length() == 0) {
            System.out.println("Only whitespace entered; try again...");
         }
         else {
            keepGoing = false;
         }
      } while (keepGoing);
      return response;
   }

   /* Returns a new Scanner attached to either System.in or to a file,
   ** the former if the given array has length zero and the latter if not.
   ** In the latter case, the file in question is that named by args[0].
   */
   private static Scanner getScanner(String[] args) {
      Scanner result;
      if (args.length == 0) { 
         echo = false;
         result = new Scanner(System.in);
      }
      else { 
         try {
            echo = true;
            result = new Scanner(new File(args[0]));
         }
         catch (FileNotFoundException e) {
            result = null;
            System.out.printf("File %s not found; aborting execution.\n",
                              args[0]);
            System.exit(0);
         }
      }
      return result;
   }

}