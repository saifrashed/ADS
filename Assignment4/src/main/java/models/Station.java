package models;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Station {
    private final int stn;
    private final String name;
    private NavigableMap<LocalDate, Measurement> measurements = new TreeMap<>();

    public Station(int id, String name) {
        this.stn = id;
        this.name = name;
        // TODO initialize the measurements data structure with a suitable implementation class.
    }

    public Collection<Measurement> getMeasurements() {
        // TODO return the measurements of this station

        return measurements.values();
    }

    public int getStn() {
        return stn;
    }

    public String getName() {
        return name;
    }

    /**
     * import station number and name from a text line
     *
     * @param textLine
     * @return a new Station instance for this data
     * or null if the data format does not comply
     */
    public static Station fromLine(String textLine) {
        String[] fields = textLine.split(",");
        if (fields.length < 2) return null;
        try {
            return new Station(Integer.parseInt(fields[0].trim()), fields[1].trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Add a collection of new measurements to this station.
     * Measurements that are not related to this station
     * and measurements with a duplicate date shall be ignored and not added
     *
     * @param newMeasurements
     * @return the nett number of measurements which have been added.
     */
    public int addMeasurements(Collection<Measurement> newMeasurements) {
        int oldSize = this.getMeasurements().size();

        // TODO add all newMeasurements to the station
        //  ignore those who are not related to this station and entries with a duplicate date.

        Map<LocalDate, Measurement> newMeasurementMap = newMeasurements.stream()
                .filter(m -> m.getStation().equals(this) && !measurements.containsValue(m)) // check for assoc station and prevent duplication
                .collect(Collectors.toMap(Measurement::getDate, Function.identity())); // put measurement in map

        measurements.putAll(newMeasurementMap); // add the stream result to measurements

        return this.getMeasurements().size() - oldSize;
    }

    /**
     * calculates the all-time maximum temperature for this station
     *
     * @return the maximum temperature ever measured at this station
     * returns Double.NaN when no valid measurements are available
     */
    public double allTimeMaxTemperature() {
        // TODO calculate the maximum wind gust speed across all valid measurements

        double maxTemp = measurements.values().stream()
                .collect(Collectors.summarizingDouble(measurement -> measurement.getAverageTemperature())).getMax();

        if (!Double.isNaN(maxTemp)) {
            return measurements.values().stream()
                    .collect(Collectors.summarizingDouble(measurement -> measurement.getAverageTemperature())).getMax();
        } else {
            return Double.NaN;
        }
    }

    /**
     * @return the date of the first day of a measurement for this station
     * returns Optional.empty() if no measurements are available
     */
    public Optional<LocalDate> firstDayOfMeasurement() {
        // TODO get the date of the first measurement at this station

        // retrieve measurements, find minimal date
        return measurements.keySet().stream()
                .min(Comparator.comparing(LocalDate::toEpochDay));
    }

    /**
     * calculates the number of valid values of the data field that is specified by the mapper
     * invalid or empty values should be are represented by Double.NaN
     * this method can be used to check on different types of measurements each with their own mapper
     *
     * @param mapper the getter method of the data field to be checked.
     * @return the number of valid values found
     */
    public int numValidValues(Function<Measurement, Double> mapper) {
        // TODO count the number of valid values that can be accessed in the measurements collection
        //  by means of the mapper access function

        return (int) getMeasurements().stream().filter(o -> !Double.isNaN(mapper.apply(o))).count();
    }

    /**
     * calculates the total precipitation at this station
     * across the time period between startDate and endDate (inclusive)
     *
     * @param startDate the start date of the period of accumulation (inclusive)
     * @param endDate   the end date of the period of accumulation (inclusive)
     * @return the total precipitation value across the period
     * 0.0 if no measurements have been made in this period.
     */
    public double totalPrecipitationBetween(LocalDate startDate, LocalDate endDate) {
        // TODO calculate and return the total precipitation across the given period
        //  use the 'subMap' method to only process the measurements within the given period

//        .subMap(startDate, endDate)


//        measurements.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().getPrecipitation()));
//
//        Double map = measurements.subMap(startDate, true, endDate, true ).entrySet().stream()
//                .filter(d -> !Double.isNaN(d.getValue().getPrecipitation()))
//                .mapToDouble(x -> x.getValue().getPrecipitation())
//                .sum();

//        measurements.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().getPrecipitation())).forEach((item, item2) -> System.out.println(item + "-" + item2));
//        System.out.println();
//        measurements.subMap(startDate, true, endDate, true ).entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().getPrecipitation())).forEach((item, item2) -> System.out.println(item + "-" + item2));

//        System.out.println(map);


        return measurements.subMap(startDate, true, endDate, true ).entrySet().stream()
                .filter(d -> !Double.isNaN(d.getValue().getPrecipitation()))
                .mapToDouble(x -> x.getValue().getPrecipitation())
                .sum();
    }

    /**
     * calculates the average of all valid measurements of the quantity selected by the mapper function
     * across the time period between startDate and endDate (inclusive)
     *
     * @param startDate the start date of the period of averaging (inclusive)
     * @param endDate   the end date of the period of averaging (inclusive)
     * @param mapper    a getter method that obtains the double value from a measurement instance to be averaged
     * @return the average of all valid values of the selected quantity across the period
     * Double.NaN if no valid measurements are available from this period.
     */
    public double averageBetween(LocalDate startDate, LocalDate endDate, Function<Measurement, Double> mapper) {
        // TODO calculate and return the average value of the quantity mapper across the given period
        //  use the 'subMap' method to only process the measurements within the given period

        OptionalDouble result = measurements.subMap(startDate, true, endDate, true ).entrySet().stream()
                .filter(d -> !Double.isNaN(mapper.apply(d.getValue())))
                .mapToDouble(x -> mapper.apply(x.getValue()))
                .average();

        return result.orElse(Double.NaN);
    }

    // TODO any other methods required to make it work


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return stn == station.stn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stn);
    }

    @Override
    public String toString() {
        return stn + "/" + name;
    }
}
