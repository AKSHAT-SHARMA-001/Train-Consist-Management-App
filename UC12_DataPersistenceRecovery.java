/**
 * UC12 - Data Persistence & System Recovery
 * Book My Stay App
 * Handles saving booking data to file and recovering system state after restart.
 */
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UC12_DataPersistenceRecovery {

    private static final String DATA_FILE = "bookings_data.txt";
    private static final String BACKUP_FILE = "bookings_backup.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Booking data model (serializable as CSV line)
    public static class BookingData {
        private String bookingId;
        private String guestName;
        private String roomId;
        private String roomType;
        private String checkIn;
        private String checkOut;
        private double totalAmount;
        private String status;
        private String savedAt;

        public BookingData(String bookingId, String guestName, String roomId,
                           String roomType, String checkIn, String checkOut,
                           double totalAmount, String status) {
            this.bookingId = bookingId;
            this.guestName = guestName;
            this.roomId = roomId;
            this.roomType = roomType;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.totalAmount = totalAmount;
            this.status = status;
            this.savedAt = LocalDateTime.now().format(FORMATTER);
        }

        // Serialize to CSV
        public String toCsv() {
            return String.join(",", bookingId, guestName, roomId, roomType,
                    checkIn, checkOut, String.valueOf(totalAmount), status, savedAt);
        }

        // Deserialize from CSV
        public static BookingData fromCsv(String csvLine) {
            String[] parts = csvLine.split(",", -1);
            if (parts.length < 8) throw new IllegalArgumentException("Invalid data line: " + csvLine);
            BookingData data = new BookingData(
                    parts[0], parts[1], parts[2], parts[3],
                    parts[4], parts[5], Double.parseDouble(parts[6]), parts[7]);
            if (parts.length > 8) data.savedAt = parts[8];
            return data;
        }

        @Override
        public String toString() {
            return String.format("[%s] Guest: %-15s Room: %-5s Type: %-8s Amount: ₹%-10.2f Status: %s",
                    bookingId, guestName, roomId, roomType, totalAmount, status);
        }
    }

    // Persistence Manager
    public static class PersistenceManager {

        public boolean saveBooking(BookingData booking) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(DATA_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(booking.toCsv());
                writer.newLine();
                System.out.println("Saved booking: " + booking.bookingId);
                return true;
            } catch (IOException e) {
                System.out.println("Error saving booking: " + e.getMessage());
                return false;
            }
        }

        public List<BookingData> loadAllBookings() {
            List<BookingData> bookings = new ArrayList<>();
            Path filePath = Paths.get(DATA_FILE);
            if (!Files.exists(filePath)) {
                System.out.println("No data file found. Starting fresh.");
                return bookings;
            }
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        try {
                            bookings.add(BookingData.fromCsv(line));
                        } catch (Exception e) {
                            System.out.println("Skipping corrupt record: " + line);
                        }
                    }
                }
                System.out.println("Recovered " + bookings.size() + " bookings from persistence.");
            } catch (IOException e) {
                System.out.println("Error reading data file: " + e.getMessage());
            }
            return bookings;
        }

        public boolean createBackup() {
            try {
                Path source = Paths.get(DATA_FILE);
                if (!Files.exists(source)) {
                    System.out.println("No data to back up.");
                    return false;
                }
                Files.copy(source, Paths.get(BACKUP_FILE), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Backup created: " + BACKUP_FILE);
                return true;
            } catch (IOException e) {
                System.out.println("Backup failed: " + e.getMessage());
                return false;
            }
        }

        public boolean recoverFromBackup() {
            try {
                Path backup = Paths.get(BACKUP_FILE);
                if (!Files.exists(backup)) {
                    System.out.println("No backup file found.");
                    return false;
                }
                Files.copy(backup, Paths.get(DATA_FILE), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("System recovered from backup successfully.");
                return true;
            } catch (IOException e) {
                System.out.println("Recovery failed: " + e.getMessage());
                return false;
            }
        }

        public void clearData() {
            try {
                Files.deleteIfExists(Paths.get(DATA_FILE));
                System.out.println("Data file cleared.");
            } catch (IOException e) {
                System.out.println("Error clearing data: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        PersistenceManager pm = new PersistenceManager();
        pm.clearData(); // clean start for demo

        System.out.println("=== Saving Bookings ===");
        pm.saveBooking(new BookingData("BK001", "Arjun Kumar",  "R201", "Double", "2025-04-10", "2025-04-15", 12500.00, "CONFIRMED"));
        pm.saveBooking(new BookingData("BK002", "Priya Sharma", "R301", "Suite",  "2025-05-01", "2025-05-05", 20000.00, "CONFIRMED"));
        pm.saveBooking(new BookingData("BK003", "Ravi Verma",   "R101", "Single", "2025-04-20", "2025-04-22", 3000.00,  "CANCELLED"));

        System.out.println("\n=== Creating Backup ===");
        pm.createBackup();

        System.out.println("\n=== Simulating System Recovery ===");
        pm.clearData();
        pm.recoverFromBackup();

        System.out.println("\n=== Loading Recovered Bookings ===");
        List<BookingData> recovered = pm.loadAllBookings();
        recovered.forEach(System.out::println);
    }
}
