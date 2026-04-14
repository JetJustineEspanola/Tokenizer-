import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Load keywords before tokenizing
        KeywordManager.loadKeywords("keywords.txt");

        Tokenizer tokenizer = new Tokenizer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   LEXICAL ANALYZER (TOKENIZER)");
        System.out.println("========================================");
        System.out.println();

        boolean running = true;
        while (running) {
            if (!scanner.hasNextLine()) break;
            System.out.print("Enter code (or 'quit' to exit): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("\nExiting tokenizer. Goodbye!");
                running = false;
                break;
            }

            if (input.isEmpty()) {
                System.out.println("(empty input)\n");
                continue;
            }

            // Tokenize the input
            List<Token> tokens = tokenizer.tokenize(input);

            // Display results
            System.out.println("\n================== TOKENS ==================");

            if (tokens.isEmpty()) {
                System.out.println("(no tokens found)");
            } else {
                System.out.println("LEXEME              | TYPE");
                System.out.println("--------------------+--------------------");
                for (Token token : tokens) {
                    System.out.println(token.toString());
                }
            }

            System.out.println("==========================================");
            System.out.println("Total tokens: " + tokens.size());
            System.out.println();
        }

        scanner.close();
    }
}