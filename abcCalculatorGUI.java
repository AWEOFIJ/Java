import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class abcCalculatorGUI extends JFrame implements ActionListener {
    private JTextField display;      // 顯示運算過程與結果
    private JPanel buttonPanel;      // 按鈕排列面板
    private double result;           // 儲存目前計算結果
    private String operator;         // 前次運算子
    private boolean startNewNumber;  // 是否開啟新數字輸入狀態

    public abcCalculatorGUI() {
        // 初始化運算邏輯參數
        result = 0;
        operator = "=";
        startNewNumber = true;

        // 建立顯示欄位
        display = new JTextField("0");
        display.setFont(new Font("SansSerif", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        // 建立按鈕面板，並以 GridLayout 佈局 4x4 按鈕
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("SansSerif", Font.BOLD, 24));
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        // 組合主要介面元件
        setLayout(new BorderLayout(5, 5));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // 加上鍵盤事件處理，使數字與運算符可透過鍵盤輸入
        setupKeyBindings();

        // JFrame 設定
        setTitle("計算機");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null); // 畫面置中
        setVisible(true);
    }

    /**
     * 利用 Key Bindings 讓鍵盤按鍵也能觸發對應動作
     */
    private void setupKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // 指定要處理的按鍵組合：數字、"." 與常用的運算子
        String[] keys = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "+", "-", "*", "/", "=", "ENTER" };
        
        Integer[] numbInteger = {
            KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3,
            KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7,
            KeyEvent.VK_8, KeyEvent.VK_9
        };

        for (String key : keys) {
            KeyStroke ks = KeyStroke.getKeyStroke(key);
            im.put(ks, key);
            am.put(key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 將 ENTER 鍵視為 "=" 處理
                    String input = key.equals("ENTER") ? "=" : key;
                    // 模擬按鈕點擊事件呼叫 actionPerformed
                    abcCalculatorGUI.this.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, input));
                }
            });
        }

        for (Integer num : numbInteger) {
            KeyStroke numb = KeyStroke.getKeyStroke(num, num);
            im.put(numb, num);
            am.put(num, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 將 ENTER 鍵視為 "=" 處理
                    String input = num.equals("ENTER") ? "=" : num.toString();
                    // 模擬按鈕點擊事件呼叫 actionPerformed
                    abcCalculatorGUI.this.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, input));
                }
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // 若輸入的是數字或小數點
        if (command.matches("[0-9\\.]")) {
            if (startNewNumber) {
                display.setText(command);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command);
            }
        } else { 
            // 處理運算子（包括 "="）
            if (!startNewNumber) {
                calculate(Double.parseDouble(display.getText()));
                operator = command;
                startNewNumber = true;
            } else {  
                // 若剛輸入完算子後立即輸入 "-"，可支援第一個數為負數
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
     * 根據上一個運算子計算目前結果，並更新顯示欄位
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
                    JOptionPane.showMessageDialog(this, "錯誤：除以零！", "錯誤", JOptionPane.ERROR_MESSAGE);
                    result = 0;
                } else {
                    result /= n;
                }
                break;
            case "=":
                result = n;
                break;
        }
        display.setText("" + result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new abcCalculatorGUI());
    }
}