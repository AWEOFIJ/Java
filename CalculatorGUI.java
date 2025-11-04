
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
// import java.awt.event.keyTyped;

public class CalculatorGUI extends JFrame implements ActionListener {

    private final JTextField display; // 顯示運算過程與結果
    private final JTextField processDisplay; // 顯示計算過程
    private final JPanel buttonPanel; // 按鈕排列面板
    private double result; // 儲存目前計算結果
    private String operator; // 儲存前一次的運算子
    private boolean startNewNumber; // 是否開始輸入新的數字
    private List<String> tokens; // 儲存計算過程的 token（operand/operator）

    public CalculatorGUI() {
        this(true);
    }

    /**
     * Create the CalculatorGUI. If visible is false the frame will not be shown
     * (useful for tests).
     */
    public CalculatorGUI(boolean visible) {
        // 初始化運算邏輯參數
        result = 0;
        operator = "=";
        startNewNumber = true;
        tokens = new ArrayList<>();

        // 建立計算過程顯示欄位
        processDisplay = new JTextField("");
        processDisplay.setFont(new Font("SansSerif", Font.PLAIN, 14));
        processDisplay.setHorizontalAlignment(JTextField.RIGHT);
        processDisplay.setEditable(false);
        processDisplay.setBackground(Color.WHITE);

        // 建立結果顯示欄位
        display = new JTextField("0");
        display.setFont(new Font("SansSerif", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        // 建立按鈕面板，採用 GridLayout 排列按鈕
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        // 定義按鈕標籤
        String[] buttonLabels = {
                "AC", "C", "←", "",
                "7", "8", "9", "+",
                "4", "5", "6", "-",
                "1", "2", "3", "*",
                "0", ".", "=", "/"
        };

        for (String label : buttonLabels) {
            if (label.equals("")) {
                // 若標籤為空，加入空白 JLabel 作為補位
                buttonPanel.add(new JLabel(""));
            } else {
                JButton button = new JButton(label);
                button.setFont(new Font("SansSerif", Font.BOLD, 24));
                // ActionListener will be added after construction
                buttonPanel.add(button);
            }
        }

        // 組合畫面上的元件
        setLayout(new BorderLayout(5, 5));
        
        // 創建一個面板來包含兩個顯示欄位
        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));
        displayPanel.add(processDisplay, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.SOUTH);
        
        add(displayPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // 設定鍵盤事件
        setupKeyBindings();

        // JFrame 設定
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // 自動調整元件大小
        setLocationRelativeTo(null); // 畫面置中
        setVisible(visible);
    }

    /**
     * 利用 Key Bindings 讓鍵盤輸入也能操作計算機
     */
    private void setupKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // More reliable bindings: use both "typed X" (for keyTyped) and VK_ constants (for keyPressed)
        // Use a single ActionMap key / AbstractAction per logical command so that
        // multiple KeyStrokes (typed, VK, numpad) mapped to the same command
        // only invoke one action. This avoids duplicate firing when both a
        // typed and a VK event are produced for the same physical key.
        java.util.Map<String, String> cmdToActionKey = new java.util.HashMap<>();

        java.util.function.BiConsumer<KeyStroke, String> mapKeyStrokeToCmd = (ks, cmd) -> {
            if (ks == null || cmd == null) return;
            String actionKey = cmdToActionKey.get(cmd);
            if (actionKey == null) {
                actionKey = "KB_CMD_" + cmd;
                cmdToActionKey.put(cmd, actionKey);
                // create the single AbstractAction for this command
                am.put(actionKey, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Dispatch the command on the EDT asynchronously to avoid re-entrancy
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                CalculatorGUI.this.actionPerformed(new ActionEvent(CalculatorGUI.this, ActionEvent.ACTION_PERFORMED, cmd));
                            }
                        });
                    }
                });
            }
            im.put(ks, actionKey);
        };

        java.util.function.BiConsumer<KeyStroke, String> bindKS = (ks, cmd) -> {
            mapKeyStrokeToCmd.accept(ks, cmd);
        };

        // Digits: bind VK (top-row) and numpad variants. We avoid "typed" bindings to
        // prevent duplicate activations (pressed + typed).
        for (int d = 0; d <= 9; d++) {
            String s = Integer.toString(d);
            bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_0 + d, 0), s);
            bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0 + d, 0), s);
        }

        // Decimal (period)
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), ".");
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), ".");

        // Operators: prefer VK numpad variants and top-row equivalents when available
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "+");
        // top-row '+' is SHIFT + '=' on many keyboards
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, java.awt.event.InputEvent.SHIFT_DOWN_MASK), "+");

        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "-");
        // '*' and '/'
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "*");
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0), "/");
        // top-row '*' is SHIFT+8 on many keyboards; leave numpad primary for these

        // Equals / Enter
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "=");
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "=");

        // Backspace
        bindKS.accept(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "←");
    }

    public void keyTyped(KeyEvent e) {
        // 這裡不需要實作，因為我們使用 Key Bindings

        String keyText = KeyEvent.getKeyText(e.getKeyCode());
        String keyCode = Integer.toString(e.getKeyCode());

        System.out.println("Key typed: " + keyText + " (" + keyCode + ")");
    }

    public void keyPressed(KeyEvent e) {
        // 這裡不需要實作，因為我們使用 Key Bindings

        String keyText = KeyEvent.getKeyText(e.getKeyCode());
        String keyCode = Integer.toString(e.getKeyCode());

        System.out.println("Key Pressed: " + keyText + " (" + keyCode + ")");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        // Debug: log action commands so keyboard input can be diagnosed
        System.out.println("actionPerformed: command='" + command + "' source=" + e.getSource().getClass().getName());

        // 處理「AC」鍵：重置所有內容與狀態
        if (command.equals("AC")) {
            result = 0;
            operator = "=";
            startNewNumber = true;
            display.setText("0");
            tokens.clear();
            processDisplay.setText("");
            return;
        }

        // 處理「C」鍵：清除目前的輸入不影響先前的計算結果
        if (command.equals("C")) {
            startNewNumber = true;
            display.setText("0");
            return;
        }

        // 處理「←」鍵：刪除當前輸入的最後一個字元
        if (command.equals("←")) {
            String text = display.getText();
            if (text.length() > 1) {
                display.setText(text.substring(0, text.length() - 1));
            } else {
                display.setText("0");
            }
            startNewNumber = false;
            return;
        }

        // 若輸入的是數字或小數點
        if (command.matches("[0-9\\.]")) {
            if (startNewNumber) {
                display.setText(command.equals(".") ? "0." : command);
                if (operator.equals("=")) {
                    tokens.clear();
                }
                startNewNumber = false;
            } else {
                if (command.equals(".") && display.getText().contains(".")) {
                    return;
                }
                display.setText(display.getText() + command);
            }
            // update display to show running tokens + current input
            updateProcessDisplay();
        } else {
            // 處理運算符（改用 tokens 管理歷史）
            if (!startNewNumber) {
                String currentNumber = display.getText();
                // Perform the calculation using the previous operator
                calculate(Double.parseDouble(currentNumber));

                // Display's text has been updated by calculate() and contains the result
                String resultStr = display.getText();

                // If previous operator was '=' we reset the tokens and start with the current number
                if (operator.equals("=")) {
                    tokens.clear();
                    tokens.add(currentNumber);
                }

                if (command.equals("=")) {
                    // Ensure last token is the current operand
                    if (tokens.isEmpty() || !tokens.get(tokens.size() - 1).equals(currentNumber)) {
                        tokens.add(currentNumber);
                    }
                    tokens.add("=");
                    tokens.add(resultStr);
                } else {
                    // For other operators, ensure current operand is present then add operator
                    if (tokens.isEmpty()) {
                        tokens.add(currentNumber);
                    } else {
                        String last = tokens.get(tokens.size() - 1);
                        if (isOperatorToken(last)) {
                            // If tokens end with an operator placeholder, append the just-entered operand
                            tokens.add(currentNumber);
                        } else {
                            // If last token is an operand that differs from currentNumber, append it
                            if (!last.equals(currentNumber)) {
                                tokens.add(currentNumber);
                            }
                        }
                    }
                    tokens.add(command);
                }

                operator = command;
                startNewNumber = true;
                updateProcessDisplay();
            } else {
                if (command.equals("-") && operator.equals("=")) {
                    display.setText(command);
                    startNewNumber = false;
                    tokens.clear();
                    updateProcessDisplay();
                } else {
                    if (!tokens.isEmpty()) {
                        String last = tokens.get(tokens.size() - 1);
                        if (isOperatorToken(last)) {
                            // replace last operator
                            tokens.set(tokens.size() - 1, command);
                        } else {
                            // append operator placeholder
                            tokens.add(command);
                        }
                    }
                    operator = command;
                    updateProcessDisplay();
                }
            }
        }
    }

    /**
     * 根據前次運算符計算目前結果，並更新顯示欄位
     */
    private void calculate(double n) {
        switch (operator) {
            case "+":
                result += n;
                break;
            case "-":
                result -= n;
                break;
            case "*":
                result *= n;
                break;
            case "/":
                if (n == 0) {
                    JOptionPane.showMessageDialog(this, "錯誤：不能除以零！", "錯誤", JOptionPane.ERROR_MESSAGE);
                    result = 0;
                } else {
                    result /= n;
                }
                break;
            case "=":
                result = n;
                break;
        }
        
        // 如果是整數就省略小數點和0
        String resultStr;
        if (result == (long) result) {
            resultStr = String.format("%d", (long)result);
        } else {
            resultStr = Double.toString(result);
        }
        display.setText(resultStr);
        
        // calculation does not mutate the history tokens; history rendering is handled in actionPerformed
    }

    private boolean isOperatorToken(String t) {
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/") || t.equals("=");
    }

    private void updateProcessDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(tokens.get(i));
        }

        if (!startNewNumber) {
            String current = display.getText();
            if (!tokens.isEmpty()) {
                String last = tokens.get(tokens.size() - 1);
                if (isOperatorToken(last) && !last.equals("=")) {
                    if (sb.length() > 0) sb.append(' ');
                    sb.append(current);
                }
            }
            if (tokens.isEmpty()) {
                sb.setLength(0);
                sb.append(current);
            }
        }

        processDisplay.setText(sb.toString());
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CalculatorGUI gui = new CalculatorGUI();
                // Add ActionListeners after construction to avoid leaking 'this'
                for (Component comp : gui.buttonPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        JButton jButton = (JButton) comp;
                        jButton.addActionListener(gui);
                    }
                }
            }
        });
    }
}
