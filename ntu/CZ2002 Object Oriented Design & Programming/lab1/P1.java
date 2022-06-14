package lab1;
import java.util.Scanner;

public class P1{

    public static void main (String[] args) {
        System.out.print("Enter character: ");
        Scanner sc = new Scanner(System.in);
        char c = sc.next().charAt(0);
        switch (c) {
            case 'a':
            case 'A':
                System.out.println("Action movie fan\n");
                break;
            case 'c':
            case 'C':
                System.out.println("Comedy movie fan\n");
                break;
            case 'd':
            case 'D':
                System.out.println("Drama movie fan\n");
                break;
            default:
                System.out.println("Invalid choice\n");
        }



    }
}
