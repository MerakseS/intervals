import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Intervals {

    private static final int MIN_PARAMETERS_COUNT = 2;
    private static final int MAX_PARAMETERS_COUNT = 3;

    private static final int INTERVAL_NAME_INDEX = 0;
    private static final int INTERVAL_CONSTRUCTION_STARTING_NOTE_INDEX = 1;
    private static final int INTERVAL_IDENTIFICATION_STARTING_NOTE_INDEX = 0;
    private static final int LAST_NOTE_INDEX = 1;
    private static final int ORDER_INDEX = 2;

    private static final Map<String, Integer[]> intervals = new HashMap<>(11);
    private static final int INTERVAL_QUANTITY_INDEX = 0;
    private static final int INTERVAL_QUALITY_INDEX = 1;

    static {
        intervals.put("m2", new Integer[] {1, 2});
        intervals.put("M2", new Integer[] {2, 2});
        intervals.put("m3", new Integer[] {3, 3});
        intervals.put("M3", new Integer[] {4, 3});
        intervals.put("P4", new Integer[] {5, 4});
        intervals.put("P5", new Integer[] {7, 5});
        intervals.put("m6", new Integer[] {8, 6});
        intervals.put("M6", new Integer[] {9, 6});
        intervals.put("m7", new Integer[] {10, 7});
        intervals.put("M7", new Integer[] {11, 7});
        intervals.put("P8", new Integer[] {12, 8});
    }

    private static final List<Character> notes = List.of('C', 'D', 'E', 'F', 'G', 'A', 'B');

    private static final Map<String, Integer> accidentals = Map.of(
        "bb", -2,
        "b", -1,
        "", 0,
        "#", 1,
        "##", 2
    );

    // C--D--E-F--G--A--B-C
    private static final List<Integer> intervalsBetweenNeighbouringNotes = List.of(2, 2, 1, 2, 2, 2, 1);

    private static final String ORDER_DESCENDING_VALUE = "dsc";

    private static int order;

    public static String intervalConstruction(String[] args) {
        validateInput(args);

        String intervalName = args[INTERVAL_NAME_INDEX];
        int intervalQuantity = getIntervalQuantityByName(intervalName);
        int intervalQuality = getIntervalQualityByName(intervalName);

        String startingNote = args[INTERVAL_CONSTRUCTION_STARTING_NOTE_INDEX];
        char startingNoteName = parseNoteName(startingNote);
        String startingNoteAccidentalName = parseNoteAccidental(startingNote);

        order = getIntervalOrder(args);

        char lastNoteName = calculateLastNoteName(intervalQuality, startingNoteName);
        String lastNoteAccidentalName = calculateLastNoteAccidentalName(startingNoteName,
            startingNoteAccidentalName, lastNoteName, intervalQuantity);

        return lastNoteName + lastNoteAccidentalName;
    }

    private static char calculateLastNoteName(int intervalQuality, char startingNoteName) {
        int startingNoteIndex = notes.indexOf(startingNoteName);
        int lastNoteIndex = Math.floorMod(startingNoteIndex + order * (intervalQuality - 1), notes.size());
        return notes.get(lastNoteIndex);
    }

    private static String calculateLastNoteAccidentalName(char startingNote, String startingNoteAccidentalName,
        char lastNote, int intervalQuantity) {

        int semitonesSum = 0;
        semitonesSum += getStartingNoteAccidentalByName(startingNoteAccidentalName);
        semitonesSum += calculateIntervalBetweenNotes(startingNote, lastNote);

        int semitoneDifference = order * (intervalQuantity - semitonesSum);
        return getAccidentalByValue(semitoneDifference);
    }

    private static String getAccidentalByValue(int value) {
        return accidentals.entrySet().stream()
            .filter(entry -> entry.getValue() == value)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Unknown accidental value. Value: %d", value)));
    }

    public static String intervalIdentification(String[] args) {
        validateInput(args);

        String startingNote = args[INTERVAL_IDENTIFICATION_STARTING_NOTE_INDEX];
        char startingNoteName = parseNoteName(startingNote);
        String startingNoteAccidentalName = parseNoteAccidental(startingNote);

        String lastNote = args[LAST_NOTE_INDEX];
        char lastNoteName = parseNoteName(lastNote);
        String lastNoteAccidentalName = parseNoteAccidental(lastNote);

        order = getIntervalOrder(args);

        int intervalQuality = calculateIntervalQuality(startingNoteName, lastNoteName);
        int intervalQuantity = calculateIntervalQuantity(startingNoteName, startingNoteAccidentalName,
            lastNoteName, lastNoteAccidentalName);

        return getIntervalNameByIntervalDescription(intervalQuality, intervalQuantity);
    }

    private static int calculateIntervalQuality(char startingNote, char lastNote) {
        int intervalQuality = 1;
        int noteIndex = notes.indexOf(startingNote);
        while (!notes.get(noteIndex).equals(lastNote)) {
            intervalQuality++;
            noteIndex = incrementNoteIndex(noteIndex);
        }

        return intervalQuality;
    }

    private static int calculateIntervalQuantity(char startingNote, String startingNoteAccidentalName,
        char lastNote, String lastNoteAccidentalName) {

        int intervalQuantity = 0;
        intervalQuantity += getStartingNoteAccidentalByName(startingNoteAccidentalName);
        intervalQuantity += calculateIntervalBetweenNotes(startingNote, lastNote);
        intervalQuantity += getLastNoteAccidentalByName(lastNoteAccidentalName);

        return intervalQuantity;
    }

    private static String getIntervalNameByIntervalDescription(int intervalQuality, int intervalQuantity) {
        return intervals.entrySet().stream()
            .filter(entry -> {
                Integer[] intervalDefinition = entry.getValue();
                return intervalDefinition[INTERVAL_QUALITY_INDEX] == intervalQuality &&
                    intervalDefinition[INTERVAL_QUANTITY_INDEX] == intervalQuantity;
            })
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Cannot identify the interval. Quality: %d, Quantity: %d",
                    intervalQuality, intervalQuantity)
            ));
    }

    private static void validateInput(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }

        if (args.length < MIN_PARAMETERS_COUNT || args.length > MAX_PARAMETERS_COUNT) {
            throw new IllegalArgumentException("Illegal number of elements in input array");
        }
    }

    private static int calculateIntervalBetweenNotes(char startingNote, char lastNote) {
        int interval = 0;
        int noteIndex = notes.indexOf(startingNote);
        while (!notes.get(noteIndex).equals(lastNote)) {
            interval += getIntervalBetweenNeighbouringNotes(noteIndex);
            noteIndex = incrementNoteIndex(noteIndex);
        }

        return interval;
    }

    private static int getIntervalBetweenNeighbouringNotes(int noteIndex) {
        int semitoneCountsIndex = order > 0 ? noteIndex :
            Math.floorMod(noteIndex - 1, intervalsBetweenNeighbouringNotes.size());

        return intervalsBetweenNeighbouringNotes.get(semitoneCountsIndex);
    }

    private static int getStartingNoteAccidentalByName(String startingNoteAccidentalName) {
        return -1 * order * accidentals.get(startingNoteAccidentalName);
    }

    private static int getLastNoteAccidentalByName(String lastNoteAccidentalName) {
        return order * accidentals.get(lastNoteAccidentalName);
    }

    private static int incrementNoteIndex(int noteIndex) {
        return Math.floorMod(noteIndex + order, notes.size());
    }

    private static Integer getIntervalQuantityByName(String intervalName) {
        return intervals.get(intervalName)[INTERVAL_QUANTITY_INDEX];
    }

    private static Integer getIntervalQualityByName(String intervalName) {
        return intervals.get(intervalName)[INTERVAL_QUALITY_INDEX];
    }

    private static char parseNoteName(String startingNote) {
        return startingNote.charAt(0);
    }

    private static String parseNoteAccidental(String startingNote) {
        return startingNote.substring(1);
    }

    private static int getIntervalOrder(String[] args) {
        if (args.length == MAX_PARAMETERS_COUNT
            && args[ORDER_INDEX].equals(ORDER_DESCENDING_VALUE)) {
            return -1;
        }

        return 1;
    }
}
