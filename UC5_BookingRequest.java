/**
 * UC5 - Booking Request
 * Book My Stay App
 * Handles the creation and submission of booking requests by users.
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UC5_BookingRequest {

    // Booking status enum
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, FAILED
    }

    // Guest model
    public static class Guest {
        private String guestId;
        private String name;
        private String email;
        private String phone;

        public Guest(String guestId, String name, String email, String phone) {
            this.guestId = guestId;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public String getGuestId() { return guestId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
    }

    // Booking Request model
    public static class BookingRequest {
        private String bookingId;
        private Guest guest;
        private String roomId;
        private String roomType;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private int numberOfGuests;
        private double totalAmount;
        private BookingStatus status;

        public BookingRequest(Guest guest, String roomId, String roomType,
                              LocalDate checkIn, LocalDate checkOut, int guests, double pricePerNight) {
            this.bookingId = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.guest = guest;
            this.roomId = roomId;
            this.roomType = roomType;
            this.checkInDate = checkIn;
            this.checkOutDate = checkOut;
            this.numberOfGuests = guests;
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            this.totalAmount = nights * pricePerNight;
            this.status = BookingStatus.PENDING;
        }

        public String getBookingId() { return bookingId; }
        public BookingStatus getStatus() { return status; }
        public void setStatus(BookingStatus status) { this.status = status; }
        public double getTotalAmount() { return totalAmount; }
        public String getRoomId() { return roomId; }

        public void displayBookingSummary() {
            long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            System.out.println("\n========== Booking Request Summary ==========");
            System.out.println("Booking ID   : " + bookingId);
            System.out.println("Guest Name   : " + guest.getName());
            System.out.println("Email        : " + guest.getEmail());
            System.out.println("Phone        : " + guest.getPhone());
            System.out.println("Room ID      : " + roomId);
            System.out.println("Room Type    : " + roomType);
            System.out.println("Check-In     : " + checkInDate);
            System.out.println("Check-Out    : " + checkOutDate);
            System.out.println("Nights       : " + nights);
            System.out.println("Guests       : " + numberOfGuests);
            System.out.printf("Total Amount : ₹%.2f%n", totalAmount);
            System.out.println("Status       : " + status);
            System.out.println("==============================================");
        }
    }

    // Booking Request Service
    public static class BookingRequestService {

        public BookingRequest createBookingRequest(Guest guest, String roomId, String roomType,
                                                   LocalDate checkIn, LocalDate checkOut,
                                                   int guests, double pricePerNight) {
            // Basic validation
            if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
                System.out.println("Invalid dates: Check-in must be before check-out.");
                return null;
            }
            if (guests <= 0) {
                System.out.println("Invalid number of guests.");
                return null;
            }

            BookingRequest request = new BookingRequest(guest, roomId, roomType,
                    checkIn, checkOut, guests, pricePerNight);
            System.out.println("Booking request created with ID: " + request.getBookingId());
            return request;
        }

        public void submitRequest(BookingRequest request) {
            if (request == null) {
                System.out.println("Cannot submit null booking request.");
                return;
            }
            // Simulate submission processing
            System.out.println("Submitting booking request: " + request.getBookingId() + "...");
            request.setStatus(BookingStatus.CONFIRMED);
            System.out.println("Booking request submitted successfully. Status: " + request.getStatus());
        }
    }

    public static void main(String[] args) {
        Guest guest = new Guest("G001", "Arjun Kumar", "arjun@email.com", "9876543210");

        BookingRequestService service = new BookingRequestService();
        BookingRequest request = service.createBookingRequest(
                guest, "R201", "Double",
                LocalDate.of(2025, 4, 10),
                LocalDate.of(2025, 4, 15),
                2, 2500.00
        );

        if (request != null) {
            request.displayBookingSummary();
            service.submitRequest(request);
        }
    }
}
