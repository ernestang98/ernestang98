package lab3;

public class PlaneSeat {
    public int seatID;
    public boolean assigned = false;
    public int customerID = 0;

    public PlaneSeat (int seatID) { this.seatID = seatID; }
    public int getSeatID() {
        return this.seatID;
    }
    public int getCustomerID() {
        return this.customerID;
    }
    public boolean isOccupied() {
        return this.assigned;
    }
    public void assign(int customerID){
        this.customerID = customerID;
        this.assigned = true;
    }
    public void unAssign() {
        this.customerID = 0;
        this.assigned = false;
    }
}