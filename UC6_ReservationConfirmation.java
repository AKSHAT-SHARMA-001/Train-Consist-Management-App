/**
 * UC6 - Reservation Confirmation & Room Allocation
 * Book My Stay App
 * Confirms reservations and allocates specific rooms to guests.
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UC6_ReservationConfirmation {

    // Reservation model
    public static class Reservation {
        private String reservationId;
        private String bookingId;
        private String guestName;
        private String allocatedRoomId;
        private String roomType;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private double totalAmount;
        private LocalDateTime confirmedAt;
        private boolean confirmed;

        public Reservation(String bookingId, String guestName, String roomType,
                           LocalDate checkIn, LocalDate checkOut, double totalAmount) {
            this.reservationId = "RES-" + bookingId;
            this.bookingId = bookingId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.checkInDate = checkIn;
            this.checkOutDate = checkOut;
            this.totalAmount = totalAmount;
            this.confirmed = false;
        }

        public String getReservationId() { return reservationId; }
        public String getBookingId() { return bookingId; }
        public String getRoomType() { return roomType; }
        public String getAllocatedRoomId() { return allocatedRoomId; }

        public void confirm(String allocatedRoomId) {
            this.allocatedRoomId = allocatedRoomId;
            this.confirmedAt = LocalDateTime.now();
            this.confirmed = true;
        }

        public void printConfirmation() {
            System.out.println("\n============================================");
            System.out.println("     ✔  RESERVATION CONFIRMED  ✔          ");
            System.out.println("============================================");
            System.out.println("Reservation ID  : " + reservationId);
            System.out.println("Booking ID      : " + bookingId);
            System.out.println("Guest Name      : " + guestName);
            System.out.println("Allocated Room  : " + allocatedRoomId);
            System.out.println("Room Type       : " + roomType);
            System.out.println("Check-In        : " + checkInDate);
            System.out.println("Check-Out       : " + checkOutDate);
            System.out.printf("Total Amount    : ₹%.2f%n", totalAmount);
            System.out.println("Confirmed At    : " + confirmedAt);
            System.out.println("Status          : " + (confirmed ? "CONFIRMED" : "PENDING"));
            System.out.println("============================================");
        }
    }

    // Room Allocation Service
    public static class RoomAllocationService {

        // Simulated available rooms by type
        private final Map<String, String[]> availableRoomsByType = new HashMap<>();

        public RoomAllocationService() {
            availableRoomsByType.put("Single",  new String[]{"R101", "R103", "R105"});
            availableRoomsByType.put("Double",  new String[]{"R201", "R203"});
            availableRoomsByType.put("Suite",   new String[]{"R301"});
            availableRoomsByType.put("Deluxe",  new String[]{"R401", "R402"});
            availableRoomsByType.put("Family",  new String[]{"R501"});
        }

        public String allocateRoom(String roomType) {
            String[] rooms = availableRoomsByType.get(roomType);
            if (rooms == null || rooms.length == 0) {
                System.out.println("No rooms available for type: " + roomType);
                return null;
            }
            // Allocate first available room
            return rooms[0];
        }

        public Reservation confirmReservation(Reservation reservation) {
            String roomId = allocateRoom(reservation.getRoomType());
            if (roomId == null) {
                System.out.println("Reservation failed: No room available.");
                return null;
            }
            reservation.confirm(roomId);
            System.out.println("Room " + roomId + " allocated to reservation " + reservation.getReservationId());
            return reservation;
        }
    }

    public static void main(String[] args) {
        Reservation reservation = new Reservation(
                "BKA1B2C3D4", "Priya Sharma", "Double",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                10000.00
        );

        RoomAllocationService service = new RoomAllocationService();
        Reservation confirmed = service.confirmReservation(reservation);

        if (confirmed != null) {
            confirmed.printConfirmation();
        }
    }
}
