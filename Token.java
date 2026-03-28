/**
 * Token.java — Person 1
 *
 * Represents a single token produced by the Tokenizer.
 *
 * ── Person 3 (GUI) usage ──────────────────────────────────────────
 *   token.getLexeme()   the raw text        e.g.  "int", "x", "42"
 *   token.getType()     the token category  e.g.  "KEYWORD"
 *   token.toString()    "int                KEYWORD"  (for console)
 * ─────────────────────────────────────────────────────────────────
 */
public class Token {

    private String lexeme;
    private String type;

    public Token(String lexeme, String type) {
        this.lexeme = lexeme;
        this.type   = type;
    }

    /** The raw matched text — e.g. "int", "myVar", "==", "42" */
    public String getLexeme() { return lexeme; }

    /**
     * The token category. Possible values:
     *   KEYWORD    IDENTIFIER    INTEGER    FLOAT
     *   OPERATOR   DELIMITER     UNKNOWN
     */
    public String getType() { return type; }

    /** Formatted string — useful for console output and debugging */
    @Override
    public String toString() {
        return String.format("%-20s %s", lexeme, type);
    }
}