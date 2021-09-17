package models;

/**
 * Class PassengerWagon
 */
public class PassengerWagon extends Wagon {
    private int wagonId;
    private int numberOfSeats;

    public PassengerWagon(int wagonId, int numberOfSeats) {
        super(wagonId);
        this.wagonId = wagonId;
        this.numberOfSeats = numberOfSeats;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    @Override
    public String toString() {
        return "[Wagon-" + wagonId + "]";
    }
}
