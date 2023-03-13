package com.innowisegroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Intervals {

    private static final int MIN_PARAMETERS_COUNT = 2;
    private static final int MAX_PARAMETERS_COUNT = 3;

    private static final int INTERVAL_NAME_INDEX = 0;
    private static final int STARTING_NOTE_INDEX = 1;
    private static final int ORDER_INDEX = 2;

    private static final String ORDER_DESCENDING_VALUE = "dsc";

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

    private static final List<Integer> intervalsBetweenNotes = List.of(2, 2, 1, 2, 2, 2, 1);

    private static int order;

    public static String intervalConstruction(String[] args) {
        validateInput(args);

        String intervalName = args[INTERVAL_NAME_INDEX];
        int intervalQuantity = getIntervalQuantity(intervalName);
        int intervalQuality = getIntervalQuality(intervalName);

        String startingNote = args[STARTING_NOTE_INDEX];
        char startingNoteName = parseNoteName(startingNote);
        String startingNoteAccidental = parseNoteAccidental(startingNote);

        order = getIntervalOrder(args);

        char lastNoteName = calculateLastNoteName(intervalQuality, startingNoteName);
        String lastNoteAccidental = calculateLastNoteAccidental(startingNoteAccidental,
            startingNoteName, lastNoteName, intervalQuantity);

        return lastNoteName + lastNoteAccidental;
    }

    public static String intervalIdentification(String[] args) {
        return null;
    }

    private static char calculateLastNoteName(int intervalQuality, char startingNoteName) {
        int startingNoteNameIndex = notes.indexOf(startingNoteName);
        int lastNoteNameIndex = Math.floorMod(startingNoteNameIndex + order * (intervalQuality - 1), notes.size());
        return notes.get(lastNoteNameIndex);
    }

    private static String calculateLastNoteAccidental(String startingNoteAccidental,
        char startingNoteName, char lastNoteName, int intervalQuantity) {

        int semitonesSum = -1 * order * accidentals.get(startingNoteAccidental);
        int noteIndex = notes.indexOf(startingNoteName);
        while (!notes.get(noteIndex).equals(lastNoteName)) {
            semitonesSum += getNoteSemitoneCount(noteIndex);
            noteIndex = Math.floorMod(noteIndex + order, notes.size());
        }

        int semitoneDifference = order * (intervalQuantity - semitonesSum);
        return getAccidentalByValue(semitoneDifference);
    }

    private static int getNoteSemitoneCount(int noteIndex) {
        int semitoneCountsIndex = order > 0 ? noteIndex :
            Math.floorMod(noteIndex - 1, intervalsBetweenNotes.size());

        return intervalsBetweenNotes.get(semitoneCountsIndex);
    }

    private static String getAccidentalByValue(int value) {
        return accidentals.entrySet().stream()
            .filter(entry -> entry.getValue() == value)
            .map(Map.Entry::getKey)
            .findFirst().orElseThrow();
    }

    private static void validateInput(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }

        if (args.length < MIN_PARAMETERS_COUNT || args.length > MAX_PARAMETERS_COUNT) {
            throw new IllegalArgumentException("Illegal number of elements in input array");
        }


    }

    private static Integer getIntervalQuantity(String intervalName) {
        return intervals.get(intervalName)[INTERVAL_QUANTITY_INDEX];
    }

    private static Integer getIntervalQuality(String intervalName) {
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
