import javax.swing.JOptionPane;

public class SimpleCalculatorGUI {
    public static void main(String[] args) {
        while (true) {
            // 請求使用者輸入操作符或輸入 "exit" 結束
            String operator = JOptionPane.showInputDialog(null, "請輸入操作符（+、-、*、/）或輸入 exit 結束：");
            if (operator == null || operator.equalsIgnoreCase("exit")) {
                break;
            }
            if (operator.length() == 0) {
                JOptionPane.showMessageDialog(null, "請輸入有效的操作符！");
                continue;
            }
            
            // 請求使用者輸入第一個數字
            String strNum1 = JOptionPane.showInputDialog(null, "輸入第一個數字：");
            if (strNum1 == null) { // 當使用者按下取消
                break;
            }
            
            try {
                double num1 = Double.parseDouble(strNum1);

                // 請求使用者輸入第二個數字
                String strNum2 = JOptionPane.showInputDialog(null, "輸入第二個數字：");
                if (strNum2 == null) {
                    break;
                }
                double num2 = Double.parseDouble(strNum2);
                
                double result = 0;
                char op = operator.charAt(0);
                
                // 根據操作符執行運算
                switch (op) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 == 0) {
                            JOptionPane.showMessageDialog(null, "錯誤：不能除以零！");
                            continue;
                        } else {
                            result = num1 / num2;
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "無效的操作符！");
                        continue;
                }
                
                // 顯示計算結果
                JOptionPane.showMessageDialog(null, "結果是：" + result);
            } catch (NumberFormatException e) {
                // 處理無效數字輸入
                JOptionPane.showMessageDialog(null, "輸入錯誤，請輸入有效的數字！");
            }
        }
        System.exit(0);
    }
}