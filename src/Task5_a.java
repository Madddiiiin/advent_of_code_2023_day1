import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Task5_a {

    public static void execute() {
        List<String> input = fileReader();
        long solution = findClosestLocation(findLocations(input));
        System.out.println(solution);
    }

    public static List<String> fileReader() {
        Path path = Paths.get("aoc_2023_day5_puzzle_input.txt");
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("An IO Exception occurred!");
            return new ArrayList<>();
        }
    }

    public static List<String> getSeeds (List<String> input) {
        String firstRow = input.get(0);
        String seeds = firstRow.substring(firstRow.indexOf(':') + 2);
        return (Arrays.stream(seeds.split(" ")).toList());
    }

    public static List<Long> stringToLong (List<String> list) {
        List<Long> output = new ArrayList<>();
        for (String s : list) {
            output.add(Long.valueOf(s));
        }
        return output;
    }
    /*
    mapType: 1 = seedToSoil, 2 = soilToFertilizer, 3 = fertilizerToWater, 4 = waterToLight, 5 = lightToTemperature, 6 = temperatureToHumidity, 7 = humidityToLocation
     */
    public static List<List<Long>> seperateMaps (List<String> input, int mapType) {
        List<List<String>> MapSegmentAsString = new ArrayList<>();
        int emptyLines = 0;
        for (int i = 0; i < input.size(); i++) {
            if (Objects.equals(input.get(i), "")) {
                emptyLines++;
                i += 2;
            }
            if (emptyLines == mapType) {
                MapSegmentAsString.add(Arrays.stream(input.get(i).split(" ")).toList());
            }
        }
        List<List<Long>> MapSegmentAsInt = new ArrayList<>();
        for (List<String> current : MapSegmentAsString) {
            MapSegmentAsInt.add(stringToLong(current));
        }
        return MapSegmentAsInt;
    }
    public static List<Long> mapper (List<List<Long>> map, List<Long> sources) {
        List<Long> correspondences = new ArrayList<>();
        for (int i = 0; i < sources.size(); i++) {
            long currentSource = sources.get(i);
            correspondences.add(currentSource);
            for (List<Long> row : map) {
                if (currentSource >= row.get(1) && currentSource < row.get(1) + row.get(2)) {
                    long correspondence = row.get(0) + (currentSource - row.get(1));
                    correspondences.set(i, correspondence);
                }
            }
        }
        return correspondences;
    }

    public static List<Long> findLocations (List<String> inputData) {
        List<Long> sources = stringToLong(getSeeds(inputData));
        List<Long> locations;
        List<List<Long>> map = seperateMaps(inputData, 1);
        locations = mapper(map, sources);
        for (int i = 0; i < 6; i++) {
            sources = locations;
            map = seperateMaps(inputData, i + 2);
            locations = mapper(map, sources);
        }
        return locations;
    }
    public static long findClosestLocation (List<Long> locations) {
        return locations.stream().min(Long::compare).get();
    }
}