package models;

/**
 * Class Freight wagon
 */
public class FreightWagon extends Wagon {
    private int wagonId;
    private int maxWeight;

    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);
        this.wagonId = wagonId;
        this.maxWeight = maxWeight;
    }

    public int getWagonId() {
        return wagonId;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setWagonId(int wagonId) {
        this.wagonId = wagonId;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString() {
        return "[Wagon-"+wagonId+"]";
    }
}
