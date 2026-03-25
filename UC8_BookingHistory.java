/**
 * UC8 - Booking History & Reporting
 * Book My Stay App
 * Retrieves and displays booking history and generates summary reports.
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UC8_BookingHistory {

    // Booking record model
    public static class BookingRecord {
        private String bookingId;
        private String guestName;
        private String roomId;
        private String roomType;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private double totalAmount;
        private String status;

        public BookingRecord(String bookingId, String guestName, String roomId,
                             String roomType, LocalDate checkIn, LocalDate checkOut,
                             double totalAmount, String status) {
            this.bookingId = bookingId;
            this.guestName = guestName;
            this.roomId = roomId;
            this.roomType = roomType;
            this.checkInDate = checkIn;
            this.checkOutDate = checkOut;
            this.totalAmount = totalAmount;
            this.status = status;
        }

        public String getBookingId() { return bookingId; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public LocalDate getCheckInDate() { return checkInDate; }
        public double getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }

        @Override
        public String toString() {
            return String.format("%-14s %-18s %-6s %-10s %-12s %-12s ₹%-10.2f %s",
                    bookingId, guestName, roomId, roomType, checkInDate, checkOutDate, totalAmount, status);
        }
    }

    // Booking History Service
    public static class BookingHistoryService {
        private final List<BookingRecord> bookingHistory = new ArrayList<>();

        public BookingHistoryService() {
            // Sample data
            bookingHistory.add(new BookingRecord("BK001", "Arjun Kumar",   "R201", "Double",  LocalDate.of(2025, 1, 5),  LocalDate.of(2025, 1, 8),  7500.00,  "CONFIRMED"));
            bookingHistory.add(new BookingRecord("BK002", "Priya Sharma",  "R301", "Suite",   LocalDate.of(2025, 1, 12), LocalDate.of(2025, 1, 15), 15000.00, "CONFIRMED"));
            bookingHistory.add(new BookingRecord("BK003", "Ravi Verma",    "R101", "Single",  LocalDate.of(2025, 2, 3),  LocalDate.of(2025, 2, 5),  3000.00,  "CANCELLED"));
            bookingHistory.add(new BookingRecord("BK004", "Neha Singh",    "R401", "Deluxe",  LocalDate.of(2025, 2, 20), LocalDate.of(2025, 2, 25), 17500.00, "CONFIRMED"));
            bookingHistory.add(new BookingRecord("BK005", "Arjun Kumar",   "R501", "Family",  LocalDate.of(2025, 3, 1),  LocalDate.of(2025, 3, 7),  24000.00, "CONFIRMED"));
            bookingHistory.add(new BookingRecord("BK006", "Kiran Patel",   "R202", "Double",  LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 12), 5000.00,  "CANCELLED"));
        }

        public List<BookingRecord> getAllBookings() {
            return new ArrayList<>(bookingHistory);
        }

        public List<BookingRecord> getBookingsByGuest(String guestName) {
            return bookingHistory.stream()
                    .filter(b -> b.getGuestName().equalsIgnoreCase(guestName))
                    .collect(Collectors.toList());
        }

        public List<BookingRecord> getBookingsByStatus(String status) {
            return bookingHistory.stream()
                    .filter(b -> b.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        public void printHistory(List<BookingRecord> records, String title) {
            System.out.println("\n===== " + title + " =====");
            System.out.printf("%-14s %-18s %-6s %-10s %-12s %-12s %-12s %s%n",
                    "Booking ID", "Guest", "Room", "Type", "Check-In", "Check-Out", "Amount", "Status");
            System.out.println("-".repeat(100));
            records.forEach(System.out::println);
        }

        public void generateSummaryReport() {
            System.out.println("\n========== Booking Summary Report ==========");
            long confirmed  = bookingHistory.stream().filter(b -> b.getStatus().equals("CONFIRMED")).count();
            long cancelled  = bookingHistory.stream().filter(b -> b.getStatus().equals("CANCELLED")).count();
            double revenue  = bookingHistory.stream()
                    .filter(b -> b.getStatus().equals("CONFIRMED"))
                    .mapToDouble(BookingRecord::getTotalAmount).sum();

            System.out.println("Total Bookings   : " + bookingHistory.size());
            System.out.println("Confirmed        : " + confirmed);
            System.out.println("Cancelled        : " + cancelled);
            System.out.printf("Total Revenue    : ₹%.2f%n", revenue);
            System.out.println("Most Booked Type : " + getMostBookedRoomType());
            System.out.println("=============================================");
        }

        private String getMostBookedRoomType() {
            return bookingHistory.stream()
                    .filter(b -> b.getStatus().equals("CONFIRMED"))
                    .collect(Collectors.groupingBy(BookingRecord::getRoomType, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
        }
    }

    public static void main(String[] args) {
        BookingHistoryService service = new BookingHistoryService();

        service.printHistory(service.getAllBookings(), "All Booking History");
        service.printHistory(service.getBookingsByGuest("Arjun Kumar"), "Bookings for Arjun Kumar");
        service.printHistory(service.getBookingsByStatus("CANCELLED"), "Cancelled Bookings");
        service.generateSummaryReport();
    }
}
