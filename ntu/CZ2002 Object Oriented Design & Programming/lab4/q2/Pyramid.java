package lab4.q2;

public class Pyramid implements Shape{
    private int base;
    private int height;

    public Pyramid(int base, int height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        Triangle triangle = new Triangle(base, height);
        return 4 * triangle.area();
    }
}
