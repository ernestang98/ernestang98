package lab1;

import java.util.Scanner;

public class P2 {
    public static void main (String[] args) {
        System.out.print("Enter Salary: ");
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        System.out.print("Enter Merit Points: ");
        int m = sc.nextInt();
        if (i <= 649) {
            if (i < 600) System.out.println("Grade C");
            else {
                if (m < 10) System.out.println("Grade C");
                else System.out.println("Grade B");
            }
        } else if (i <= 799) {
            if (i < 700) System.out.println("Grade B");
            else {
                if (m < 20) System.out.println("Grade B");
                else System.out.println("Grade A");
            }
        }
        else {
            System.out.println("Grade A");
        }

    }
}
