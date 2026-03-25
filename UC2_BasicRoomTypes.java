/**
 * UC2 - Basic Room Types & Static Availability
 * Book My Stay App
 * Defines room types and displays their static availability status.
 */
import java.util.HashMap;
import java.util.Map;

public class UC2_BasicRoomTypes {

    // Enum representing different room types
    public enum RoomType {
        SINGLE("Single Room", 1500.00),
        DOUBLE("Double Room", 2500.00),
        SUITE("Suite", 5000.00),
        DELUXE("Deluxe Room", 3500.00),
        FAMILY("Family Room", 4000.00);

        private final String displayName;
        private final double pricePerNight;

        RoomType(String displayName, double pricePerNight) {
            this.displayName = displayName;
            this.pricePerNight = pricePerNight;
        }

        public String getDisplayName() { return displayName; }
        public double getPricePerNight() { return pricePerNight; }
    }

    // Static availability map
    private static final Map<RoomType, Integer> staticAvailability = new HashMap<>();

    static {
        staticAvailability.put(RoomType.SINGLE, 10);
        staticAvailability.put(RoomType.DOUBLE, 8);
        staticAvailability.put(RoomType.SUITE, 3);
        staticAvailability.put(RoomType.DELUXE, 5);
        staticAvailability.put(RoomType.FAMILY, 4);
    }

    public static void displayAllRoomTypes() {
        System.out.println("========== Available Room Types ==========");
        System.out.printf("%-15s %-20s %-15s %-10s%n", "Type", "Name", "Price/Night", "Available");
        System.out.println("----------------------------------------------------------");
        for (RoomType type : RoomType.values()) {
            int available = staticAvailability.getOrDefault(type, 0);
            System.out.printf("%-15s %-20s ₹%-14.2f %-10d%n",
                    type.name(), type.getDisplayName(), type.getPricePerNight(), available);
        }
        System.out.println("==========================================================");
    }

    public static int getAvailability(RoomType type) {
        return staticAvailability.getOrDefault(type, 0);
    }

    public static boolean isAvailable(RoomType type) {
        return staticAvailability.getOrDefault(type, 0) > 0;
    }

    public static void main(String[] args) {
        displayAllRoomTypes();

        System.out.println("\nChecking specific room availability:");
        System.out.println("Suite available: " + isAvailable(RoomType.SUITE));
        System.out.println("Single rooms left: " + getAvailability(RoomType.SINGLE));
    }
}
