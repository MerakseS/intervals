package com.innowisegroup;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IntervalsConstructionTest {

    @Test
    void nullInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Intervals.intervalConstruction(null));
    }

    @Test
    void fewerElementsInInputArray() {
        String[] args = new String[0];
        Assertions.assertThrows(IllegalArgumentException.class, () -> Intervals.intervalConstruction(args));
    }

    @Test
    void moreElementsInInputArray() {
        String[] args = new String[4];
        Assertions.assertThrows(IllegalArgumentException.class, () -> Intervals.intervalConstruction(args));
    }

    @Test
    void exampleTest() {
        String[] args = new String[] {"P5", "A#", "asc"};
        String actual = Intervals.intervalConstruction(args);
        Assertions.assertEquals("E#", actual);
    }

    @ParameterizedTest
    @MethodSource("provideWithData")
    void intervalsConstructionAlgorithmTest(String[] args, String expected) {
        String actual = Intervals.intervalConstruction(args);
        Assertions.assertEquals(expected, actual);
    }

    public static Stream<Arguments> provideWithData() {
        return Stream.of(
            Arguments.arguments(new String[] {"M2", "C", "asc"}, "D"),
            Arguments.arguments(new String[] {"P5", "B", "asc"}, "F#"),
            Arguments.arguments(new String[] {"m2", "Bb", "dsc"}, "A"),
            Arguments.arguments(new String[] {"M3", "Cb", "dsc"}, "Abb"),
            Arguments.arguments(new String[] {"P4", "G#", "dsc"}, "D#"),
            Arguments.arguments(new String[] {"m3", "B", "dsc"}, "G#"),
            Arguments.arguments(new String[] {"m2", "Fb", "asc"}, "Gbb"),
            Arguments.arguments(new String[] {"M2", "E#", "dsc"}, "D#"),
            Arguments.arguments(new String[] {"P4", "E", "dsc"}, "B"),
            Arguments.arguments(new String[] {"m2", "D#", "asc"}, "E"),
            Arguments.arguments(new String[] {"M7", "G", "asc"}, "F#")
        );
    }
}