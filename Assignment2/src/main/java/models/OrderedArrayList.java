package models;

import java.util.*;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E>
        extends ArrayList<E>
        implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    protected int nSorted;                      // the number of items that have been ordered by barcode in the list
    // representation-invariant
    //      all items at index positions 0 <= index < nSorted have been ordered by the given ordening comparator
    //      other items at index position nSorted <= index < size() can be in any order amongst themselves
    //              and also relative to the sorted section

    public OrderedArrayList() {
        this(null);
    }

    public OrderedArrayList(Comparator<? super E> ordening) {
        super();
        this.ordening = ordening;
        this.nSorted = 0;
    }

    public Comparator<? super E> getOrdening() {
        return this.ordening;
    }

    @Override
    public void clear() {
        super.clear();
        this.nSorted = 0;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);
        this.ordening = c;
        this.nSorted = this.size();
    }

    // TODO override the ArrayList.add(index, item), ArrayList.remove(index) and Collection.remove(object) methods
    //  such that they sustain the representation invariant of OrderedArrayList
    //  (hint: only change nSorted as required to guarantee the representation invariant, do not invoke a sort)

    @Override
    public boolean remove(Object o) {

        // check if the to be removed object falls within the sorted sequence. If so decrement nSorted size
        if(indexOf(o) != -1 && indexOf(o) < nSorted) {
            nSorted--;
        }

        return super.remove(o);
    }

    @Override
    public boolean add(E e) {
        return super.add(e);
    }

    @Override
    public void sort() {
        if (this.nSorted < this.size()) {
            this.sort(this.ordening);
        }
    }

    @Override
    public int indexOf(Object item) {
        if (item != null) {
            return indexOfByBinarySearch((E) item);
        } else {
            return -1;
        }
    }

    @Override
    public int indexOfByBinarySearch(E searchItem) {
        if (searchItem != null) {
            // some arbitrary choice to use the iterative or the recursive version
            return indexOfByRecursiveBinarySearch(searchItem);
        } else {
            return -1;
        }
    }

    /**
     * finds the position of the searchItem by an iterative binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByIterativeBinarySearch(E searchItem) {

        // TODO implement an iterative binary search on the sorted section of the arrayList, 0 <= index < nSorted
        //   to find the position of an item that matches searchItem (this.ordening comparator yields a 0 result)

        if(size() == -1) return -1; // check if list is empty

        // iterative binary search
        int low = 0;
        int high = nSorted - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (this.ordening.compare(this.get(mid), searchItem) < 0) {
                low = mid + 1;
            } else if (this.ordening.compare(this.get(mid), searchItem) > 0) {
                high = mid - 1;
            } else if (this.ordening.compare(this.get(mid), searchItem) == 0) {
                return mid;
            }
        }
        // TODO if no match was found, attempt a linear search of searchItem in the section nSorted <= index < size()


        // linear search in unsorted section
        for (int ndx = nSorted; ndx < this.size(); ndx++)
            if (this.ordening.compare(this.get(ndx), searchItem) == 0) return ndx;


        return -1;
    }

    /**
     * finds the position of the searchItem by a recursive binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByRecursiveBinarySearch(E searchItem) {

        if(size() == -1) return -1; // check if list is empty

        int recursiveIndex = indexOfRecursiveHelper(0, nSorted, searchItem);

        if (recursiveIndex != -1) {
            return recursiveIndex;
        }

        for (int ndx = nSorted; ndx < this.size(); ndx++)
            if (this.ordening.compare(this.get(ndx), searchItem) == 0) return ndx;

        return -1;
    }

    public int indexOfRecursiveHelper(int l, int r, E item) {
        //Restrict the boundary of right index
        // and the left index to prevent
        // overflow of indices.
        if (r >= l && l < nSorted) {

            int mid = l + (r - l) / 2;

            // If the element is present
            // at the middle itself
            if (this.ordening.compare(this.get(mid), item) == 0)
                return mid;

            // If element is smaller than mid, then it can only
            // be present in left subarray
            if (this.ordening.compare(this.get(mid), item) > 0)
                return indexOfRecursiveHelper(l, mid - 1, item);

            // Else the element can only be present in right
            // subarray
            return indexOfRecursiveHelper(mid + 1, r, item);
        }

        // We reach here when element is not present in array
        return -1;
    }


    /**
     * finds a match of newItem in the list and applies the merger operator with the newItem to that match
     * i.e. the found match is replaced by the outcome of the merge between the match and the newItem
     * If no match is found in the list, the newItem is added to the list.
     *
     * @param newItem
     * @param merger  a function that takes two items and returns an item that contains the merged content of
     *                the two items according to some merging rule.
     *                e.g. a merger could add the value of attribute X of the second item
     *                to attribute X of the first item and then return the first item
     * @return whether a new item was added to the list or not
     */
    @Override
    public boolean merge(E newItem, BinaryOperator<E> merger) {

        if (newItem == null) return false;
        int matchedItemIndex = this.indexOfByRecursiveBinarySearch(newItem);

        if (matchedItemIndex < 0) {
            add(newItem);
            return true;
        } else {
            // TODO retrieve the matched item and
            //  replace the matched item in the list with the merger of the matched item and the newItem
            E mergedItem = merger.apply(get(matchedItemIndex), newItem);
            remove(matchedItemIndex);
            add(matchedItemIndex, mergedItem);

            return false;
        }
    }
}
