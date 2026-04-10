import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainTest {
    //UC 8 - filter bogies by capacity tests
    @Test
    void testFilter_CapacityGreaterThanThreshold() {
        List<Bogie> bogies = List.of(
                new Bogie("Sleeper", 72),
                new Bogie("Cargo", 120),
                new Bogie("AC", 56),
                new Bogie("AC", 56)
        );

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertEquals(2, result.size());
    }

    @Test
    void testFilter_CapacityEqualToThreshold() {
        List<Bogie> bogies = List.of(
                new Bogie("Sleeper", 70)
        );

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFilter_CapacityLessThanThreshold() {
        List<Bogie> bogies = List.of(
                new Bogie("AC", 50)
        );

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertTrue(result.isEmpty());
    }


    @Test
    void testFilter_MultipleBogiesMatching() {
        List<Bogie> bogies = List.of(
                new Bogie("Sleeper", 80),
                new Bogie("Cargo", 120),
                new Bogie("AC", 40)
        );

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertEquals(2, result.size());
    }


    @Test
    void testFilter_NoBogiesMatching() {
        List<Bogie> bogies = List.of(
                new Bogie("AC", 40),
                new Bogie("Chair", 50)
        );

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFilter_AllBogiesMatching() {
        List<Bogie> bogies = List.of(
                new Bogie("Sleeper", 80),
                new Bogie("Cargo", 120)
        );

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertEquals(bogies.size(), result.size());
    }

    @Test
    void testFilter_EmptyBogieList() {
        List<Bogie> bogies = new ArrayList<>();

        List<Bogie> result = Train.filterBogiesByCapacity(bogies, 70);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFilter_OriginalListUnchanged() {
        List<Bogie> bogies = new ArrayList<>();
        bogies.add(new Bogie("Sleeper", 80));
        bogies.add(new Bogie("AC", 50));

        int originalSize = bogies.size();

        Train.filterBogiesByCapacity(bogies, 70);

        assertEquals(originalSize, bogies.size());
    }





    //UC 9 - group bogies by type tests
    private List<Bogie> getSampleBogies() {
        return Arrays.asList(
                new Bogie("Sleeper", 72),
                new Bogie("AC Chair", 56),
                new Bogie("First Class", 24),
                new Bogie("Sleeper", 80)
        );
    }

    private List<Bogie> filterWithLoop(List<Bogie> bogies, int threshold) {
        List<Bogie> filtered = new ArrayList<>();
        for (Bogie bogie : bogies) {
            if (bogie.capacity > threshold) {
                filtered.add(bogie);
            }
        }
        return filtered;
    }

    @Test
    void testGrouping_BogiesGroupedByType() {
        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(getSampleBogies());

        assertTrue(grouped.containsKey("Sleeper"));
        assertEquals(2, grouped.get("Sleeper").size());
    }

    @Test
    void testGrouping_MultipleBogiesInSameGroup() {
        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(getSampleBogies());

        assertEquals(2, grouped.get("Sleeper").size());
    }

    @Test
    void testGrouping_DifferentBogieTypes() {
        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(getSampleBogies());

        assertEquals(3, grouped.size());
    }

    @Test
    void testGrouping_EmptyBogieList() {
        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(new ArrayList<>());

        assertTrue(grouped.isEmpty());
    }

    @Test
    void testGrouping_SingleBogieCategory() {
        List<Bogie> bogies = Arrays.asList(
                new Bogie("Sleeper", 72),
                new Bogie("Sleeper", 80)
        );

        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(bogies);

        assertEquals(1, grouped.size());
        assertTrue(grouped.containsKey("Sleeper"));
    }

    @Test
    void testGrouping_MapContainsCorrectKeys() {
        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(getSampleBogies());

        assertTrue(grouped.containsKey("Sleeper"));
        assertTrue(grouped.containsKey("AC Chair"));
        assertTrue(grouped.containsKey("First Class"));
    }

    @Test
    void testGrouping_GroupSizeValidation() {
        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(getSampleBogies());

        assertEquals(2, grouped.get("Sleeper").size());
        assertEquals(1, grouped.get("AC Chair").size());
        assertEquals(1, grouped.get("First Class").size());
    }

    @Test
    void testGrouping_OriginalListUnchanged() {
        List<Bogie> bogies = getSampleBogies();
        int originalSize = bogies.size();

        Map<String, List<Bogie>> grouped =
                Train.groupBogiesByType(bogies);

        assertEquals(originalSize, bogies.size());
        assertEquals(4, bogies.size());
    }





    //UC10: total seating capacity
    @Test
    void testReduce_TotalSeatCalculation() {
        int total = Train.aggregateCapacity(getSampleBogies());

        assertEquals(232, total);
    }

    @Test
    void testReduce_MultipleBogiesAggregation() {
        int total = Train.aggregateCapacity(getSampleBogies());

        assertEquals(232, total);
    }

    @Test
    void testReduce_SingleBogieCapacity() {
        List<Bogie> bogies = Collections.singletonList(
                new Bogie("Sleeper", 72)
        );

        int total = Train.aggregateCapacity(bogies);

        assertEquals(72, total);
    }

    @Test
    void testReduce_EmptyBogieList() {
        List<Bogie> bogies = new ArrayList<>();

        int total = Train.aggregateCapacity(bogies);

        assertEquals(0, total);
    }

    @Test
    void testReduce_CorrectCapacityExtraction() {
        List<Bogie> bogies = getSampleBogies();

        int total = Train.aggregateCapacity(bogies);

        int expected = bogies.stream().mapToInt(b -> b.capacity).sum();

        assertEquals(expected, total);
    }

    @Test
    void testReduce_AllBogiesIncluded() {
        List<Bogie> bogies = getSampleBogies();

        int total = Train.aggregateCapacity(bogies);

        assertEquals(232, total);
    }

    @Test
    void testReduce_OriginalListUnchanged() {
        List<Bogie> bogies = getSampleBogies();
        int originalSize = bogies.size();

        Train.aggregateCapacity(bogies);

        assertEquals(originalSize, bogies.size());
        assertEquals(4, bogies.size());
    }





    //UC11: Validation Check
    @Test
    void testRegex_ValidTrainID() {
        assertTrue(Train.isValidTrainId("TRN-1234"));
    }

    @Test
    void testRegex_InvalidTrainIDFormat() {
        assertFalse(Train.isValidTrainId("TRAIN12"));
        assertFalse(Train.isValidTrainId("TRN12A"));
        assertFalse(Train.isValidTrainId("1234-TRN"));
    }

    @Test
    void testRegex_ValidCargoCode() {
        assertTrue(Train.isValidCargoCode("PET-AB"));
    }

    @Test
    void testRegex_InvalidCargoCodeFormat() {
        assertFalse(Train.isValidCargoCode("PET-ab"));
        assertFalse(Train.isValidCargoCode("PET123"));
        assertFalse(Train.isValidCargoCode("AB-PET"));
    }

    @Test
    void testRegex_TrainIDDigitLengthValidation() {
        assertFalse(Train.isValidTrainId("TRN-123"));
        assertFalse(Train.isValidTrainId("TRN-12345"));
    }

    @Test
    void testRegex_CargoCodeUppercaseValidation() {
        assertFalse(Train.isValidCargoCode("PET-ab"));
    }

    @Test
    void testRegex_EmptyInputHandling() {
        assertFalse(Train.isValidTrainId(""));
        assertFalse(Train.isValidCargoCode(""));
    }

    @Test
    void testRegex_ExactPatternMatch() {
        assertFalse(Train.isValidTrainId("TRN-1234XYZ"));
        assertFalse(Train.isValidCargoCode("PET-AB123"));
    }

    //UC12: safety compliance validation
    @Test
    void testSafety_AllBogiesValid() {
        List<Train.GoodsBogie> goodsBogies = List.of(
                createBogie("Cylindrical", "Petroleum"),
                createBogie("Open", "Coal"),
                createBogie("Box", "Grain")
        );

        assertTrue(Train.allMatch(goodsBogies));
    }

    @Test
    void testSafety_RectangularWithInvalidCargo() {
        List<Train.GoodsBogie> goodsBogies = List.of(
                createBogie("Rectangular", "Petroleum")
        );

        assertFalse(Train.allMatch(goodsBogies));
    }

    @Test
    void testSafety_NonRectangularBogiesAllowed() {
        List<Train.GoodsBogie> goodsBogies = List.of(
                createBogie("Open", "Coal"),
                createBogie("Box", "Grain")
        );

        assertTrue(Train.allMatch(goodsBogies));
    }

    @Test
    void testSafety_MixedBogiesWithViolation() {
        List<Train.GoodsBogie> goodsBogies = List.of(
                createBogie("Rectangular", "Petroleum"), // invalid
                createBogie("Open", "Coal"),
                createBogie("Box", "Grain")
        );

        assertFalse(Train.allMatch(goodsBogies));
    }
    private Train.GoodsBogie createBogie(String shape, String cargo) {
        Train.GoodsBogie bogie = new Train.GoodsBogie(shape);
        bogie.assignCargo(cargo);
        return bogie;
    }

    @Test
    void testSafety_EmptyBogieList() {
        assertTrue(Train.allMatch(Collections.emptyList()));
    }

    //UC13: Performance comparison tests
    @Test
    void testLoopFilteringLogic() {
        List<Bogie> bogies = List.of(
                new Bogie("A", 80),
                new Bogie("B", 60),
                new Bogie("C", 61),
                new Bogie("D", 40)
        );

        List<Bogie> loopResult = filterWithLoop(bogies, 60);

        assertEquals(2, loopResult.size());
        assertTrue(loopResult.stream().allMatch(b -> b.capacity > 60));
    }

    @Test
    void testStreamFilteringLogic() {
        List<Bogie> bogies = List.of(
                new Bogie("A", 80),
                new Bogie("B", 60),
                new Bogie("C", 61),
                new Bogie("D", 40)
        );

        List<Bogie> streamResult = Train.filterBogiesByCapacity(bogies, 60);

        assertEquals(2, streamResult.size());
        assertTrue(streamResult.stream().allMatch(b -> b.capacity > 60));
    }

    @Test
    void testLoopAndStreamResultsMatch() {
        List<Bogie> bogies = List.of(
                new Bogie("A", 80),
                new Bogie("B", 60),
                new Bogie("C", 61),
                new Bogie("D", 120),
                new Bogie("E", 59)
        );

        List<Bogie> loopResult = filterWithLoop(bogies, 60);
        List<Bogie> streamResult = Train.filterBogiesByCapacity(bogies, 60);

        assertEquals(loopResult.size(), streamResult.size());
    }

    @Test
    void testExecutionTimeMeasurement() {
        List<Bogie> bogies = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            bogies.add(new Bogie("T" + i, i % 200));
        }

        long start = System.nanoTime();
        List<Bogie> streamResult = Train.filterBogiesByCapacity(bogies, 60);
        long end = System.nanoTime();

        long elapsed = end - start;

        assertTrue(elapsed > 0);
        assertNotNull(streamResult);
    }

    @Test
    void testLargeDatasetProcessing() {
        List<Bogie> bogies = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            bogies.add(new Bogie("T" + i, i % 200));
        }

        List<Bogie> loopResult = filterWithLoop(bogies, 60);
        List<Bogie> streamResult = Train.filterBogiesByCapacity(bogies, 60);

        assertNotNull(loopResult);
        assertNotNull(streamResult);
        assertEquals(loopResult.size(), streamResult.size());
    }

    //UC14: Invalid capacity exception handling
    @Test
    void testException_ValidCapacityCreation() {
        assertDoesNotThrow(() -> new Train.PassengerBogie("Sleeper", 72));
    }

    @Test
    void testException_NegativeCapacityThrowsException() {
        assertThrows(Train.InvalidCapacityException.class,
                () -> new Train.PassengerBogie("Sleeper", -10));
    }

    @Test
    void testException_ZeroCapacityThrowsException() {
        assertThrows(Train.InvalidCapacityException.class,
                () -> new Train.PassengerBogie("Sleeper", 0));
    }

    @Test
    void testException_ExceptionMessageValidation() {
        Train.InvalidCapacityException exception = assertThrows(Train.InvalidCapacityException.class,
                () -> new Train.PassengerBogie("Sleeper", 0));

        assertEquals("Capacity must be greater than zero", exception.getMessage());
    }

    @Test
    void testException_ObjectIntegrityAfterCreation() throws Train.InvalidCapacityException {
        Train.PassengerBogie bogie = new Train.PassengerBogie("Sleeper", 72);

        assertEquals("Sleeper", bogie.type);
        assertEquals(72, bogie.capacity);
    }

    @Test
    void testException_MultipleValidBogiesCreation() {
        assertDoesNotThrow(() -> {
            Train.PassengerBogie b1 = new Train.PassengerBogie("Sleeper", 72);
            Train.PassengerBogie b2 = new Train.PassengerBogie("AC Chair", 56);
            Train.PassengerBogie b3 = new Train.PassengerBogie("First Class", 24);

            assertNotNull(b1);
            assertNotNull(b2);
            assertNotNull(b3);
        });
    }


    //UC15 - Runtime Handling
    @Test
    void testCargo_SafeAssignment() {
        Train.GoodsBogie bogie = new Train.GoodsBogie("Cylindrical");

        bogie.assignCargo("Petroleum");
        assertEquals("Petroleum", bogie.cargo);
    }

    @Test
    void testCargo_UnsafeAssignmentHandled() {
        Train.GoodsBogie bogie = new Train.GoodsBogie("Rectangular");

        assertDoesNotThrow(() -> bogie.assignCargo("Petroleum"));
        assertNull(bogie.cargo);
    }

    @Test
    void testCargo_CargoNotAssignedAfterFailure() {
        Train.GoodsBogie bogie = new Train.GoodsBogie("Rectangular");

        bogie.assignCargo("Petroleum");
        assertNull(bogie.cargo);
    }

    @Test
    void testCargo_ProgramContinuesAfterException() {
        Train.GoodsBogie bogie1 = new Train.GoodsBogie("Rectangular");
        Train.GoodsBogie bogie2 = new Train.GoodsBogie("Cylindrical");

        bogie1.assignCargo("Petroleum");
        bogie2.assignCargo("Petroleum");
        assertEquals("Petroleum", bogie2.cargo);
    }

    @Test
    void testCargo_FinallyBlockExecution() {
        Train.GoodsBogie bogie = new Train.GoodsBogie("Rectangular");

        assertDoesNotThrow(() -> bogie.assignCargo("Petroleum"));
        assertTrue(true);
    }


    //UC16 - Sort Passenger Bogies
    @Test
    void testSort_BasicSorting() {
        int[] arr = {72, 56, 24, 70, 60};
        Train.bubbleSort(arr);
        assertArrayEquals(new int[]{24, 56, 60, 70, 72}, arr);
    }

    @Test
    void testSort_AlreadySortedArray() {
        int[] arr = {24, 56, 60, 70, 72};
        Train.bubbleSort(arr);
        assertArrayEquals(new int[]{24, 56, 60, 70, 72}, arr);
    }

    @Test
    void testSort_DuplicateValues() {
        int[] arr = {72, 56, 56, 24};
        Train.bubbleSort(arr);
        assertArrayEquals(new int[]{24, 56, 56, 72}, arr);
    }

    @Test
    void testSort_SingleElementArray() {
        int[] arr = {50};
        Train.bubbleSort(arr);
        assertArrayEquals(new int[]{50}, arr);
    }

    @Test
    void testSort_AllEqualValues() {
        int[] arr = {40, 40, 40};
        Train.bubbleSort(arr);
        assertArrayEquals(new int[]{40, 40, 40}, arr);
    }


    //UC17 - Sort Bogie Names using Arrays.sort()
    @Test
    void testSort_BasicAlphabeticalSorting() {
        String[] input = {"Sleeper","AC Chair","First Class","General","Luxury"};
        String[] expected = {"AC Chair","First Class","General","Luxury","Sleeper"};

        Arrays.sort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSort_UnsortedInput() {
        String[] input = {"Luxury","General","Sleeper","AC Chair"};
        String[] expected = {"AC Chair","General","Luxury","Sleeper"};

        Arrays.sort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSort_AlreadySortedArray2() {
        String[] input = {"AC Chair","First Class","General"};
        String[] expected = {"AC Chair","First Class","General"};

        Arrays.sort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSort_DuplicateBogieNames() {
        String[] input = {"Sleeper","AC Chair","Sleeper","General"};
        String[] expected = {"AC Chair","General","Sleeper","Sleeper"};

        Arrays.sort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSort_SingleElementArray2() {
        String[] input = {"Sleeper"};
        String[] expected = {"Sleeper"};

        Arrays.sort(input);

        assertArrayEquals(expected, input);
    }



    //UC18 - searching bogieIds
    @Test
    void testSearch_BogieFound() {
        Set<String> bogies = Set.of("BG101","BG205","BG309","BG412","BG550");

        assertTrue(Train.searchBogie(bogies, "BG309"));
    }

    @Test
    void testSearch_BogieNotFound() {
        Set<String> bogies = Set.of("BG101","BG205","BG309","BG412","BG550");

        assertFalse(Train.searchBogie(bogies, "BG999"));
    }

    @Test
    void testSearch_FirstElementMatch() {
        Set<String> bogies = Set.of("BG101","BG205","BG309","BG412","BG550");

        assertTrue(Train.searchBogie(bogies, "BG101"));
    }

    @Test
    void testSearch_LastElementMatch() {
        Set<String> bogies = Set.of("BG101","BG205","BG309","BG412","BG550");

        assertTrue(Train.searchBogie(bogies, "BG550"));
    }

    @Test
    void testSearch_SingleElementArray() {
        Set<String> bogies = Set.of("BG101");

        assertTrue(Train.searchBogie(bogies, "BG101"));
    }



    //UC19 - Binary Search Method
    @Test
    void testBinarySearch_BogieFound() {
        String[] bogies = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(Train.binarySearch(bogies, "BG309"));
    }

    @Test
    void testBinarySearch_BogieNotFound() {
        String[] bogies = {"BG101","BG205","BG309","BG412","BG550"};
        assertFalse(Train.binarySearch(bogies, "BG999"));
    }

    @Test
    void testBinarySearch_FirstElementMatch() {
        String[] bogies = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(Train.binarySearch(bogies, "BG101"));
    }

    @Test
    void testBinarySearch_LastElementMatch() {
        String[] bogies = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(Train.binarySearch(bogies, "BG550"));
    }

    @Test
    void testBinarySearch_SingleElementArray() {
        String[] bogies = {"BG101"};
        assertTrue(Train.binarySearch(bogies, "BG101"));
    }

    @Test
    void testBinarySearch_EmptyArray() {
        String[] bogies = {};
        assertFalse(Train.binarySearch(bogies, "BG101"));
    }

    @Test
    void testBinarySearch_UnsortedInputHandled() {
        String[] bogies = {"BG309","BG101","BG550","BG205","BG412"};

        Arrays.sort(bogies);

        assertTrue(Train.binarySearch(bogies, "BG205"));
    }


    //UC20 - Exception handling during search operations
    @Test
    void testSearch_ThrowsExceptionWhenEmpty() {
        String[] bogies = {};
        String searchKey = "BG101";

        assertThrows(IllegalStateException.class, () -> {
            Train.binarySearchWithExceptionHandler(bogies, searchKey);
        });
    }

    @Test
    void testSearch_AllowsSearchWhenDataExists() {
        String[] bogies = {"BG101", "BG205"};
        String searchKey = "BG101";

        assertDoesNotThrow(() -> {
            Train.binarySearchWithExceptionHandler(bogies, searchKey);
        });
    }

    @Test
    void testSearch_BogieFoundAfterValidation() {
        String[] bogies = {"BG101", "BG205", "BG309"};
        String searchKey = "BG205";

        boolean result = Train.binarySearchWithExceptionHandler(bogies, searchKey);

        assertTrue(result);
    }

    @Test
    void testSearch_BogieNotFoundAfterValidation() {
        String[] bogies = {"BG101", "BG205", "BG309"};
        String searchKey = "BG999";

        boolean result = Train.binarySearchWithExceptionHandler(bogies, searchKey);

        assertFalse(result);
    }

    @Test
    void testSearch_SingleElementValidCase() {
        String[] bogies = {"BG101"};
        String searchKey = "BG101";

        boolean result = Train.binarySearchWithExceptionHandler(bogies, searchKey);

        assertTrue(result);
    }
}