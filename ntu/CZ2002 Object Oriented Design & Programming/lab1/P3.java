package lab1;

import java.util.Scanner;

public class P3 {

    private static double conversion(double x) {
       return x * 1.82;
    }

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Starting Value: ");
        int f = sc.nextInt();
        System.out.print("Enter Ending Value: ");
        int l = sc.nextInt();
        System.out.print("Enter Incrementing Value: ");
        int i = sc.nextInt();
        System.out.println();
        System.out.println();

        if (f > l) {
            System.out.print("Error Input!!!!");
        }
        else {
            System.out.println("For Loop:");
            System.out.println("US$         S$");
            System.out.println("------------");
            for (int a = f; a <= l; a+=i) {
                double b = conversion(a);
                String str = String.format("%d          %.1f", a, b);
                System.out.println(str);

            }
            System.out.println();
            System.out.println();

            System.out.println("While Loop:");
            System.out.println("US$         S$");
            System.out.println("------------");
            int b = f;
            while (b <= l) {
                double c = conversion(b);
                String str = String.format("%d          %.1f", b, c);
                System.out.println(str);
                b += i;
            }
            System.out.println();
            System.out.println();

            System.out.println("Do While Loop:");
            System.out.println("US$         S$");
            System.out.println("------------");
            do {
                double d = conversion(f);
                String str = String.format("%d          %.1f", f, d);
                System.out.println(str);
                f += i;
            } while (f <= l);

        }
    }
}
