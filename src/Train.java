import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Bogie{
    String name;
    int capacity;

    Bogie(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}

public class Train {

    static class InvalidCapacityException extends Exception {
        InvalidCapacityException(String message) {
            super(message);
        }
    }

    static class PassengerBogie {
        String type;
        int capacity;

        PassengerBogie(String type, int capacity) throws InvalidCapacityException {
            if (capacity <= 0) {
                throw new InvalidCapacityException("Capacity must be greater than zero");
            }
            this.type = type;
            this.capacity = capacity;
        }
    }

    public static List<Bogie> filterBogiesByCapacity(List<Bogie> bogies, int threshold) {
        return bogies.stream()
                .filter(b -> b.capacity > threshold)
                .toList();
    }

    public static Map<String, List<Bogie>> groupBogiesByType(List<Bogie> bogies){
        return bogies.stream()
                .collect(Collectors.groupingBy(b->b.name));
    }

    public static int aggregateCapacity(List<Bogie> bogies){
        return bogies.stream()
                .map(b->b.capacity).reduce(0, Integer::sum);
    }

    private static final Pattern TRAIN_PATTERN = Pattern.compile("TRN-\\d{4}");
    private static final Pattern CARGO_PATTERN = Pattern.compile("PET-[A-Z]{2}");

    public static boolean isValidTrainId(String trainId) {
        return trainId != null && TRAIN_PATTERN.matcher(trainId).matches();
    }

    public static boolean isValidCargoCode(String cargoCode) {
        return cargoCode != null && CARGO_PATTERN.matcher(cargoCode).matches();
    }

    static class GoodsBogie {
        String shape;
        String cargo;

        GoodsBogie(String shape) {
            this.shape = shape;
        }

        void assignCargo(String cargo) {
            try {
                if (shape.equalsIgnoreCase("rectangular") &&
                        cargo.equalsIgnoreCase("petroleum")) {

                    throw new CargoSafetyException(
                            "Unsafe cargo assignment!"
                    );
                }

                this.cargo = cargo;
                System.out.println("Cargo assigned successfully: " + cargo);

            } catch (CargoSafetyException e) {
                System.out.println("Error: " + e.getMessage());

            } finally {
                System.out.println("Cargo validation completed for " + shape + " bogie\n");
            }
        }
    }

    static boolean allMatch(List<GoodsBogie> goodsBogies) {
        return goodsBogies.stream().allMatch(bogie ->
                bogie != null
                        && bogie.shape != null
                        && bogie.cargo != null
                        && (!"Cylindrical".equalsIgnoreCase(bogie.shape)
                        || "Petroleum".equalsIgnoreCase(bogie.cargo))
        );
    }

    //Custom Runtime
    static class CargoSafetyException extends RuntimeException{
        public CargoSafetyException(String message) {
            super(message);
        }
    }

    public static void bubbleSort(int[] capacities) {
        for (int i = 0; i < capacities.length - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < capacities.length - i - 1; j++) {
                if (capacities[j] > capacities[j + 1]) {
                    int temp = capacities[j];
                    capacities[j] = capacities[j + 1];
                    capacities[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped)
                break;
        }
    }

    static boolean searchBogie(Set<String> bogieIds, String searchId) {
        for (String id : bogieIds) {
            if (id.equals(searchId)) {
                return true;
            }
        }
        return false;
    }

    static boolean binarySearch(String[] arr, String searchKey){
        int start = 0, end = arr.length - 1;

        while(start <= end){
            int mid= start + (end - start) / 2;

            int comparison = searchKey.compareTo(arr[mid]);

            if(comparison == 0)
                return true;

            if(comparison > 0){
                start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        return false;
    }
    static boolean binarySearchWithExceptionHandler(String[] arr, String searchKey){
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException("No bogies available in the train. Cannot perform search");
        }

        int start = 0, end = arr.length - 1;

        while(start <= end){
            int mid= start + (end - start) / 2;

            int comparison = searchKey.compareTo(arr[mid]);

            if(comparison == 0)
                return true;

            if(comparison > 0){
                start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println("       === Train Consist Management App === ");
        System.out.println("========================================================\n");

        List<String> trainConsist = new LinkedList<>();

        System.out.println("Train initialized successfully...");
        System.out.println("Inital Bogie Count: " + trainConsist.size());
        System.out.println("Current Train Consist: " + trainConsist);

        System.out.println("\nSystem ready for operations...");

        System.out.println("===========================");
        System.out.println("Add Passenger Bogies to Train (UC2)");
        System.out.println("===========================");

        List<String> passengerBogies = new ArrayList<>();

        passengerBogies.add("Sleeper");
        passengerBogies.add("AC Chair");
        passengerBogies.add("First Class");

        System.out.println("\nAfter adding Bogies: ");
        System.out.println("Passenger Bogies: " + passengerBogies);

        passengerBogies.remove("AC Chair");
        System.out.println("\nAfter removing 'AC Chair': ");
        System.out.println("Passenger Bogies: " + passengerBogies);

        System.out.println("\nChecking if 'Sleeper' exists: ");
        System.out.println("Contains Sleeper? : " + passengerBogies.contains("Sleeper"));

        System.out.println("\nFinal Train Passenger Consist: ");
        System.out.println(passengerBogies);

        System.out.println("\nCRUD operations(UC2) completed successfully...");


        System.out.println("\n\n===========================");
        System.out.println("Track Unique Bogie IDs (UC3)");
        System.out.println("===========================\n");

        Set<String> bogieIds = new HashSet<>();
        bogieIds.add("BG101");
        bogieIds.add("BG102");
        bogieIds.add("BG103");
        bogieIds.add("BG104");
        //duplicating the entries
        bogieIds.add("BG101");
        bogieIds.add("BG102");


        System.out.println("Bogie IDs After Insertion: ");
        System.out.println(bogieIds);

        System.out.println("\nNote:\nDuplicates are automatically ignored by the HashSet.\n");

        System.out.println("Uniqueness validation completed... (UC3)");

        System.out.println("\n\n=======================================");
        System.out.println("  Maintain Ordered Bogie Consist (UC4)");
        System.out.println("========================================\n");

        trainConsist.add("Engine");
        trainConsist.add("Sleeper");
        trainConsist.add("AC");
        trainConsist.add("Cargo");
        trainConsist.add("Guard");

        System.out.println("Initial Train Consist: ");
        System.out.println(trainConsist);

        trainConsist.add(2, "Pantry Car");
        System.out.println("\nAfter Inserting 'Pantry Car' at position 2: ");
        System.out.println(trainConsist);

        //    trainConsist.removeFirst();
        //    trainConsist.removeLast();

        trainConsist.remove(0); trainConsist.remove(trainConsist.size() - 1);

        System.out.println("\nAfter removing First and Last Bogie: ");
        System.out.println(trainConsist);

        System.out.println("\nOrdered consist operations completed... (UC4)");

        System.out.println("\n\n========================================");
        System.out.println("  Preserve Insertion Order of Bogies (UC5)");
        System.out.println("=========================================\n");

        Set<String> formation = new LinkedHashSet<>();
        formation.add("Engine");
        formation.add("Sleeper");
        formation.add("Cargo");
        formation.add("Guard");
        //Duplicating entries for test
        formation.add("Guard");
        formation.add("Cargo");

        System.out.println("Final Train Formation:");
        System.out.println(formation);

        System.out.println("\nNote:\nLinkedHashSet preserves the insertion order and removes duplicates automatically.\n");
        System.out.println("Formation setup completed... (UC5)");

        /* -------------------UC6_Deprecated------------------
        System.out.println("\n\n========================================");
        System.out.println("  Map Bogie to Capacity (HashMap) (UC6)");
        System.out.println("========================================\n");

        Map<String, Integer> capacityMap = new HashMap<>();
        capacityMap.put("First Class", 24);
        capacityMap.put("Cargo", 120);
        capacityMap.put("Sleeper", 72);
        capacityMap.put("AC Chair", 56);

        System.out.println("Bogie Capacity Details:");
        for (Map.Entry<String, Integer> entry : capacityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("\nBogie-capacity mapping completed... (UC6)");*/

        System.out.println("\n\n========================================");
        System.out.println("  Sort Bogies by Capacity (Comparator) (UC7)");
        System.out.println("========================================\n");

        List<Bogie> bogies = new ArrayList<>();
        bogies.add(new Bogie("First Class", 24));
        bogies.add(new Bogie("Cargo", 120));
        bogies.add(new Bogie("Sleeper", 72));
        bogies.add(new Bogie("AC Chair", 56));
        bogies.add(new Bogie("AC Chair", 60));
        bogies.add(new Bogie("Sleeper", 70));

        System.out.println("Before sorting");
        for (Bogie b : bogies) {
            System.out.printf("%s  ->  %d\n", b.name, b.capacity);
        }

        bogies.sort(Comparator.comparingInt(b->b.capacity));
        System.out.println("\nAfter sorting");
        for (Bogie b : bogies) {
            System.out.printf("%s  ->  %d\n", b.name, b.capacity);
        }

        System.out.println("Sorting completed... (UC7)");

        System.out.println("\n\n========================================");
        System.out.println("  Filter Passenger Bogies Using Streams (UC8)");
        System.out.println("========================================\n");

        Stream<Bogie> stream = bogies.stream();
        List<Bogie> filteredList = filterBogiesByCapacity(bogies, 60);

        System.out.println("Filtered Bogies (Capacity > 60)");

        filteredList.forEach(b ->
                System.out.println(b.name + " - " + b.capacity));

        System.out.println("\nFiltering completed... (UC8)");



        System.out.println("\n\n========================================");
        System.out.println("  Group Bogies by Type (Collectors.groupingBy) (UC9)");
        System.out.println("========================================\n");

        Map<String, List<Bogie>> groupedBogies = groupBogiesByType(bogies);

        System.out.println("Grouped Bogies: \n");
        for(Map.Entry<String, List<Bogie>> entry : groupedBogies.entrySet()){
            System.out.println("Bogie Type: " + entry.getKey());
            for (Bogie b : entry.getValue()) {
                System.out.println("   Capacity -> " + b.capacity);
            }
        }

        System.out.println("\nGrouping completed... (UC9)");


        System.out.println("\n\n========================================");
        System.out.println("  Count Total Seats in Train (reduce) (UC10)");
        System.out.println("========================================\n");

        System.out.println("Total Seating Capacity of Train: " + aggregateCapacity(bogies));

        System.out.println("\nAggregation completed... (UC10)");



        System.out.println("\n\n========================================");
        System.out.println("  Validate Train ID & Cargo Codes (Regex) (UC11)");
        System.out.println("========================================\n");

        Scanner obj = new Scanner(System.in);

        System.out.print("Enter Train ID (Format: TRN-1234): ");
        String trainId = obj.nextLine();

        System.out.print("Enter Cargo Code (Format: PET-AB): ");
        String cargoCode = obj.nextLine();

        System.out.println("Train ID Valid: " + isValidTrainId(trainId));
        System.out.println("Cargo Code Valid: " + isValidCargoCode(cargoCode));

        obj.close();

        System.out.println("\nValidation completed... (UC11)");



        System.out.println("\n\n========================================");
        System.out.println("  Safety Compliance Check for Goods Bogies & Runtime Handling (UC12) & (UC15)");
        System.out.println("========================================\n");

        List<GoodsBogie> goodsBogies = new ArrayList<>();
        GoodsBogie bogie1 = new GoodsBogie("Cylindrical");
        GoodsBogie bogie2 = new GoodsBogie("Open");
        GoodsBogie bogie3 = new GoodsBogie("Box");
        GoodsBogie bogie4 = new GoodsBogie("Cylindrical");

        bogie1.assignCargo("Petroleum");
        bogie2.assignCargo("Coal");
        bogie3.assignCargo("Grain");
        bogie4.assignCargo("Coal");

        // Unsafe case
        GoodsBogie bogie5 = new GoodsBogie("Rectangular");
        bogie5.assignCargo("Petroleum");

        System.out.println("Goods Bogies in Train: ");
        for(GoodsBogie bogieX : goodsBogies){
            System.out.println(bogieX.cargo + " -> " + bogieX.shape);
        }

        boolean safetyCompliant = allMatch(goodsBogies);
        System.out.println("\nSafety Compliance Status: " + safetyCompliant);
        System.out.println(safetyCompliant
                ? "Train formation is SAFE."
                : "Train formation is NOT SAFE.");

        System.out.println("\nSafety Validation completed... (UC12)");


        System.out.println("\n\n==============================================");
        System.out.println("  Performance Comparison (Loops vs Streams) (UC13)");
        System.out.println("==============================================\n");

        List<Bogie> benchmarkBogies = new ArrayList<>();
        String[] bogieTypes = {"Open", "Box", "Cylindrical", "Flat", "Covered"};
        Random random = new Random(13);

        for (int i = 0; i < 100000; i++) {
            String type = bogieTypes[i % bogieTypes.length];
            int capacityForBenchmark = 20 + random.nextInt(181);
            benchmarkBogies.add(new Bogie(type, capacityForBenchmark));
        }

        int threshold = 100;

        long loopStart = System.nanoTime();
        List<Bogie> loopFiltered = new ArrayList<>();
        for (Bogie bogie : benchmarkBogies) {
            if (bogie.capacity > threshold) {
                loopFiltered.add(bogie);
            }
        }
        long loopEnd = System.nanoTime();

        long streamStart = System.nanoTime();
        List<Bogie> streamFiltered = benchmarkBogies.stream()
                .filter(bogie -> bogie.capacity > threshold)
                .toList();
        long streamEnd = System.nanoTime();

        if (loopFiltered.size() != streamFiltered.size()) {
            System.out.println("Benchmark warning: result size mismatch detected.");
        }

        System.out.println("Loop Execution Time (ns): " + (loopEnd - loopStart));
        System.out.println("Stream Execution Time (ns): " + (streamEnd - streamStart));

        System.out.println("\nPerformance benchmarking completed... (UC13)");


        System.out.println("\n\n========================================");
        System.out.println("  Handle Invalid Bogie Capacity (UC14)");
        System.out.println("========================================\n");

        try {
            PassengerBogie validBogie = new PassengerBogie("Sleeper", 72);
            System.out.println("Created Bogie: " + validBogie.type + " -> " + validBogie.capacity);

            PassengerBogie invalidBogie = new PassengerBogie("General", 0);
            System.out.println("Created Bogie: " + invalidBogie.type + " -> " + invalidBogie.capacity);
        } catch (InvalidCapacityException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nException handling completed... (UC14)");


        System.out.println("\nRuntime handling completed... (UC15)");


        System.out.println("\n\n========================================");
        System.out.println("  Sort Passenger Bogies by Capacity (Bubble Sort – Algorithm Intro) (UC16)");
        System.out.println("========================================\n");

        int[] capacities = {72, 56, 24, 70, 60};

        System.out.println("Original Capacities: ");
        for(int c : capacities){
            System.out.print(c + " ");
        }

        bubbleSort(capacities);

        System.out.println("\n\nSorted Capacities (Ascending): ");
        for(int c : capacities){
            System.out.print(c + " ");
        }


        System.out.println("\n\nSorting completed... (UC16)");

        System.out.println("\n\n========================================");
        System.out.println("  Sort Bogie Names Using Arrays.sort() (UC17)");
        System.out.println("========================================\n");

        String[] bogieNames = {"Sleeper", "AC Chair", "First Class", "General", "Luxury"};
        System.out.println("Original Bogie Names: ");
        System.out.println(Arrays.toString(bogieNames));

        Arrays.sort(bogieNames);

        System.out.println("\nSorted Bogie Names(Alphabetical): ");
        System.out.println(Arrays.toString(bogieNames));

        System.out.println("\nArrays.sort() Sorting completed... (UC17)");



        /* Linear Search Method UC18 - deprecated
        System.out.println("\n\n========================================");
        System.out.println("  Linear Search for Bogie ID (Array-Based Searching) (UC18)");
        System.out.println("========================================\n");

        bogieIds.add("BG101");
        bogieIds.add("BG205");
        bogieIds.add("BG309");
        bogieIds.add("BG412");
        bogieIds.add("BG550");

//        String[] bogieIds = {"BG101", "BG205", "BG309", "BG412", "BG550"};

        String searchId = "BG309";

        System.out.println("Available Bogie IDs: ");
        for(String id : bogieIds){
            System.out.println(id);
        }

        boolean found = false;

        found = searchBogie(bogieIds, searchId);

        if(found)
            System.out.println("\nBogie " + searchId + " found in train consist.");
        else
            System.out.println("\nBogie " + searchId + " NOT found in train consist.");

        System.out.println("\nSearch completed... (UC18)"); */

        System.out.println("\n\n========================================");
        System.out.println("  Binary Search for Bogie ID (Optimized Searching) & Exception Handling during search operations (UC19) & (UC20)");
        System.out.println("========================================\n");

        String[] bogieIds2 = {"BG101", "BG205", "BG309", "BG412", "BG550"};

        System.out.println("Sorted Bogie IDs");
        System.out.println(Arrays.toString(bogieIds2));

        String searchKey = "BG309";

        if(binarySearchWithExceptionHandler(bogieIds2, searchKey)){
            System.out.println("\nBogie " + searchKey + " found using Binary Search");
        }else{
            System.out.println("\nBogie  " + searchKey + "NOT found using Binary Search");
        }

        System.out.println("\nSearch completed... (UC19) & (UC20)");



    }
}