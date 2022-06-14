package lab3;

public class Plane {
    public PlaneSeat[] seat;
    public int numEmptySeats;
    public Plane() {
        this.numEmptySeats = 12;
        this.seat = new PlaneSeat[12];
        for (int i = 0; i < this.numEmptySeats; i++) {
            this.seat[i] = new PlaneSeat(i+1);
        }
    }
    public PlaneSeat[] sortSeats() {
        PlaneSeat[] copy = new PlaneSeat[12];
        PlaneSeat temp;
        System.arraycopy(this.seat, 0, copy, 0, 12);
        int n = 12;
        for (int x = 0; x < n-1; x++) {
            for (int y = 0; y < n-1; y++) {
                if (copy[y].getCustomerID() > copy[y+1].getCustomerID()) {
                    temp = copy[y];
                    copy[y] = copy[y+1];
                    copy[y+1] = temp;
                }
            }
        }
        return copy;
    }
    public void showNumEmptySeats() {
        System.out.printf("The number of empty seats is: %d\n\n", this.numEmptySeats);
    }
    public void showEmptySeats(){
        PlaneSeat[] copy = this.seat;
        System.out.println("The following seats are empty:");
        for (int z = 0; z < 12; z++) {
            if (!copy[z].isOccupied()) {
                System.out.printf("SeatID: %d\n", copy[z].getSeatID());
            }
        }
        System.out.println();
    }
    public void showAssignedSeats(boolean bySeatID){
        System.out.println("The following seats are occupied:");
        if (bySeatID) {
            int n = 12;
            PlaneSeat temp;
            for (int x = 0; x < n-1; x++) {
                for (int y = 0; y < n-1; y++) {
                    if (this.seat[y].getSeatID() > this.seat[y+1].getSeatID()) {
                        temp = this.seat[y];
                        this.seat[y] = this.seat[y+1];
                        this.seat[y+1] = temp;
                    }
                }
            }
            for (int z = 0; z < 12; z++) {
                if (this.seat[z].isOccupied()) {
                    System.out.printf("SeatID %d is assigned to CustomerID %d\n",
                            this.seat[z].getSeatID(), this.seat[z].getCustomerID());
                }
            }
        }
        else {
            PlaneSeat[] sorted = sortSeats();
            int length = sorted.length;
            for (int z = 0; z < length; z++) {
                if (sorted[z].isOccupied()) {
                    System.out.printf("SeatID %d is assigned to CustomerID %d\n",
                            sorted[z].getSeatID(), sorted[z].getCustomerID());
                }
            }
        }
        System.out.println();
    }
    public void assignSeat(int seatID, int custID) {
        if (!this.seat[seatID - 1].isOccupied()){
            this.seat[seatID - 1].assign(custID);
            System.out.println("Seat Assigned!\n");
            this.numEmptySeats -= 1;
        }
        else {
            System.out.println("Seat already assigned to a customer.\n");
        }
    }
    public void unAssignSeat(int seatID) {
        if (this.seat[seatID - 1].isOccupied()){
            this.seat[seatID - 1].unAssign();
            System.out.println("Seat unassigned!\n");
            this.numEmptySeats += 1;
        }
        else {
            System.out.println("Seat is already unassigned!\n");
        }
    }
}