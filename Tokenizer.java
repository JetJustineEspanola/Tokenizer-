import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer.java — Person 1
 *
 * Core lexical analyzer engine. No external dependencies — drop
 * Tokenizer.java and Token.java into any project and it works.
 *
 * ── Person 3 (GUI) — how to use ──────────────────────────────────
 *
 *   Tokenizer tokenizer = new Tokenizer();
 *   List<Token> tokens  = tokenizer.tokenize(inputString);
 *
 *   for (Token t : tokens) {
 *       t.getLexeme();   // the raw text  e.g. "int"
 *       t.getType();     // the category  e.g. "KEYWORD"
 *   }
 *
 * ── Person 2 (Keywords) — how to plug in ─────────────────────────
 *
 *   When KeywordManager is ready, find the isKeyword() method below
 *   and replace the body with:
 *
 *       return KeywordManager.isKeyword(word);
 *
 *   That is the ONLY change needed to integrate Person 2's work.
 * ─────────────────────────────────────────────────────────────────
 */
public class Tokenizer {

    // ── Token type constants — use these strings in the GUI ────────
    public static final String KEYWORD    = "KEYWORD";
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String INTEGER    = "INTEGER";
    public static final String FLOAT      = "FLOAT";
    public static final String OPERATOR   = "OPERATOR";
    public static final String DELIMITER  = "DELIMITER";
    public static final String UNKNOWN    = "UNKNOWN";

    // ── Multi-char operators — checked before single-char ──────────
    private static final String[] MULTI_OPS = { "==", "!=", "<=", ">=" };

    // ─────────────────────────────────────────────────────────────────
    // PUBLIC API — Person 3 only needs this one method
    // ─────────────────────────────────────────────────────────────────

    /**
     * Tokenize a string of source code.
     *
     * @param input  any line or block of code
     * @return       ordered list of Token objects, one per lexeme
     */
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;

        while (i < input.length()) {
            char c = input.charAt(i);

            // ── 1. Skip whitespace ─────────────────────────────────
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // ── 2. Identifier or keyword ───────────────────────────
            if (isLetter(c)) {
                StringBuilder buf = new StringBuilder();
                while (i < input.length() && (isLetter(input.charAt(i)) || isDigit(input.charAt(i)))) {
                    buf.append(input.charAt(i++));
                }
                String word = buf.toString();
                tokens.add(new Token(word, isKeyword(word) ? KEYWORD : IDENTIFIER));
                continue;
            }

            // ── 3. Number — integer or float ───────────────────────
            if (isDigit(c)) {
                StringBuilder buf = new StringBuilder();
                boolean isFloat = false;
                while (i < input.length() && (isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    if (input.charAt(i) == '.') isFloat = true;
                    buf.append(input.charAt(i++));
                }
                tokens.add(new Token(buf.toString(), isFloat ? FLOAT : INTEGER));
                continue;
            }

            // ── 4. Multi-character operators (==, !=, <=, >=) ──────
            boolean matched = false;
            for (String op : MULTI_OPS) {
                if (input.startsWith(op, i)) {
                    tokens.add(new Token(op, OPERATOR));
                    i += op.length();
                    matched = true;
                    break;
                }
            }
            if (matched) continue;

            // ── 5. Single-character operator ───────────────────────
            if (isOperator(c)) {
                tokens.add(new Token(String.valueOf(c), OPERATOR));
                i++;
                continue;
            }

            // ── 6. Delimiter ───────────────────────────────────────
            if (isDelimiter(c)) {
                tokens.add(new Token(String.valueOf(c), DELIMITER));
                i++;
                continue;
            }

            // ── 7. Unknown ─────────────────────────────────────────
            tokens.add(new Token(String.valueOf(c), UNKNOWN));
            i++;
        }

        return tokens;
    }

    // ─────────────────────────────────────────────────────────────────
    // KEYWORD CHECK
    // Person 2: replace the body of this method with:
    //     return KeywordManager.isKeyword(word);
    // ─────────────────────────────────────────────────────────────────
    private boolean isKeyword(String word) {
        String[] keywords = { "int", "float", "if", "else", "return", "while", "for", "do", "void", "boolean" };
        for (String k : keywords) {
            if (k.equals(word)) return true;
        }
        return false;
    }

    // ─────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────
    private boolean isLetter(char c)    { return Character.isLetter(c) || c == '_'; }
    private boolean isDigit(char c)     { return Character.isDigit(c); }
    private boolean isOperator(char c)  { return "+-*/=<>!".indexOf(c) != -1; }
    private boolean isDelimiter(char c) { return ";,(){}[]".indexOf(c) != -1; }
}