// ============================================================
// KeywordManager.java — Person 2
// ============================================================
// Loads keywords from a .txt file (one keyword per line) and
// provides the isKeyword(word) method called by Tokenizer.java.
//
// INTEGRATION:
//   In Tokenizer.java, replace the body of isKeyword() with:
//       return KeywordManager.isKeyword(word);
//
//   In Main.java / GUI.java, call before tokenizing:
//       KeywordManager.loadKeywords("keywords.txt");
// ============================================================

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KeywordManager {

    // Stores keywords loaded from the file
    private static String[] keywords = new String[0];
    private static int count = 0;

    /**
     * Loads keywords from a plain-text file (one per line).
     * Blank lines and whitespace are ignored.
     *
     * @param filename  path to the keywords file, e.g. "keywords.txt"
     */
    public static void loadKeywords(String filename) {
        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("[KeywordManager] Warning: could not load '" + filename + "'. " + e.getMessage());
            System.err.println("[KeywordManager] Falling back to built-in keywords.");
            // Fallback — mirrors Tokenizer's original built-in list
            String[] fallback = { "i    nt", "float", "if", "else", "return", "while", "for", "do", "void", "boolean" };
            keywords = fallback;
            count    = fallback.length;
            return;
        }

        keywords = list.toArray(new String[0]);
        count    = keywords.length;
        System.out.println("[KeywordManager] Loaded " + count + " keyword(s) from '" + filename + "'.");
    }

    /**
     * Returns true if the given word is in the loaded keyword list.
     *
     * @param word  the identifier to test
     * @return      true if it is a keyword, false otherwise
     */
    public static boolean isKeyword(String word) {
        for (int i = 0; i < count; i++) {
            if (keywords[i].equals(word)) return true;
        }
        return false;
    }

    /**
     * (Optional) Prints all loaded keywords to stdout — useful for debugging.
     */
    public static void printKeywords() {
        System.out.println("[KeywordManager] Keywords (" + count + "):");
        for (int i = 0; i < count; i++) {
            System.out.println("  " + (i + 1) + ". " + keywords[i]);
        }
    }
}