package models;

import java.util.ArrayList;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /* three helper methods that are usefull in other methods */
    public boolean hasWagons() {
        return getFirstWagon() != null;
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {
        this.firstWagon = wagon;
    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        return firstWagon != null ? (firstWagon.getTailLength() + 1) : 0;
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        return firstWagon != null ? firstWagon.getLastWagonAttached() : null;
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        // TODO

        // check instance of first wagon
        if (!(firstWagon instanceof PassengerWagon)) {
            return 0;
        }

        // initialise length variable and temporary wagon variable
        int numberOfSeats = 0;

        // loop through all connected wagons
        for (Wagon temp = firstWagon; temp != null; temp = temp.getNextWagon()) {
            numberOfSeats += ((PassengerWagon) temp).getNumberOfSeats();
        }

        return numberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        // TODO

        // check instance of first wagon
        if (!(firstWagon instanceof FreightWagon)) {
            return 0;
        }

        // initialise length variable and temporary wagon variable
        int totalMaxWeight = 0;

        // loop through all connected wagons
        for (Wagon temp = firstWagon; temp != null; temp = temp.getNextWagon()) {
            totalMaxWeight += ((FreightWagon) temp).getMaxWeight();
        }

        return totalMaxWeight;
    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        // TODO

        int counter = 1;
        Wagon tempWagon = firstWagon;

        // loop through all connected wagons
        while (tempWagon != null && counter != position) {
            counter++;
            tempWagon = tempWagon.getNextWagon();
        }

        return tempWagon;
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        // TODO

        if (firstWagon != null) {
            Wagon tempWagon = firstWagon;

            // loop through all connected wagons
            while (tempWagon != null && tempWagon.getId() != wagonId) {
                tempWagon = tempWagon.getNextWagon();
            }

            return tempWagon;
        } else {
            return null;
        }
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return
     */
    public boolean canAttach(Wagon wagon) {
        // TODO

        if (hasWagons()) {
            int currentAmount = (firstWagon.getTailLength() + 1);
            int toBeAdded = (wagon.getTailLength() + 1);
            int maxWagons = getEngine().getMaxWagons();

            // check capacity
            if ((currentAmount + toBeAdded) > maxWagons) {
                return false;
            }

            // check type of train
            if (wagon.getClass() != firstWagon.getLastWagonAttached().getClass()) {
                return false;
            }

            // check duplicate wagons
            return arrayTrain().indexOf(wagon) == -1;

        }

        return true;
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {

        if (!canAttach(wagon)) {
            return false;
        }

        if (hasWagons()) {
            wagon.detachFront();
            getFirstWagon().getLastWagonAttached().attachTail(wagon);
        } else {
            setFirstWagon(wagon);
        }

        return true;
    }

    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        // TODO

        if (!canAttach(wagon)) {
            return false;
        }

        if (firstWagon != null) {
            wagon.getLastWagonAttached().attachTail(firstWagon);
        }

        setFirstWagon(wagon);


        return true;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        // TODO

        Wagon currentWagon = findWagonAtPosition(position);
        Wagon insertWagon = wagon;

        if (firstWagon == null && position == 1) {
            setFirstWagon(wagon);
            return true;
        } else {
            if (currentWagon != null) {
                currentWagon.getPreviousWagon().attachTail(insertWagon);
                currentWagon.reAttachTo(insertWagon);
                return true;
            }
            return false;
        }
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        // TODO

        Wagon wagon = findWagonById(wagonId);

        if (wagon == null || !toTrain.canAttach(wagon)) {
            return false;
        }

        // set variables for head and tail
        Wagon head = wagon.getPreviousWagon() != null ? wagon.getPreviousWagon() : null;
        Wagon tail = wagon.getNextWagon() != null ? wagon.getNextWagon() : null;

        // detach current selected wagon from train
        if (wagon.hasPreviousWagon()) {
            wagon.detachFront();
            head.detachTail();
        }

        // check if the sequence has to be reconnected or if there has to be a new firstwagon
        if (wagon.hasNextWagon()) {
            wagon.detachTail();
            tail.detachFront();

            if (head != null) {
                head.attachTail(tail);
            } else {
                setFirstWagon(tail);
            }
        }

        // check if new train has wagons for it to be on the end or beginning
        if (toTrain.hasWagons()) {
            toTrain.getLastWagonAttached().attachTail(wagon);
        } else {
            toTrain.setFirstWagon(wagon);
        }

        return true;
    }

    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        // TODO


        if (hasWagons()) {
            Wagon splitWagon = findWagonAtPosition(position);
            Wagon prevHead = splitWagon.getPreviousWagon();

            if (toTrain.canAttach(splitWagon)) {
                // check if is not firstwagon
                if (prevHead != null) {
                    splitWagon.detachFront();
                    prevHead.detachTail();
                } else { // if it is firstwagon
                    setFirstWagon(null);
                }

                if(!toTrain.hasWagons()) {
                    toTrain.setFirstWagon(splitWagon);
                } else {
                    toTrain.getLastWagonAttached().attachTail(splitWagon);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        // TODO

        if (firstWagon != null) {
            Wagon newFirstWagon = firstWagon.reverseSequence();
            setFirstWagon(newFirstWagon);
        }
    }


    /**
     * Converts the the train sequence to an array to allow different kinds of operations.
     *
     * @return
     */
    public ArrayList<Wagon> arrayTrain() {
        ArrayList<Wagon> wagons = new ArrayList<>();

        for (Wagon wagon = getFirstWagon(); wagon != null; wagon = wagon.getNextWagon()) {
            wagons.add(wagon);
        }

        return wagons;
    }


    @Override
    public String toString() {
        StringBuilder trainString = new StringBuilder();
        ArrayList<Wagon> wagons = arrayTrain();

        trainString.append(engine.toString()); // get the engine first

        for (Wagon wagon : wagons) {
            trainString.append(wagon.toString());
        }

        trainString.append(" With ").append(firstWagon.getTailLength() + 1).append(" wagons ");
        trainString.append("from ").append(origin).append(" to ").append(destination);


        return trainString.toString();
    }
}
