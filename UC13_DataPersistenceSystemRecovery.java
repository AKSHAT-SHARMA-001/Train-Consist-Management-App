/**
 * UC13 - Data Persistence & System Recovery
 * Book My Stay App
 * Extended persistence layer with transaction logging and crash recovery support.
 */
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UC13_DataPersistenceSystemRecovery {

    private static final String TRANSACTION_LOG = "transaction_log.txt";
    private static final String SNAPSHOT_FILE  = "system_snapshot.txt";
    private static final DateTimeFormatter FMT  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Transaction types
    public enum TransactionType {
        BOOKING_CREATED, BOOKING_CONFIRMED, BOOKING_CANCELLED,
        ROOM_ALLOCATED, ROOM_RELEASED, PAYMENT_PROCESSED
    }

    // Transaction log entry
    public static class TransactionEntry {
        private final String transactionId;
        private final TransactionType type;
        private final String entityId;
        private final String details;
        private final String timestamp;

        public TransactionEntry(TransactionType type, String entityId, String details) {
            this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.type = type;
            this.entityId = entityId;
            this.details = details;
            this.timestamp = LocalDateTime.now().format(FMT);
        }

        public TransactionEntry(String transactionId, String type, String entityId,
                                String details, String timestamp) {
            this.transactionId = transactionId;
            this.type = TransactionType.valueOf(type);
            this.entityId = entityId;
            this.details = details;
            this.timestamp = timestamp;
        }

        public String toCsv() {
            return String.join("|", transactionId, type.name(), entityId, details, timestamp);
        }

        public static TransactionEntry fromCsv(String line) {
            String[] p = line.split("\\|", -1);
            return new TransactionEntry(p[0], p[1], p[2], p[3], p[4]);
        }

        @Override
        public String toString() {
            return String.format("[%s] %-25s Entity: %-12s | %s | %s",
                    transactionId, type, entityId, details, timestamp);
        }
    }

    // System Snapshot (in-memory state)
    public static class SystemSnapshot {
        private final Map<String, String> roomStatus = new LinkedHashMap<>();
        private final Map<String, String> bookingStatus = new LinkedHashMap<>();
        private String snapshotTime;

        public void setRoomStatus(String roomId, String status) { roomStatus.put(roomId, status); }
        public void setBookingStatus(String bookingId, String status) { bookingStatus.put(bookingId, status); }

        public String serialize() {
            StringBuilder sb = new StringBuilder();
            sb.append("SNAPSHOT_TIME=").append(LocalDateTime.now().format(FMT)).append("\n");
            roomStatus.forEach((k, v) -> sb.append("ROOM|").append(k).append("=").append(v).append("\n"));
            bookingStatus.forEach((k, v) -> sb.append("BOOKING|").append(k).append("=").append(v).append("\n"));
            return sb.toString();
        }

        public static SystemSnapshot deserialize(String data) {
            SystemSnapshot snap = new SystemSnapshot();
            for (String line : data.split("\n")) {
                if (line.startsWith("SNAPSHOT_TIME=")) {
                    snap.snapshotTime = line.substring("SNAPSHOT_TIME=".length());
                } else if (line.startsWith("ROOM|")) {
                    String[] parts = line.substring(5).split("=", 2);
                    snap.roomStatus.put(parts[0], parts[1]);
                } else if (line.startsWith("BOOKING|")) {
                    String[] parts = line.substring(8).split("=", 2);
                    snap.bookingStatus.put(parts[0], parts[1]);
                }
            }
            return snap;
        }

        public void print() {
            System.out.println("\n===== System Snapshot [" + snapshotTime + "] =====");
            System.out.println("-- Room Status --");
            roomStatus.forEach((k, v) -> System.out.println("  " + k + " : " + v));
            System.out.println("-- Booking Status --");
            bookingStatus.forEach((k, v) -> System.out.println("  " + k + " : " + v));
            System.out.println("=================================================");
        }
    }

    // Recovery Manager
    public static class RecoveryManager {

        public void logTransaction(TransactionEntry entry) {
            try (BufferedWriter w = Files.newBufferedWriter(Paths.get(TRANSACTION_LOG),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                w.write(entry.toCsv());
                w.newLine();
                System.out.println("Logged: " + entry);
            } catch (IOException e) {
                System.out.println("Log error: " + e.getMessage());
            }
        }

        public List<TransactionEntry> replayTransactionLog() {
            List<TransactionEntry> entries = new ArrayList<>();
            Path path = Paths.get(TRANSACTION_LOG);
            if (!Files.exists(path)) { System.out.println("No transaction log found."); return entries; }
            try (BufferedReader r = Files.newBufferedReader(path)) {
                String line;
                while ((line = r.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        try { entries.add(TransactionEntry.fromCsv(line)); }
                        catch (Exception e) { System.out.println("Corrupt log entry skipped: " + line); }
                    }
                }
            } catch (IOException e) { System.out.println("Error reading log: " + e.getMessage()); }
            System.out.println("Replayed " + entries.size() + " transactions from log.");
            return entries;
        }

        public void saveSnapshot(SystemSnapshot snapshot) {
            try {
                Files.writeString(Paths.get(SNAPSHOT_FILE), snapshot.serialize());
                System.out.println("System snapshot saved.");
            } catch (IOException e) { System.out.println("Snapshot save error: " + e.getMessage()); }
        }

        public SystemSnapshot loadSnapshot() {
            try {
                if (!Files.exists(Paths.get(SNAPSHOT_FILE))) {
                    System.out.println("No snapshot found.");
                    return null;
                }
                String data = Files.readString(Paths.get(SNAPSHOT_FILE));
                System.out.println("Snapshot loaded successfully.");
                return SystemSnapshot.deserialize(data);
            } catch (IOException e) { System.out.println("Snapshot load error: " + e.getMessage()); return null; }
        }

        public void clearLogs() {
            try {
                Files.deleteIfExists(Paths.get(TRANSACTION_LOG));
                Files.deleteIfExists(Paths.get(SNAPSHOT_FILE));
                System.out.println("Logs and snapshots cleared.");
            } catch (IOException e) { System.out.println("Clear error: " + e.getMessage()); }
        }
    }

    public static void main(String[] args) {
        RecoveryManager recovery = new RecoveryManager();
        recovery.clearLogs();

        System.out.println("=== Logging Transactions ===");
        recovery.logTransaction(new TransactionEntry(TransactionType.BOOKING_CREATED,   "BK001", "Guest: Arjun, Room: R201"));
        recovery.logTransaction(new TransactionEntry(TransactionType.ROOM_ALLOCATED,    "R201",  "Allocated to BK001"));
        recovery.logTransaction(new TransactionEntry(TransactionType.BOOKING_CONFIRMED, "BK001", "Status: CONFIRMED"));
        recovery.logTransaction(new TransactionEntry(TransactionType.PAYMENT_PROCESSED, "BK001", "Amount: 12500.00"));
        recovery.logTransaction(new TransactionEntry(TransactionType.BOOKING_CANCELLED, "BK002", "Cancelled by guest"));
        recovery.logTransaction(new TransactionEntry(TransactionType.ROOM_RELEASED,     "R202",  "Released after cancellation"));

        System.out.println("\n=== Saving System Snapshot ===");
        SystemSnapshot snapshot = new SystemSnapshot();
        snapshot.setRoomStatus("R201", "BOOKED");
        snapshot.setRoomStatus("R301", "AVAILABLE");
        snapshot.setBookingStatus("BK001", "CONFIRMED");
        snapshot.setBookingStatus("BK002", "CANCELLED");
        recovery.saveSnapshot(snapshot);

        System.out.println("\n=== Simulating System Crash & Recovery ===");
        System.out.println("-- Loading Snapshot --");
        SystemSnapshot restored = recovery.loadSnapshot();
        if (restored != null) restored.print();

        System.out.println("\n-- Replaying Transaction Log --");
        List<TransactionEntry> log = recovery.replayTransactionLog();
        System.out.println("\nTransaction History:");
        log.forEach(System.out::println);
    }
}
