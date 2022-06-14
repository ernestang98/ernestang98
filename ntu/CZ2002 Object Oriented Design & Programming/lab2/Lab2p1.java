package lab2;
import java.util.Scanner;

public class Lab2p1 {
    public static void main(String[] args) {
        int choice;
        Scanner sc = new Scanner(System.in); do {
            System.out.println("Perform the following methods:");
            System.out.println("1: multiplication test");
            System.out.println("2: quotient using division by subtraction");
            System.out.println("3: remainder using division by subtraction");
            System.out.println("4: count the number of digits");
            System.out.println("5: position of a digit");
            System.out.println("6: extract all odd digits");
            System.out.println("7: quit");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    mulTest();
                    break;
                case 2:
                    System.out.print("Enter integer to be divided: ");
                    int input0 = sc.nextInt();
                    System.out.print("Enter divisor: ");
                    int input1 = sc.nextInt();
                    int num0 = divide(input0, input1);
                    System.out.printf("%d/%d = %d\n\n", input0, input1, num0);
                    break;
                case 3:
                    System.out.print("Enter integer to be modulo-ed: ");
                    int input2 = sc.nextInt();
                    System.out.print("Enter modulus: ");
                    int input3 = sc.nextInt();
                    int num1 = modulus(input2, input3);
                    String str = input2 + " % " + input3 + " = " + num1;
                    System.out.println(str);
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Enter integer to be counted: ");
                    int input4 = sc.nextInt();
                    int num2 = countDigits(input4);
                    if (num2 == -1) {
                        System.out.println("Error!!!!");
                    }
                    else {
                        System.out.printf("count = %d\n", num2);
                    }
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Enter integer: ");
                    int input5 = sc.nextInt();
                    System.out.print("Enter digit to be found: ");
                    int input6 = sc.nextInt();
                    int num3 = position(input5, input6);
                    System.out.printf("Position = %d\n", num3);
                    System.out.println();
                    break;
                case 6:
                    System.out.print("Enter integer: ");
                    long input7 = sc.nextInt();
                    long num4 = extractOddDigits(input7);
                    if (num4 == 0) {
                        System.out.printf("OddDigits = %d\n", -1);
                    }
                    else if (num4 == -1) {
                        System.out.println("OddDigits = Error Input!!!!");
                    }
                    else {
                        System.out.printf("OddDigits = %d\n", num4);
                    }
                    System.out.println();
                    break;
                case 7:
                    System.out.println("Program terminating ....");
            }
        } while (choice < 7);
    }

    public static void mulTest(){
        int score = 0;
        for (int i = 0; i < 5; i++) {
            int value_0 = (int) Math.round(Math.random() * 10);
            int value_1 = (int) Math.round(Math.random() * 10);
            if (value_0 <= 0) {
                value_0 = 1;
            }
            if (value_1 <= 0) {
                value_1 = 1;
            }
            System.out.printf("How much is %d times %d? ", value_0, value_1);
            Scanner sc1 = new Scanner(System.in);
            int input = sc1.nextInt();
            int multiply = value_0 * value_1;
            if (input == multiply) {
                score += 1;
            }
        }
        System.out.printf("%d answers out of 5 are correct.\n\n", score);
    }

    public static int divide(int m, int n) {
        int count = 0;
        while (m - n >= 0) {
            m -= n;
            count++;
        }
        return count;
    }

    public static int modulus(int m, int n) {
        while (m - n >= 0) {
            m -= n;
        }
        return m;
    }

    public static int countDigits(int val) {
        int count = 0;
        if (val < 0) {
            count = -1;
        }
        else {
            count+=1;
            while (val/10 > 0) {
                count += 1;
                val=val/10;
            }
        }
        return count;
    }

    public static int position(int n, int digit) {
        int length = countDigits(n);
        int temp;
        int index = -1;

//        // Start from 0 from the leftmost digit //
//        int multiplier = (int) (1 * Math.pow(10, length-1));
//        int reverse= 0;
//        for (int i = 0; i < length; i++) {
//            temp = n%10;
//            reverse = reverse + temp * multiplier;
//            n = n/10;
//            multiplier = multiplier/10;
//        }
//        for (int j = 0; j < length; j++) {
//            temp = reverse % 10;
//            if (temp == digit) {
//                index = j;
//            }
//            reverse = reverse / 10;
//        }
        
        // Start from 1 from the rightmost digit //
        for (int j = 1; j <= length; j++) {
            temp = n % 10;
            if (temp == digit) {
                index = j;
                break;
            }
            n = n / 10;
        }
        return index;
    }

    public static long extractOddDigits(long n) {
        int m = (int) n;
        int length = countDigits(m);
        int multiplier = 1;
        int total = 0;
        if (n < 0) {
            total = -1;
        }
        else {
            for (int i = 0; i < length; i++) {
                if (n % 10 % 2 == 1) {
                    total = (int) (total + n % 10 * multiplier);
                    multiplier = multiplier * 10;
                }
                n = n / 10;
            }
        }
        return total;
    }
}