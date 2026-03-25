/**
 * UC4 - Room Search & Availability Check
 * Book My Stay App
 * Allows users to search for rooms based on criteria and check real-time availability.
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UC4_RoomSearchAvailability {

    // Search criteria model
    public static class SearchCriteria {
        private String roomType;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private double maxPricePerNight;
        private int numberOfGuests;

        public SearchCriteria(String roomType, LocalDate checkIn, LocalDate checkOut,
                              double maxPrice, int guests) {
            this.roomType = roomType;
            this.checkInDate = checkIn;
            this.checkOutDate = checkOut;
            this.maxPricePerNight = maxPrice;
            this.numberOfGuests = guests;
        }

        public String getRoomType() { return roomType; }
        public LocalDate getCheckInDate() { return checkInDate; }
        public LocalDate getCheckOutDate() { return checkOutDate; }
        public double getMaxPricePerNight() { return maxPricePerNight; }
        public int getNumberOfGuests() { return numberOfGuests; }
    }

    // Room availability model
    public static class RoomAvailability {
        private String roomId;
        private String roomType;
        private double pricePerNight;
        private int maxOccupancy;
        private boolean available;

        public RoomAvailability(String roomId, String roomType, double price, int maxOccupancy, boolean available) {
            this.roomId = roomId;
            this.roomType = roomType;
            this.pricePerNight = price;
            this.maxOccupancy = maxOccupancy;
            this.available = available;
        }

        public String getRoomId() { return roomId; }
        public String getRoomType() { return roomType; }
        public double getPricePerNight() { return pricePerNight; }
        public int getMaxOccupancy() { return maxOccupancy; }
        public boolean isAvailable() { return available; }

        @Override
        public String toString() {
            return String.format("Room %-6s | %-10s | ₹%-8.2f | Occupancy: %-3d | %s",
                    roomId, roomType, pricePerNight, maxOccupancy,
                    available ? "✔ Available" : "✘ Not Available");
        }
    }

    // Room Search Service
    public static class RoomSearchService {

        // Simulated room database
        private final List<RoomAvailability> roomDatabase = new ArrayList<>();

        public RoomSearchService() {
            roomDatabase.add(new RoomAvailability("R101", "Single",  1500.00, 1,  true));
            roomDatabase.add(new RoomAvailability("R102", "Single",  1500.00, 1,  false));
            roomDatabase.add(new RoomAvailability("R201", "Double",  2500.00, 2,  true));
            roomDatabase.add(new RoomAvailability("R202", "Double",  2700.00, 2,  true));
            roomDatabase.add(new RoomAvailability("R301", "Suite",   5000.00, 4,  true));
            roomDatabase.add(new RoomAvailability("R401", "Deluxe",  3500.00, 3,  false));
            roomDatabase.add(new RoomAvailability("R501", "Family",  4000.00, 5,  true));
        }

        public List<RoomAvailability> searchRooms(SearchCriteria criteria) {
            List<RoomAvailability> results = new ArrayList<>();

            for (RoomAvailability room : roomDatabase) {
                boolean matchesType = criteria.getRoomType() == null
                        || criteria.getRoomType().isEmpty()
                        || room.getRoomType().equalsIgnoreCase(criteria.getRoomType());
                boolean matchesPrice = room.getPricePerNight() <= criteria.getMaxPricePerNight();
                boolean matchesOccupancy = room.getMaxOccupancy() >= criteria.getNumberOfGuests();

                if (matchesType && matchesPrice && matchesOccupancy && room.isAvailable()) {
                    results.add(room);
                }
            }
            return results;
        }

        public void displayResults(List<RoomAvailability> results) {
            if (results.isEmpty()) {
                System.out.println("No rooms found matching your criteria.");
                return;
            }
            System.out.println("\n===== Search Results =====");
            results.forEach(System.out::println);
            System.out.println("Total matches: " + results.size());
        }
    }

    public static void main(String[] args) {
        RoomSearchService service = new RoomSearchService();

        // Search example
        SearchCriteria criteria = new SearchCriteria(
                "Double",
                LocalDate.of(2025, 4, 10),
                LocalDate.of(2025, 4, 15),
                3000.00,
                2
        );

        System.out.println("Searching for rooms...");
        System.out.println("Type: " + criteria.getRoomType());
        System.out.println("Check-in: " + criteria.getCheckInDate());
        System.out.println("Check-out: " + criteria.getCheckOutDate());
        System.out.println("Max Price: ₹" + criteria.getMaxPricePerNight());
        System.out.println("Guests: " + criteria.getNumberOfGuests());

        List<RoomAvailability> results = service.searchRooms(criteria);
        service.displayResults(results);
    }
}
