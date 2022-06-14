package lab3;
import java.util.Scanner;

public class PlaneApp {
    public static void main(String[] args) {
        Plane create_plane = new Plane();
        int choice;
        Scanner sc = new Scanner(System.in); do {
            System.out.println("List of options:");
            System.out.println("1: Show number of empty seats");
            System.out.println("2: Show the list of empty seats");
            System.out.println("3: Show the list of seat assignments by seat ID");
            System.out.println("4: Show the list of seat assignments by customer ID");
            System.out.println("5: Assign a customer to a seat");
            System.out.println("6: Remove a seat assignment");
            System.out.println("7: Exit");
            System.out.print("Enter the number of your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    create_plane.showNumEmptySeats();
                    break;
                case 2:
                    create_plane.showEmptySeats();
                    break;
                case 3:
                    create_plane.showAssignedSeats(true);
                    break;
                case 4:
                    create_plane.showAssignedSeats(false);
                    break;
                case 5:
                    System.out.println("Assigning Seat...");
                    System.out.print("  Please enter SeatID: ");
                    int SeatID = sc.nextInt();
                    System.out.print("  Please enter CustID: ");
                    int CustID = sc.nextInt();
                    create_plane.assignSeat(SeatID, CustID);
                    break;
                case 6:
                    System.out.print("Enter SeatID to unassign customer from: ");
                    int SeatID_rm = sc.nextInt();
                    create_plane.unAssignSeat(SeatID_rm);
                    break;
                case 7:
                    System.out.println("Program terminating ....");
            }
        } while (choice < 7);
    }
}