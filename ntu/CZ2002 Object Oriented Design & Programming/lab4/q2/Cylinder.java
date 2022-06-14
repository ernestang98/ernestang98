package lab4.q2;

public class Cylinder implements Shape{
    private int radius;
    private int length;

    public Cylinder(int radius, int length) {
        this.radius = radius;
        this.length = length;
    }

    @Override
    public double area() {
        Circle circle = new Circle(radius);
        return circle.area() + (circle.area() * length);
    }
}
