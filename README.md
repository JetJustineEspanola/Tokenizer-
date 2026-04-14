# Lexical Analyzer (Tokenizer)

A Java-based lexical analyzer that tokenizes source code into categorized tokens (keywords, identifiers, operators, delimiters, numbers, etc.). Includes both a **command-line interface** and a **modern graphical user interface**.

---

## Features

- **Token Recognition**: Identifies 7 token types
  - `KEYWORD` - Language reserved words (int, float, if, else, etc.)
  - `IDENTIFIER` - Variable/function names
  - `INTEGER` - Whole numbers
  - `FLOAT` - Decimal numbers
  - `OPERATOR` - Mathematical/logical operators (+, -, *, /, =, ==, !=, etc.)
  - `DELIMITER` - Punctuation (;, ,, (, ), {, }, [, ])
  - `UNKNOWN` - Unrecognized characters

- **Dynamic Keyword Management**: Add, remove, and save custom keywords at runtime
- **File Operations**: Open, save, and create source code files
- **Dual Interface**: CLI for quick tests, GUI for full-featured analysis
- **Real-time Statistics**: Token counts by category
- **Modern Dark Theme**: Easy on the eyes with syntax-highlighted output

---

## Project Structure

```
Tokenizer-/
├── Token.java           # Data class representing a single token
├── Tokenizer.java       # Core lexical analysis engine
├── KeywordManager.java  # Loads/manages keywords from file
├── keywords.txt         # Keyword definitions (one per line)
├── GUI.java             # Graphical user interface
├── Main.java            # Command-line interface
└── README.md            # This file
```

---

## File Descriptions

### `Token.java`
Simple data class that holds token information:
- **lexeme** - The actual text (e.g., `"int"`, `"x"`, `"42"`)
- **type** - The category (e.g., `"KEYWORD"`, `"IDENTIFIER"`)

### `Tokenizer.java`
The core lexical analyzer engine:
- Scans input character by character
- Identifies tokens using pattern matching
- Delegates keyword checking to `KeywordManager`
- Returns a list of `Token` objects

### `KeywordManager.java`
Manages the keyword dictionary:
- Loads keywords from `keywords.txt` at startup
- Provides `isKeyword(word)` for token classification
- Supports runtime add/remove operations
- Can save modified keyword list back to file
- Falls back to built-in keywords if file is missing

### `keywords.txt`
Plain text file with one keyword per line:
```
int
float
if
else
return
while
for
do
void
boolean
class
public
private
static
new
null
true
false
this
super
```

### `GUI.java`
Full-featured graphical interface:
- Code editor with line numbers
- Token output table with color-coded types
- File menu (New, Open, Save, Save As)
- Keywords menu (Manage, Reload)
- Statistics sidebar
- Keyboard shortcuts (Ctrl+Enter, Ctrl+O, Ctrl+S, Ctrl+K)

### `Main.java`
Command-line interface:
- Interactive REPL-style input
- Displays tokens in console table format
- Type `quit` to exit

---

## Compilation

Compile all files at once:

```bash
javac Token.java Tokenizer.java KeywordManager.java GUI.java Main.java
```

Or compile individually:

```bash
javac Token.java
javac Tokenizer.java
javac KeywordManager.java
javac GUI.java
javac Main.java
```

---

## Usage

### Graphical Interface (Recommended)

```bash
java GUI
```

**GUI Features:**
| Action | Method |
|--------|--------|
| Run tokenizer | Click "Run Tokenizer" or press `Ctrl+Enter` |
| Clear all | Click "Clear" button |
| Open file | `File` → `Open...` or `Ctrl+O` |
| Save file | `File` → `Save` or `Ctrl+S` |
| Manage keywords | `Keywords` → `Manage Keywords...` or `Ctrl+K` |
| Exit | `File` → `Exit` |

### Command-Line Interface

```bash
java Main
```

**Example Session:**
```
========================================
   LEXICAL ANALYZER (TOKENIZER)
========================================

Enter code (or 'quit' to exit): int x = 10;

================== TOKENS ==================
LEXEME              | TYPE
--------------------+--------------------
int                 KEYWORD
x                   IDENTIFIER
=                   OPERATOR
10                  INTEGER
;                   DELIMITER
==========================================
Total tokens: 5

Enter code (or 'quit' to exit): quit

Exiting tokenizer. Goodbye!
```

---

## Token Type Colors (GUI)

| Type | Color |
|------|-------|
| KEYWORD | Pink/Red |
| IDENTIFIER | Light Blue |
| INTEGER | Green |
| FLOAT | Mint |
| OPERATOR | Amber |
| DELIMITER | Purple |
| UNKNOWN | Gray |

---

## How It Works

### Tokenization Process

1. **Whitespace Skip** - Ignores spaces, tabs, newlines
2. **Identifier/Keyword Check** - Reads letters/underscores, checks against keyword list
3. **Number Parsing** - Reads digits, detects integers vs floats
4. **Multi-char Operators** - Checks for `==`, `!=`, `<=`, `>=` first
5. **Single-char Operators** - Checks `+`, `-`, `*`, `/`, `=`, `<`, `>`, `!`
6. **Delimiters** - Recognizes `;`, `,`, `(`, `)`, `{`, `}`, `[`, `]`
7. **Unknown** - Anything else marked as UNKNOWN

### Keyword Integration

```
┌─────────────┐     ┌──────────────────┐     ┌──────────────┐
│ Tokenizer   │────▶│ KeywordManager   │────▶│ keywords.txt │
│ tokenize()  │     │ isKeyword()      │     │ (20 keywords)│
└─────────────┘     └──────────────────┘     └──────────────┘
```

1. `Tokenizer.tokenize()` processes input
2. When an identifier is found, calls `isKeyword(word)`
3. `KeywordManager` checks against loaded keywords
4. Returns `KEYWORD` or `IDENTIFIER` accordingly

---

## Important Notes

- **File Naming**: `KeywordManager.java` must match the public class name exactly (case-sensitive)
- **Keyword File**: `keywords.txt` must be in the same directory as the compiled classes
- **Java Version**: Requires Java 8 or higher
- **Swing**: GUI uses Java Swing (included in standard JDK)

---

## Testing

**Sample Code to Tokenize:**
```java
int x = 10;
float pi = 3.14;
if (x == 0) return;
while (x > 0) { x--; }
```

**Expected Output:**
| Lexeme | Type |
|--------|------|
| int | KEYWORD |
| x | IDENTIFIER |
| = | OPERATOR |
| 10 | INTEGER |
| ; | DELIMITER |
| float | KEYWORD |
| pi | IDENTIFIER |
| = | OPERATOR |
| 3.14 | FLOAT |
| ; | DELIMITER |
| if | KEYWORD |
| ( | DELIMITER |
| x | IDENTIFIER |
| == | OPERATOR |
| 0 | INTEGER |
| ) | DELIMITER |
| return | KEYWORD |
| ; | DELIMITER |
| while | KEYWORD |
| ( | DELIMITER |
| x | IDENTIFIER |
| > | OPERATOR |
| 0 | INTEGER |
| ) | DELIMITER |
| { | DELIMITER |
| x | IDENTIFIER |
| -- | OPERATOR |
| ; | DELIMITER |
| } | DELIMITER |

---

## License

Free to use for educational purposes.

---

## Credits

- **JetJustineEspanola**: Token.java, Tokenizer.java (Core engine)
- **Akiuel**: KeywordManager.java (Keyword management)
- **zekurou1**: GUI.java (Graphical interface)

---

**Built using Java Swing**