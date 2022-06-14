package lab4.q1;

public class SalePerson implements Comparable {
    private String fN, lN;
    private int ttSales;

    public SalePerson(String fN, String lN, int ttSales) {
        this.fN = fN;
        this.lN = lN;
        this.ttSales = ttSales;
    }

    public String toString() {

        return String.format("%s, %s : %d", fN, lN, ttSales);
    }

    public boolean equals(Object o) {
        return (((SalePerson) o).getFN().equals(fN) && ((SalePerson) o).getLN().equals(fN));
    }

    public int compareTo(Object o) {
        if (ttSales < ((SalePerson) o).getTTS()) {
            return -1;
        }
        else if (ttSales > ((SalePerson) o).getTTS()) {
            return 1;
        }
        else {
            if (lN.compareTo(((SalePerson) o).getLN()) < 0) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    public String getFN() {
        return fN;
    }

    public String getLN() {
        return lN;
    }

    public int getTTS() {
        return ttSales;
    }
}
