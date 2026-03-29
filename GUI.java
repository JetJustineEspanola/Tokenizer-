import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * GUI.java — Person 3 (Redesigned)
 *
 * Compile:  javac Token.java Tokenizer.java KeywordManager.java GUI.java
 * Run:      java GUI
 */
public class GUI extends JFrame {

    // ── Palette ────────────────────────────────────────────────────
    private static final Color C_BASE    = new Color(10,  12,  16 );
    private static final Color C_SURFACE = new Color(16,  20,  28 );
    private static final Color C_RAISED  = new Color(22,  28,  38 );
    private static final Color C_BORDER  = new Color(38,  46,  60 );
    private static final Color C_BORDER2 = new Color(52,  62,  80 );
    private static final Color C_TEXT    = new Color(220, 228, 240);
    private static final Color C_MUTED   = new Color(98,  112, 134);
    private static final Color C_DIM     = new Color(56,  66,  84 );
    private static final Color C_CYAN    = new Color(34,  211, 238);
    private static final Color C_KW      = new Color(251, 113, 133);
    private static final Color C_ID      = new Color(129, 200, 255);
    private static final Color C_INT     = new Color(52,  211, 153);
    private static final Color C_FLT     = new Color(110, 231, 183);
    private static final Color C_OP      = new Color(251, 191, 36 );
    private static final Color C_DELIM   = new Color(192, 132, 252);
    private static final Color C_UNK     = new Color(148, 163, 184);

    // ── Fonts ──────────────────────────────────────────────────────
    private static final Font F_MONO    = new Font("Consolas",       Font.PLAIN, 14);
    private static final Font F_MONO_SM = new Font("Consolas",       Font.PLAIN, 12);
    private static final Font F_UI      = new Font("Segoe UI",       Font.PLAIN, 13);
    private static final Font F_UI_B    = new Font("Segoe UI",       Font.BOLD,  13);
    private static final Font F_UI_SB   = new Font("Segoe UI",       Font.BOLD,  12);
    private static final Font F_TITLE   = new Font("Segoe UI Light", Font.PLAIN, 22);
    private static final Font F_LABEL   = new Font("Segoe UI",       Font.BOLD,  10);
    private static final Font F_TINY    = new Font("Segoe UI",       Font.PLAIN, 11);

    // ── Components ─────────────────────────────────────────────────
    private JTextArea         codeInput;
    private DefaultTableModel tableModel;
    private JTable            tokenTable;
    private JLabel            statusLabel;
    private JLabel            totalLabel;
    private JPanel            sidebarStats;

    private final Tokenizer tokenizer = new Tokenizer();

    // ─────────────────────────────────────────────────────────────────
    public GUI() {
        super("Lexical Analyzer");
        KeywordManager.loadKeywords("keywords.txt");

        applyGlobalUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1060, 720);
        setMinimumSize(new Dimension(800, 560));
        setLocationRelativeTo(null);
        setBackground(C_BASE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_BASE);
        setContentPane(root);

        root.add(buildTopBar(),  BorderLayout.NORTH);
        root.add(buildBody(),    BorderLayout.CENTER);

        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────
    // TOP BAR
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(C_SURFACE);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, C_BORDER),
            new EmptyBorder(0, 28, 0, 24)
        ));
        bar.setPreferredSize(new Dimension(0, 58));

        // Left: icon + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel("\u25C8");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 22));
        icon.setForeground(C_CYAN);
        icon.setBorder(new EmptyBorder(0, 0, 0, 12));

        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Lexical Analyzer");
        title.setFont(F_TITLE);
        title.setForeground(C_TEXT);
        JLabel sub = new JLabel("Tokenizer & Classifier  \u00B7  Java Edition");
        sub.setFont(F_TINY);
        sub.setForeground(C_MUTED);
        titleStack.add(title);
        titleStack.add(sub);

        left.add(icon);
        left.add(titleStack);

        // Right: legend chips
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        chips.setOpaque(false);
        Object[][] types = {
            {"KEYWORD",    C_KW},
            {"IDENTIFIER", C_ID},
            {"INTEGER",    C_INT},
            {"FLOAT",      C_FLT},
            {"OPERATOR",   C_OP},
            {"DELIMITER",  C_DELIM},
            {"UNKNOWN",    C_UNK},
        };
        for (Object[] t : types) chips.add(makeChip((String) t[0], (Color) t[1]));

        bar.add(left,  BorderLayout.WEST);
        bar.add(chips, BorderLayout.EAST);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────────
    // BODY
    // ─────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(C_BASE);
        body.add(buildSidebar(),    BorderLayout.WEST);
        body.add(buildEditor(),     BorderLayout.CENTER);
        body.add(buildTokenPanel(), BorderLayout.EAST);
        return body;
    }

    // ── Sidebar ────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(C_SURFACE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sb.setOpaque(false);
        sb.setPreferredSize(new Dimension(190, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 0, 1, C_BORDER),
            new EmptyBorder(24, 16, 24, 16)
        ));

        sb.add(sectionLabel("ACTIONS"));
        sb.add(Box.createVerticalStrut(10));

        // Tokenize button
        GlowButton tokenizeBtn = new GlowButton("Run Tokenizer", C_CYAN, C_BASE, true);
        tokenizeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tokenizeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        tokenizeBtn.addActionListener(e -> runTokenizer());
        sb.add(tokenizeBtn);

        sb.add(Box.createVerticalStrut(8));

        GlowButton clearBtn = new GlowButton("Clear", C_RAISED, C_MUTED, false);
        clearBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        clearBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearBtn.addActionListener(e -> clearAll());
        sb.add(clearBtn);

        sb.add(Box.createVerticalStrut(24));
        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDER);
        sep.setBackground(C_BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sb.add(sep);
        sb.add(Box.createVerticalStrut(20));

        sb.add(sectionLabel("STATISTICS"));
        sb.add(Box.createVerticalStrut(12));

        sidebarStats = new JPanel();
        sidebarStats.setOpaque(false);
        sidebarStats.setLayout(new BoxLayout(sidebarStats, BoxLayout.Y_AXIS));
        sidebarStats.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshStats(null);
        sb.add(sidebarStats);

        sb.add(Box.createVerticalGlue());

        JLabel hint = new JLabel("<html>Ctrl+Enter to run</html>");
        hint.setFont(F_TINY);
        hint.setForeground(C_DIM);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(hint);

        return sb;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(C_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void refreshStats(List<Token> tokens) {
        sidebarStats.removeAll();
        int kw=0, id=0, num=0, op=0, delim=0, unk=0;
        if (tokens != null) {
            for (Token t : tokens) {
                switch (t.getType()) {
                    case Tokenizer.KEYWORD:    kw++;    break;
                    case Tokenizer.IDENTIFIER: id++;    break;
                    case Tokenizer.INTEGER:
                    case Tokenizer.FLOAT:      num++;   break;
                    case Tokenizer.OPERATOR:   op++;    break;
                    case Tokenizer.DELIMITER:  delim++; break;
                    default:                   unk++;   break;
                }
            }
        }
        int total = (tokens == null) ? 0 : tokens.size();
        Object[][] rows = {
            {"Total",       total, C_CYAN},
            {"Keywords",    kw,    C_KW},
            {"Identifiers", id,    C_ID},
            {"Numbers",     num,   C_INT},
            {"Operators",   op,    C_OP},
            {"Delimiters",  delim, C_DELIM},
            {"Unknown",     unk,   C_UNK},
        };
        for (Object[] r : rows) {
            sidebarStats.add(makeStatRow((String) r[0], (int) r[1], (Color) r[2]));
            sidebarStats.add(Box.createVerticalStrut(6));
        }
        sidebarStats.revalidate();
        sidebarStats.repaint();
    }

    private JPanel makeStatRow(String label, int value, Color color) {
        JPanel row = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 18));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 55));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(5, 10, 5, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(F_TINY);
        lbl.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));

        JLabel val = new JLabel(String.valueOf(value));
        val.setFont(F_UI_B);
        val.setForeground(color);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    // ── Editor ─────────────────────────────────────────────────────
    private JPanel buildEditor() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(C_BASE);
        panel.setBorder(new MatteBorder(0, 0, 0, 1, C_BORDER));

        // Tab bar
        JPanel tabBar = new JPanel(new BorderLayout());
        tabBar.setBackground(C_SURFACE);
        tabBar.setBorder(new MatteBorder(0, 0, 1, 0, C_BORDER));
        tabBar.setPreferredSize(new Dimension(0, 36));

        JLabel tab = new JLabel("  source.code") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_CYAN);
                g2.fillRect(0, getHeight()-2, getWidth(), 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tab.setFont(F_MONO_SM);
        tab.setForeground(C_TEXT);
        tab.setBorder(new EmptyBorder(0, 18, 0, 18));

        JLabel editorHint = new JLabel("Ctrl+Enter to run   ");
        editorHint.setFont(F_TINY);
        editorHint.setForeground(C_DIM);

        tabBar.add(tab,       BorderLayout.WEST);
        tabBar.add(editorHint, BorderLayout.EAST);

        // Line numbers
        JTextArea lineNums = new JTextArea("1") {
            @Override public Dimension getPreferredSize() {
                return new Dimension(42, super.getPreferredSize().height);
            }
        };
        lineNums.setFont(F_MONO_SM);
        lineNums.setBackground(new Color(14, 18, 26));
        lineNums.setForeground(C_DIM);
        lineNums.setEditable(false);
        lineNums.setFocusable(false);
        lineNums.setBorder(new EmptyBorder(14, 8, 14, 8));
        lineNums.setHighlighter(null);

        // Code input
        codeInput = new JTextArea();
        codeInput.setFont(F_MONO);
        codeInput.setBackground(C_BASE);
        codeInput.setForeground(new Color(180, 210, 240));
        codeInput.setCaretColor(C_CYAN);
        codeInput.setBorder(new EmptyBorder(14, 10, 14, 14));
        codeInput.setLineWrap(false);
        codeInput.setTabSize(4);
        codeInput.setSelectionColor(new Color(34, 211, 238, 40));
        codeInput.setSelectedTextColor(C_TEXT);

        // Placeholder
        codeInput.setText("int x = 10;\nfloat pi = 3.14;\nif (x == 0) return;\nwhile (x > 0) { x--; }");
        codeInput.setForeground(C_MUTED);
        codeInput.addFocusListener(new FocusAdapter() {
            boolean first = true;
            @Override public void focusGained(FocusEvent e) {
                if (first) {
                    codeInput.setText("");
                    codeInput.setForeground(new Color(180, 210, 240));
                    first = false;
                }
            }
        });

        // Sync line numbers
        codeInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { updateLines(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { updateLines(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
            void updateLines() {
                int n = codeInput.getLineCount();
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= n; i++) { if (i > 1) sb.append('\n'); sb.append(i); }
                lineNums.setText(sb.toString());
            }
        });

        // Ctrl+Enter
        codeInput.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "run");
        codeInput.getActionMap().put("run", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { runTokenizer(); }
        });

        JScrollPane scroll = new JScrollPane(codeInput);
        scroll.setBorder(null);
        scroll.setBackground(C_BASE);
        scroll.getViewport().setBackground(C_BASE);
        scroll.setRowHeaderView(lineNums);
        styleScrollBar(scroll.getVerticalScrollBar());
        styleScrollBar(scroll.getHorizontalScrollBar());

        // Status strip
        JPanel strip = new JPanel(new BorderLayout());
        strip.setBackground(new Color(12, 16, 22));
        strip.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, C_BORDER),
            new EmptyBorder(5, 16, 5, 16)
        ));
        statusLabel = new JLabel("Ready \u2014 Enter code and press Ctrl+Enter or click Run Tokenizer");
        statusLabel.setFont(F_TINY);
        statusLabel.setForeground(C_MUTED);
        totalLabel = new JLabel("");
        totalLabel.setFont(F_TINY);
        totalLabel.setForeground(C_MUTED);
        strip.add(statusLabel, BorderLayout.WEST);
        strip.add(totalLabel,  BorderLayout.EAST);

        panel.add(tabBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(strip,  BorderLayout.SOUTH);
        return panel;
    }

    // ── Token Panel ────────────────────────────────────────────────
    private JPanel buildTokenPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(C_SURFACE);
        panel.setPreferredSize(new Dimension(360, 0));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C_SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, C_BORDER),
            new EmptyBorder(10, 18, 10, 18)
        ));
        JLabel lbl = new JLabel("TOKEN OUTPUT");
        lbl.setFont(F_LABEL);
        lbl.setForeground(C_MUTED);
        header.add(lbl, BorderLayout.WEST);

        // Table model
        tableModel = new DefaultTableModel(new String[]{"", "Lexeme", "Type"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tokenTable = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? C_SURFACE : C_RAISED);
                return c;
            }
        };
        tokenTable.setFont(F_MONO_SM);
        tokenTable.setForeground(C_TEXT);
        tokenTable.setBackground(C_SURFACE);
        tokenTable.setSelectionBackground(new Color(34, 211, 238, 30));
        tokenTable.setSelectionForeground(C_TEXT);
        tokenTable.setRowHeight(34);
        tokenTable.setShowGrid(false);
        tokenTable.setIntercellSpacing(new Dimension(0, 0));
        tokenTable.setFillsViewportHeight(true);
        tokenTable.setTableHeader(null);

        tokenTable.getColumnModel().getColumn(0).setMaxWidth(38);
        tokenTable.getColumnModel().getColumn(0).setPreferredWidth(38);
        tokenTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        tokenTable.getColumnModel().getColumn(2).setPreferredWidth(140);

        tokenTable.getColumnModel().getColumn(0).setCellRenderer(new IdxRenderer());
        tokenTable.getColumnModel().getColumn(1).setCellRenderer(new LexemeRenderer());
        tokenTable.getColumnModel().getColumn(2).setCellRenderer(new TypeRenderer());

        JScrollPane scroll = new JScrollPane(tokenTable);
        scroll.setBorder(null);
        scroll.setBackground(C_SURFACE);
        scroll.getViewport().setBackground(C_SURFACE);
        styleScrollBar(scroll.getVerticalScrollBar());

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────
    // LOGIC
    // ─────────────────────────────────────────────────────────────────
    private void runTokenizer() {
        String raw = codeInput.getText().trim();
        if (raw.isEmpty()) {
            setStatus("No input provided.", C_UNK);
            return;
        }

        tableModel.setRowCount(0);
        String[] lines = raw.split("\n");
        List<Token> all = new ArrayList<>();
        int idx = 0;

        for (String line : lines) {
            List<Token> toks = tokenizer.tokenize(line);
            for (Token t : toks) {
                all.add(t);
                tableModel.addRow(new Object[]{ ++idx, t.getLexeme(), t.getType() });
            }
        }

        refreshStats(all);
        int total = all.size();
        totalLabel.setText(total + " token" + (total == 1 ? "" : "s") + "   ");
        setStatus("\u2714  " + lines.length + " line(s) \u2192 " + total + " tokens found.", C_INT);
    }

    private void clearAll() {
        codeInput.setText("");
        codeInput.setForeground(new Color(180, 210, 240));
        tableModel.setRowCount(0);
        totalLabel.setText("");
        refreshStats(null);
        setStatus("Cleared.", C_MUTED);
    }

    private void setStatus(String msg, Color c) {
        statusLabel.setText(msg);
        statusLabel.setForeground(c);
    }

    // ─────────────────────────────────────────────────────────────────
    // RENDERERS
    // ─────────────────────────────────────────────────────────────────
    private class IdxRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setText(v == null ? "" : v.toString());
            setFont(F_TINY);
            setForeground(C_DIM);
            setHorizontalAlignment(CENTER);
            setBackground(row % 2 == 0 ? C_SURFACE : C_RAISED);
            if (sel) setBackground(new Color(34, 211, 238, 25));
            setBorder(new EmptyBorder(0, 4, 0, 0));
            return this;
        }
    }

    private class LexemeRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setText(v == null ? "" : v.toString());
            setFont(F_MONO_SM);
            setForeground(C_TEXT);
            setBackground(row % 2 == 0 ? C_SURFACE : C_RAISED);
            if (sel) setBackground(new Color(34, 211, 238, 25));
            setBorder(new EmptyBorder(0, 14, 0, 4));
            return this;
        }
    }

    private class TypeRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            String type = v == null ? "" : v.toString();
            Color color = typeColor(type);

            JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            cell.setBackground(row % 2 == 0 ? C_SURFACE : C_RAISED);
            if (sel) cell.setBackground(new Color(34, 211, 238, 25));

            JLabel badge = new JLabel(type) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 22));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setFont(F_UI_SB);
            badge.setForeground(color);
            badge.setBorder(new EmptyBorder(2, 9, 2, 9));
            badge.setOpaque(false);

            cell.add(badge);
            return cell;
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────
    private Color typeColor(String t) {
        switch (t) {
            case Tokenizer.KEYWORD:    return C_KW;
            case Tokenizer.IDENTIFIER: return C_ID;
            case Tokenizer.INTEGER:    return C_INT;
            case Tokenizer.FLOAT:      return C_FLT;
            case Tokenizer.OPERATOR:   return C_OP;
            case Tokenizer.DELIMITER:  return C_DELIM;
            default:                   return C_UNK;
        }
    }

    private JLabel makeChip(String text, Color color) {
        JLabel chip = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        chip.setFont(new Font("Segoe UI", Font.BOLD, 10));
        chip.setForeground(color);
        chip.setBorder(new EmptyBorder(3, 8, 3, 8));
        chip.setOpaque(false);
        return chip;
    }

    private void styleScrollBar(JScrollBar sb) {
        sb.setBackground(C_BASE);
        sb.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = C_BORDER2;
                trackColor = C_BASE;
            }
            @Override protected JButton createDecreaseButton(int o) { return emptyBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return emptyBtn(); }
            JButton emptyBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_BORDER2);
                g2.fillRoundRect(r.x+2, r.y+2, r.width-4, r.height-4, 6, 6);
                g2.dispose();
            }
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(C_BASE);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });
    }

    private void applyGlobalUI() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        UIManager.put("Panel.background",          C_BASE);
        UIManager.put("ScrollPane.background",     C_BASE);
        UIManager.put("Table.background",          C_SURFACE);
        UIManager.put("Table.foreground",          C_TEXT);
        UIManager.put("Table.gridColor",           C_BORDER);
    }

    // ─────────────────────────────────────────────────────────────────
    // GLOW BUTTON
    // ─────────────────────────────────────────────────────────────────
    private static class GlowButton extends JButton {
        private final Color accent;
        private final Color fg;
        private final boolean filled;
        private float hover = 0f;
        private Timer timer;

        GlowButton(String text, Color accent, Color fg, boolean filled) {
            super(text);
            this.accent = accent;
            this.fg     = fg;
            this.filled = filled;
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(filled ? fg : new Color(180, 200, 220));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 16, 10, 16));
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { animateTo(1f); }
                @Override public void mouseExited(MouseEvent e)  { animateTo(0f); }
            });
        }

        void animateTo(float target) {
            if (timer != null) timer.stop();
            timer = new Timer(16, null);
            timer.addActionListener(e -> {
                hover += (target - hover) * 0.2f;
                if (Math.abs(hover - target) < 0.01f) { hover = target; timer.stop(); }
                repaint();
            });
            timer.start();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean pressed = getModel().isPressed();

            if (filled) {
                Color base = pressed ? accent.darker() : accent;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // glow
                if (hover > 0) {
                    for (int i = 4; i > 0; i--) {
                        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(),
                            (int)(15 * hover * (5-i))));
                        g2.drawRoundRect(-i, -i, getWidth()+i*2-1, getHeight()+i*2-1, 12, 12);
                    }
                }
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 140));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            } else {
                Color bg = new Color(
                    (int)(C_RAISED.getRed()   + (accent.getRed()   - C_RAISED.getRed())   * hover * 0.2f),
                    (int)(C_RAISED.getGreen() + (accent.getGreen() - C_RAISED.getGreen()) * hover * 0.2f),
                    (int)(C_RAISED.getBlue()  + (accent.getBlue()  - C_RAISED.getBlue())  * hover * 0.2f)
                );
                g2.setColor(pressed ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(C_BORDER2);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}