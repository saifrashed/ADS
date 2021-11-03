package nl.hva.ict.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArcherSorterTest {
    protected Sorter<Archer> sorter = new ArcherSorter();
    protected List<Archer> fewArchers;
    protected List<Archer> manyArchers;
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;

    @BeforeEach
    void setup() {
        ChampionSelector championSelector = new ChampionSelector(1L);
        fewArchers = new ArrayList(championSelector.enrollArchers(23));
        manyArchers = new ArrayList(championSelector.enrollArchers(250));
    }

    @Test
    void selInsSortAndCollectionSortResultInSameOrder() {
        List<Archer> fewSortedArchers = new ArrayList<>(fewArchers);
        List<Archer> manySortedArchers = new ArrayList<>(manyArchers);

        Collections.shuffle(fewSortedArchers);
        sorter.selInsSort(fewSortedArchers, Comparator.comparing(Archer::getId));
        fewArchers.sort(Comparator.comparing(Archer::getId));
        assertEquals(fewArchers, fewSortedArchers);

        sorter.selInsSort(manySortedArchers, Comparator.comparing(Archer::getLastName));
        manyArchers.sort(Comparator.comparing(Archer::getLastName));
        assertEquals(manyArchers.stream().map(Archer::getLastName).collect(Collectors.toList()),
                manySortedArchers.stream().map(Archer::getLastName).collect(Collectors.toList()));

        sorter.selInsSort(fewSortedArchers, scoringScheme);
        fewArchers.sort(scoringScheme);
        assertEquals(fewArchers, fewSortedArchers);

        sorter.selInsSort(manySortedArchers, scoringScheme);
        manyArchers.sort(scoringScheme);
        assertEquals(manyArchers, manySortedArchers);
    }

    @Test
    void quickSortAndCollectionSortResultInSameOrder() {
        List<Archer> fewSortedArchers = new ArrayList<>(fewArchers);
        List<Archer> manySortedArchers = new ArrayList<>(manyArchers);

        Collections.shuffle(fewSortedArchers);
        sorter.quickSort(fewSortedArchers, Comparator.comparing(Archer::getId));
        fewArchers.sort(Comparator.comparing(Archer::getId));
        assertEquals(fewArchers, fewSortedArchers);

        sorter.quickSort(manySortedArchers, Comparator.comparing(Archer::getLastName));
        manyArchers.sort(Comparator.comparing(Archer::getLastName));
        assertEquals(manyArchers.stream().map(Archer::getLastName).collect(Collectors.toList()),
                manySortedArchers.stream().map(Archer::getLastName).collect(Collectors.toList()));

        sorter.quickSort(fewSortedArchers, scoringScheme);
        fewArchers.sort(scoringScheme);
        assertEquals(fewArchers, fewSortedArchers);

        sorter.quickSort(manySortedArchers, scoringScheme);
        manyArchers.sort(scoringScheme);
        assertEquals(manyArchers, manySortedArchers);
    }

    @Test
    void topsHeapSortAndCollectionSortResultInSameOrder() {
        List<Archer> fewSortedArchers = new ArrayList<>(fewArchers);
        List<Archer> manySortedArchers = new ArrayList<>(manyArchers);

        Collections.shuffle(fewSortedArchers);
        sorter.topsHeapSort(5, fewSortedArchers, Comparator.comparing(Archer::getId));
        fewArchers.sort(Comparator.comparing(Archer::getId));
        assertEquals(fewArchers.subList(0, 5), fewSortedArchers.subList(0, 5));

        sorter.topsHeapSort(1, manySortedArchers, scoringScheme);
        manyArchers.sort(scoringScheme);

        assertEquals(manyArchers.get(0), manySortedArchers.get(0));


        sorter.topsHeapSort(25, manySortedArchers, scoringScheme);
        assertEquals(manyArchers.subList(0, 25), manySortedArchers.subList(0, 25));
    }


    @Test
    void testHeapSwim() {

        SorterImpl<Integer> sorter = new SorterImpl<>();

        List<Integer> numberList = new ArrayList<>();

        for (int i = 0; i < 250; i++) {
            numberList.add(i);
        }

        Collections.shuffle(numberList);

        sorter.topsHeapSort(252, numberList, Integer::compareTo);


        Integer[] array = numberList.toArray(new Integer[0]);

        printHeap(array, numberList.size() - 1);
    }

    /**
     * Display and visualise a heap data structure
     *
     * @param heap
     * @param size
     */
    private void printHeap(Integer[] heap, int size) {
        int maxDepth = (int) (Math.log(size) / Math.log(2));  // log base 2 of n

        StringBuilder hs = new StringBuilder();  // heap string builder
        for (int d = maxDepth; d >= 0; d--) {  // number of layers, we build this backwards
            int layerLength = (int) Math.pow(2, d);  // numbers per layer

            StringBuilder line = new StringBuilder();  // line string builder
            for (int i = layerLength; i < (int) Math.pow(2, d + 1); i++) {
                // before spaces only on not-last layer
                if (d != maxDepth) {
                    line.append(" ".repeat((int) Math.pow(2, maxDepth - d)));
                }
                // extra spaces for long lines
                int loops = maxDepth - d;
                if (loops >= 2) {
                    loops -= 2;
                    while (loops >= 0) {
                        line.append(" ".repeat((int) Math.pow(2, loops)));
                        loops--;
                    }
                }

                // add in the number
                if (i <= size) {
                    line.append(String.format("%-2s", heap[i]));  // add leading zeros
                } else {
                    line.append("--");
                }

                line.append(" ".repeat((int) Math.pow(2, maxDepth - d)));  // after spaces
                // extra spaces for long lines
                loops = maxDepth - d;
                if (loops >= 2) {
                    loops -= 2;
                    while (loops >= 0) {
                        line.append(" ".repeat((int) Math.pow(2, loops)));
                        loops--;
                    }
                }
            }
            hs.insert(0, line.toString() + "\n");  // prepend line
        }
        System.out.println(hs.toString());
    }

}
