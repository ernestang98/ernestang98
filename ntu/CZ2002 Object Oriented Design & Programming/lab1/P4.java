package lab1;

import java.util.Scanner;

public class P4 {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Height Value: ");
        int h = sc.nextInt();

        if (h < 1) {
            System.out.print("Error!!!!!");
        }
        else {
            for (int r = 0; r < h; r++) {
                for (int c = 0; c <= r; c++) {
                    // row controls index of y axis //
                    // column controls index of x axis //
                    if (r % 2 == 0) {
                        if (c % 2 == 0) System.out.print("AA");
                        else System.out.print("BB");
                    }
                    else {
                        if (c % 2 == 0) System.out.print("BB");
                        else System.out.print("AA");
                    }
                }
                System.out.println();
            }
        }
    }
}
