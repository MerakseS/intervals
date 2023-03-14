import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class IntervalIdentificationTest {

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
    void unknownInterval() {
        String[] args = new String[] {"A", "A"};
        Assertions.assertThrows(IllegalArgumentException.class, () -> Intervals.intervalIdentification(args));
    }

    @Test
    void checkIntervalsIdentificationAlgorithm() {
        String[] args = {"C", "D"};
        String actual = Intervals.intervalIdentification(args);
        Assertions.assertEquals("M2", actual);
    }

    @ParameterizedTest
    @MethodSource("provideWithExampleData")
    void checkIntervalsIdentificationAlgorithmWithExampleData(String[] args, String expected) {
        String actual = Intervals.intervalIdentification(args);
        Assertions.assertEquals(expected, actual);
    }

    public static Stream<Arguments> provideWithExampleData() {
        return Stream.of(
            Arguments.arguments(new String[] {"C", "D"}, "M2"),
            Arguments.arguments(new String[] {"B", "F#", "asc"}, "P5"),
            Arguments.arguments(new String[] {"Fb", "Gbb"}, "m2"),
            Arguments.arguments(new String[] {"G", "F#", "asc"}, "M7"),
            Arguments.arguments(new String[] {"Bb", "A", "dsc"}, "m2"),
            Arguments.arguments(new String[] {"Cb", "Abb", "dsc"}, "M3"),
            Arguments.arguments(new String[] {"G#", "D#", "dsc"}, "P4"),
            Arguments.arguments(new String[] {"E", "B", "dsc"}, "P4"),
            Arguments.arguments(new String[] {"E#", "D#", "dsc"}, "M2"),
            Arguments.arguments(new String[] {"B", "G#", "dsc"}, "m3")
        );
    }
}
