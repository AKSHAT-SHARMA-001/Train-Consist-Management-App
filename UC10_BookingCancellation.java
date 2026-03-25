/**
 * UC10 - Booking Cancellation & Inventory Rollback
 * Book My Stay App
 * Handles booking cancellations and rolls back room inventory upon cancellation.
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class UC10_BookingCancellation {

    // Cancellation policy enum
    public enum CancellationPolicy {
        FULL_REFUND("Full Refund", 100),
        PARTIAL_REFUND("50% Refund", 50),
        NO_REFUND("No Refund", 0);

        private final String label;
        private final int refundPercentage;

        CancellationPolicy(String label, int refundPercentage) {
            this.label = label;
            this.refundPercentage = refundPercentage;
        }

        public String getLabel() { return label; }
        public int getRefundPercentage() { return refundPercentage; }
    }

    // Cancellation record model
    public static class CancellationRecord {
        private String cancellationId;
        private String bookingId;
        private String roomId;
        private double originalAmount;
        private double refundAmount;
        private CancellationPolicy policy;
        private LocalDateTime cancelledAt;

        public CancellationRecord(String bookingId, String roomId, double originalAmount,
                                  CancellationPolicy policy) {
            this.cancellationId = "CXL-" + bookingId;
            this.bookingId = bookingId;
            this.roomId = roomId;
            this.originalAmount = originalAmount;
            this.policy = policy;
            this.refundAmount = originalAmount * policy.getRefundPercentage() / 100.0;
            this.cancelledAt = LocalDateTime.now();
        }

        public String getRoomId() { return roomId; }

        public void printCancellationSummary() {
            System.out.println("\n========== Cancellation Summary ==========");
            System.out.println("Cancellation ID  : " + cancellationId);
            System.out.println("Booking ID       : " + bookingId);
            System.out.println("Room ID          : " + roomId);
            System.out.printf("Original Amount  : ₹%.2f%n", originalAmount);
            System.out.println("Policy Applied   : " + policy.getLabel());
            System.out.printf("Refund Amount    : ₹%.2f%n", refundAmount);
            System.out.println("Cancelled At     : " + cancelledAt);
            System.out.println("==========================================");
        }
    }

    // Room Inventory (simulated)
    public static class RoomInventory {
        private final Map<String, Boolean> roomAvailability = new HashMap<>();

        public RoomInventory() {
            roomAvailability.put("R101", false); // booked
            roomAvailability.put("R201", false); // booked
            roomAvailability.put("R301", true);  // available
            roomAvailability.put("R401", false); // booked
        }

        public boolean rollbackRoom(String roomId) {
            if (!roomAvailability.containsKey(roomId)) {
                System.out.println("Room not found in inventory: " + roomId);
                return false;
            }
            roomAvailability.put(roomId, true);
            System.out.println("Inventory rollback: Room " + roomId + " is now AVAILABLE.");
            return true;
        }

        public void displayInventory() {
            System.out.println("\n--- Room Inventory Status ---");
            roomAvailability.forEach((id, available) ->
                    System.out.println("Room " + id + " : " + (available ? "Available" : "Booked")));
        }
    }

    // Cancellation Service
    public static class CancellationService {

        private final RoomInventory inventory;

        public CancellationService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public CancellationPolicy determineCancellationPolicy(LocalDate checkInDate) {
            long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), checkInDate);
            if (daysUntilCheckIn > 7) return CancellationPolicy.FULL_REFUND;
            if (daysUntilCheckIn >= 3) return CancellationPolicy.PARTIAL_REFUND;
            return CancellationPolicy.NO_REFUND;
        }

        public CancellationRecord cancelBooking(String bookingId, String roomId,
                                                double totalAmount, LocalDate checkInDate) {
            System.out.println("Processing cancellation for booking: " + bookingId);

            CancellationPolicy policy = determineCancellationPolicy(checkInDate);
            System.out.println("Cancellation policy applied: " + policy.getLabel());

            // Rollback inventory
            boolean rolledBack = inventory.rollbackRoom(roomId);
            if (!rolledBack) {
                System.out.println("Warning: Could not rollback inventory for room " + roomId);
            }

            CancellationRecord record = new CancellationRecord(bookingId, roomId, totalAmount, policy);
            record.printCancellationSummary();
            return record;
        }
    }

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService(inventory);

        System.out.println("=== Before Cancellation ===");
        inventory.displayInventory();

        // Cancel a booking
        service.cancelBooking("BK001", "R201", 7500.00, LocalDate.now().plusDays(10));

        System.out.println("\n=== After Cancellation ===");
        inventory.displayInventory();

        // Cancel with no refund scenario
        service.cancelBooking("BK004", "R401", 17500.00, LocalDate.now().plusDays(1));
        inventory.displayInventory();
    }
}
