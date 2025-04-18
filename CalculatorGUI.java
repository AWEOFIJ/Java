
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
// import java.awt.event.keyTyped;

public class CalculatorGUI extends JFrame implements ActionListener {

    private JTextField display;      // 顯示運算過程與結果
    private JPanel buttonPanel;      // 按鈕排列面板
    private double result;           // 儲存目前計算結果
    private String operator;         // 儲存前一次的運算子
    private boolean startNewNumber;  // 是否開始輸入新的數字

    public CalculatorGUI() {
        // 初始化運算邏輯參數
        result = 0;
        operator = "=";
        startNewNumber = true;

        // 建立顯示欄位
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
                button.addActionListener(this);
                buttonPanel.add(button);
            }
        }

        // 組合畫面上的元件
        setLayout(new BorderLayout(5, 5));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // 設定鍵盤事件
        setupKeyBindings();

        // JFrame 設定
        setTitle("計算機");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // 自動調整元件大小
        setLocationRelativeTo(null);  // 畫面置中
        setVisible(true);
    }

    /**
     * 利用 Key Bindings 讓鍵盤輸入也能操作計算機
     */
    private void setupKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // 設定需要處理的鍵
        String[] keys = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "+", "-", "*", "/", "=", "ENTER",
                "BACK_SPACE" };

        // int[] keysCode = { KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5,
        //         KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_PERIOD, KeyEvent.VK_ADD,
        //         KeyEvent.VK_SUBTRACT, KeyEvent.VK_MULTIPLY, KeyEvent.VK_DIVIDE, KeyEvent.VK_ENTER,
        //         KeyEvent.VK_BACK_SPACE };

        char[] keysCode = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '+', '-', '*', '/', '=', KeyEvent.VK_ENTER,
                KeyEvent.VK_BACK_SPACE };

        for (String key : keys) {
            KeyStroke ks = KeyStroke.getKeyStroke(key);
            im.put(ks, key);
            am.put(ks, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String input = key.equals("ENTER") ? "=" : key.equals("BACK_SPACE") ? "←" : key;
                    CalculatorGUI.this
                            .actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, input));
                }
            });
        }

        for (char keyCode : keysCode) {
            KeyStroke ks = KeyStroke.getKeyStroke(keyCode, 0);
            im.put(ks, Integer.toString(keyCode));
            am.put(ks, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //KeyEvent.KEY_TYPED;

                    //keyTyped(keyCode);
                    
                    String input = KeyEvent.getKeyText(keyCode);
                    CalculatorGUI.this.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED,
                            input));
                }
            });
        }
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

        // 處理「AC」鍵：重置所有內容與狀態
        if (command.equals("AC")) {
            result = 0;
            operator = "=";
            startNewNumber = true;
            display.setText("0");
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
                startNewNumber = false;
            } else {
                if (command.equals(".") && display.getText().contains(".")) {
                    return;
                }
                display.setText(display.getText() + command);
            }
        } else {
            // 處理運算符
            if (!startNewNumber) {
                calculate(Double.parseDouble(display.getText()));
                operator = command;
                startNewNumber = true;
            } else {
                if (command.equals("-")) {
                    display.setText(command);
                    startNewNumber = false;
                } else {
                    operator = command;
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
        display.setText(String.valueOf(result));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorGUI());

    }
}
