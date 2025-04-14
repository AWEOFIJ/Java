import java.util.Scanner; // 使用JOptionPane播放器來處理UI和入力:

public class SimpleCalculator {
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);

        System.out.print("計算機介面：");
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.print("\n操作符 (輸入 'exit' 結束)：");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                System.out.print("輸入第一個數字：");
                double num1 = Double.parseDouble(scanner.nextLine());

                System.out.print("輸入第二個數字：");
                double num2 = Double.parseDouble(scanner.nextLine());

                System.out.print("選擇操作符 (+, -, *, /)：");
                char op = scanner.nextLine().charAt(0);

                double result = 0;
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
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            System.out.println("錯誤：不能除以零！");
                            continue;
                        }
                        break;
                    default:
                        System.out.println("無效的操作符！");
                        continue;
                }
                System.out.println("結果是：" + result);
            } catch (NumberFormatException e) {
                System.out.println("輸入錯誤，請輸入有效的數字！");
            }
        } while (true);

        scanner.close();
    }
}