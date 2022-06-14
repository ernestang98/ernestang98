package lab4.q2;

public class Square implements Shape{
    private int length;

    public Square(int length) {
        this.length = length;
    }

    @Override
    public double area() {
        return length * length;
    }
}
