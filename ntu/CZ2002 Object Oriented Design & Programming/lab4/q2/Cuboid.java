package lab4.q2;

public class Cuboid implements Shape {
    private int length;
    private int breadth;

    public Cuboid(int length, int breadth) {
        this.length = length;
        this.breadth = breadth;
    }

    @Override
    public double area() {
        Rectangle rectangle = new Rectangle(length, breadth);
        return 4 * rectangle.area() + new Square(length).area();
    }
}
