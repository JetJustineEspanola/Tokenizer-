// ============================================================
// KeywordManager.java — Person 2
// ============================================================
// This class loads keywords from a .txt file and provides
// a method that Tokenizer.java calls to check if a word is a keyword.
//
// YOUR TASKS:
//
//   1. Import these at the top:
//        import java.io.BufferedReader;
//        import java.io.FileReader;
//        import java.io.IOException;
//
//   2. Declare a private static String array to store keywords:
//        private static String[] keywords;
//      and a private static int to track how many were loaded:
//        private static int count = 0;
//
//   3. Write a static method:
//
//        public static void loadKeywords(String filename)
//
//        Steps inside:
//          a. Open the file using FileReader and BufferedReader
//          b. Count how many lines the file has (first pass or use a list)
//             TIP: use an ArrayList<String> to collect lines, then
//                  convert to array at the end
//          c. Read each line with br.readLine()
//          d. Trim whitespace from each line: line.trim()
//          e. Skip blank lines (check if line is empty after trim)
//          f. Store each valid line into your keywords array
//          g. Close the reader when done
//          h. Wrap everything in try-catch (IOException)
//             and print an error message if the file is not found
//
//   4. Write the method Tokenizer.java will call:
//
//        public static boolean isKeyword(String word)
//
//        Steps inside:
//          a. Loop through the keywords array up to count
//          b. Compare each element to word using .equals()
//          c. Return true if a match is found, false otherwise
//
//   5. (Optional) Write a helper to print all loaded keywords:
//
//        public static void printKeywords()
//          → loop and print each keyword, one per line
//
// INTEGRATION NOTE for Person 1:
//   Once this file is ready, Person 1 opens Tokenizer.java and
//   replaces the body of their isKeyword() method with:
//       return KeywordManager.isKeyword(word);
//   and adds this call somewhere before tokenizing:
//       KeywordManager.loadKeywords("keywords.txt");
//
// INTEGRATION NOTE for Main.java:
//   Add this line at the top of main() before tokenizing:
//       KeywordManager.loadKeywords("keywords.txt");
// ============================================================

/*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KeywordManager {

    // Step 2 — declare your fields here


    // Step 3 — loadKeywords(String filename)


    // Step 4 — isKeyword(String word)


    // Step 5 — printKeywords() (optional)

}
*/