package lab4.q2;
import java.util.Scanner;

public class Shape2DApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalNumberOfShapes;
        int choice;
        double ttl2DArea = 0;
        System.out.print("Enter number of shapes to compute total area: ");
        totalNumberOfShapes = sc.nextInt();
        Shape[] shapesArr = new Shape[totalNumberOfShapes];

        for (int a = 0; a < totalNumberOfShapes; a++) {
            System.out.printf("1. Square\n" +
                              "2. Rectangle\n" +
                              "3. Circle\n" +
                              "4. Triangle\n" + "\n" +
                              "Select shape %d: ", (a+1));
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter length: ");
                    int length = sc.nextInt();
                    shapesArr[a] = new Square(length);
                    break;
                case 2:
                    System.out.print("Enter length: ");
                    length = sc.nextInt();
                    System.out.print("Enter breadth: ");
                    int breadth = sc.nextInt();
                    shapesArr[a] = new Rectangle(length, breadth);
                    break;
                case 3:
                    System.out.print("Enter radius: ");
                    int radius = sc.nextInt();
                    shapesArr[a] = new Circle(radius);
                    break;
                case 4:
                    System.out.print("Enter base: ");
                    int base = sc.nextInt();
                    System.out.print("Enter height: ");
                    int height = sc.nextInt();
                    shapesArr[a] = new Triangle(base, height);
                    break;
            }
        }

        for (int i=0; i < totalNumberOfShapes; i++) {
            ttl2DArea += shapesArr[i].area();
        }

        System.out.printf("\nTotal 2D Area: %.2f", ttl2DArea);

    }
}
