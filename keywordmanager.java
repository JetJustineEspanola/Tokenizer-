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
            String[] fallback = { "int", "float", "if", "else", "return", "while", "for", "do", "void", "boolean" };
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

    /**
     * Returns the number of loaded keywords.
     * @return keyword count
     */
    public static int getKeywordCount() {
        return count;
    }

    /**
     * Returns all loaded keywords as an array.
     * @return array of keyword strings
     */
    public static String[] getKeywords() {
        String[] result = new String[count];
        System.arraycopy(keywords, 0, result, 0, count);
        return result;
    }

    /**
     * Adds a new keyword if it doesn't already exist.
     * @param keyword the keyword to add
     * @return true if added, false if already present
     */
    public static boolean addKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return false;
        keyword = keyword.trim();
        if (isKeyword(keyword)) return false;
        
        String[] newKeywords = new String[count + 1];
        System.arraycopy(keywords, 0, newKeywords, 0, count);
        newKeywords[count] = keyword;
        keywords = newKeywords;
        count++;
        return true;
    }

    /**
     * Removes a keyword if it exists.
     * @param keyword the keyword to remove
     * @return true if removed, false if not found
     */
    public static boolean removeKeyword(String keyword) {
        if (keyword == null) return false;
        
        for (int i = 0; i < count; i++) {
            if (keywords[i].equals(keyword)) {
                String[] newKeywords = new String[count - 1];
                System.arraycopy(keywords, 0, newKeywords, 0, i);
                System.arraycopy(keywords, i + 1, newKeywords, i, count - i - 1);
                keywords = newKeywords;
                count--;
                return true;
            }
        }
        return false;
    }

    /**
     * Saves current keywords to a file.
     * @param filename path to save keywords
     * @return true if successful, false otherwise
     */
    public static boolean saveKeywords(String filename) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
            for (int i = 0; i < count; i++) {
                writer.println(keywords[i]);
            }
            return true;
        } catch (IOException e) {
            System.err.println("[KeywordManager] Error saving keywords: " + e.getMessage());
            return false;
        }
    }
}