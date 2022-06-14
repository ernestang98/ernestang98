package lab4.q2;

import java.util.Scanner;

public class Shape3DApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalNumberOfShapes;
        int choice;
        double tt3DArea = 0;
        System.out.print("Enter number of shapes to compute total area: ");
        totalNumberOfShapes = sc.nextInt();
        Shape[] shapesArr = new Shape[totalNumberOfShapes];

        for (int b = 0; b < totalNumberOfShapes; b++)
        {
            System.out.printf("1. Cube\n" +
                              "2. Cuboid\n" +
                              "3. Sphere\n" +
                              "4. Pyramid\n" +
                              "5. Cone\n" +
                              "6. Cylinder\n" + "\n" +
                              "Select shape %d: ", (b+1));
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Length: ");
                    int length = sc.nextInt();
                    shapesArr[b] = new Cube(length);
                    break;
                case 2:
                    System.out.print("Length: ");
                    length = sc.nextInt();
                    System.out.print("Breadth: ");
                    int breadth = sc.nextInt();
                    shapesArr[b] = new Cuboid(length, breadth);
                    break;
                case 3:
                    System.out.print("Radius: ");
                    int radius = sc.nextInt();
                    shapesArr[b] = new Sphere(radius);
                    break;
                case 4:
                    System.out.print("Base: ");
                    int base = sc.nextInt();
                    System.out.print("Height: ");
                    int height = sc.nextInt();
                    shapesArr[b] = new Pyramid(base, height);
                    break;
                case 5:
                    System.out.print("Base: ");
                    base = sc.nextInt();
                    System.out.print("Length: ");
                    length = sc.nextInt();
                    shapesArr[b] = new Cone(base, length);
                    break;
                case 6:
                    System.out.print("Radius: ");
                    radius = sc.nextInt();
                    System.out.print("Length: ");
                    length = sc.nextInt();
                    shapesArr[b] = new Cylinder(radius, length);
                    break;
            }
        }

        for (int i=0; i < totalNumberOfShapes; i++) {
            tt3DArea += shapesArr[i].area();
        }

        System.out.printf("\nTotal 3D Surface Area: %.2f", tt3DArea);
    }
}
