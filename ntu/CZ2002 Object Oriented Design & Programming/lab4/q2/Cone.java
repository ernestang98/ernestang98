package lab4.q2;

public class Cone implements Shape {
    private int radius;
    private int length;

    public Cone(int radius, int length) {
        this.radius = radius;
        this.length = length;
    }

    @Override
    public double area() {
        return 3.14 * radius * length;
    }
}
