import java.util.Scanner;

public class simpleCalculator_input {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("簡易計算機 (Java 12)\n");
        
        while (true) {
            System.out.print("輸入第一個數字： ");
            double num1 = scanner.nextDouble();
            
            System.out.print("選取運算符（+、-、*、/）： ");
            char operator = scanner.next().charAt(0);
            
            System.out.print("輸入第二個數字： ");
            double num2 = scanner.nextDouble();
            
            double result = 0.0;
            
            switch (operator) {
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
                    if (num2 != 0.0) {
                        result = num1 / num2;
                    } else {
                        System.out.println("除錯誤：不能數字為零");
                        continue;
                    }
                    break;
                default:
                    System.out.println("無效的操作符「" + operator + "」選取！请重新入力。");
                    continue;
            }
            
            if (result != 0.0) {
                System.out.printf("%.2f %c %.2f = %.2f\n", num1, operator, num2, result);
            } else {
                System.out.println("結果：零");
            }
            
            System.out.print("繼續計算（Y/N）？ ");
            String continueCalculation = scanner.next();
            
            if (!continueCalculation.equalsIgnoreCase("y")) {
                break;
            }
        }
        
        System.out.println("\n計算完成！");
        scanner.close();
    }
}
