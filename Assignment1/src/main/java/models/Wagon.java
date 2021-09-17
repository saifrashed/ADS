package models;

import java.util.ArrayList;

public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return getNextWagon() != null;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return getPreviousWagon() != null;
    }

    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     *
     * @return the wagon
     */
    public Wagon getLastWagonAttached() {
        // TODO find the last wagon in the sequence


        // traverse sequence for last wagon
        Wagon tempWagon = this;
        while (tempWagon.getNextWagon() != null) {
            tempWagon = tempWagon.getNextWagon();
        }

        return tempWagon;
    }

    /**
     * @return the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        // TODO traverse the tail and find its length

        // initialise length variable and temporary wagon variable
        int length = 0;
        Wagon tempWagon = this;

        // loop through all connected wagons
        while (tempWagon.getNextWagon() != null) {
            length++;
            tempWagon = tempWagon.getNextWagon();
        }

        return length;
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     *
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        // TODO verify the exceptions

        // check if current wagon already has a wagon connected and if tail wagon has one infront
        if (hasNextWagon() || tail.hasPreviousWagon()) {

            // initialise exception message and temporary wagon variable
            StringBuilder exceptionMsg = new StringBuilder();
            Wagon tempWagon = tail.getLastWagonAttached();

            // loop through all previous wagons
            while (tempWagon != null) {
                exceptionMsg.append(tempWagon.toString()).append(", ");
                tempWagon = tempWagon.getPreviousWagon();
            }
            ;

            // throw exception
            throw new IllegalStateException(exceptionMsg.toString());
        }

        this.setNextWagon(tail);
        tail.setPreviousWagon(this);

        // TODO attach the tail wagon to this wagon (sustaining the invariant propositions).
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        // TODO detach the tail from this wagon (sustaining the invariant propositions).
        //  and return the head wagon of that tail

        final Wagon DETACHING_TAIL = this.getNextWagon() != null ? this.getNextWagon() : null;

        // checking invariant propositions and setting new properties
        if (DETACHING_TAIL != null) {
            DETACHING_TAIL.setPreviousWagon(null);
            setNextWagon(null);
        }

        return DETACHING_TAIL;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        // TODO detach this wagon from its predecessor (sustaining the invariant propositions).
        //   and return that predecessor

        final Wagon DETACHING_FRONT = this.getPreviousWagon() != null ? this.getPreviousWagon() : null;

        // checking invariant propositions and setting new properties
        if (DETACHING_FRONT != null) {
            DETACHING_FRONT.setNextWagon(null);
            setPreviousWagon(null);
        }

        return DETACHING_FRONT;
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     *
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        // TODO detach any existing connections that will be rearranged

        final Wagon FRONT_WAGON = front;
        final Wagon REATTACHED_WAGON = this;

        // separate this wagon from previous sequence
        REATTACHED_WAGON.getPreviousWagon().setNextWagon(null);

        // TODO attach this wagon to its new predecessor front (sustaining the invariant propositions).

        // link this wagon to new front
        REATTACHED_WAGON.setPreviousWagon(FRONT_WAGON);
        FRONT_WAGON.setNextWagon(REATTACHED_WAGON);
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        // TODO

        final Wagon CURRENT = this;
        final Wagon HEAD = getPreviousWagon();
        final Wagon TAIL = getNextWagon();

        // check if there is a head wagon and connect it to the tail
        if (HEAD != null) {
            HEAD.setNextWagon(TAIL);
            CURRENT.setPreviousWagon(null);
        }

        // check if there is a tail wagon and connect it to the head
        if (TAIL != null) {
            TAIL.setPreviousWagon(HEAD);
            CURRENT.setNextWagon(null);
        }
    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        // TODO provide an iterative implementation,
        //   using attach- and detach methods of this class


        int tailLength = getTailLength() + 1;
        ArrayList<Wagon> reversedSequence = new ArrayList<>();

        Wagon headWagon = getPreviousWagon();
        Wagon lastWagon = getLastWagonAttached();

        // fill array with in a reversed sequence
        for (int i = 0; i < tailLength; i++) {
            reversedSequence.add(lastWagon);
            getLastWagonAttached().detachFront();
            getLastWagonAttached().detachTail();
            lastWagon = getLastWagonAttached();
        }

        Wagon temp = headWagon; // Temporary pointer

        // loop through reversed sequence array and link all wagons together
        for (Wagon wagon : reversedSequence) {
            if(temp != null) {
                temp.attachTail(wagon);
            }
            temp = wagon;
        }


        return reversedSequence.get(0);
    }

    // TODO
}
