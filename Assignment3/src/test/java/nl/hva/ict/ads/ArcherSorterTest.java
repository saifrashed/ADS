package nl.hva.ict.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.ToLongFunction;
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


//    @Test
//    void measureSorting() {
//
//        long maxProblemSize = 1000000000;
//
//        SorterImpl<Long> sorter = new SorterImpl<>();
//
//
//        for (long problemSize = 100; problemSize < maxProblemSize; problemSize = problemSize * 2) {
//
//            // creation of list
//            List<Long> numberList = new ArrayList<>();
//            long started, finished;
//
//            for (long j = 0; j < problemSize; j++) {
//                numberList.add(j);
//            }
//
//            Collections.shuffle(numberList);
//            // end creation of list
//
//
//            // measurement
//            started = System.nanoTime();
//            sorter.topsHeapSort((int) problemSize, numberList, Long::compareTo);
////            sorter.quickSort(numberList, Long::compareTo);
////            sorter.selInsSort(numberList, Long::compareTo);
//            finished = System.nanoTime();
//
//            System.out.printf("Measurment of problem size (%d) %.3f msec\n", problemSize, (finished - started) / 1E6);
//        }
//    }
}
