/**
 * UC9 - Error Handling & Validation
 * Book My Stay App
 * Provides centralized error handling and input validation across the application.
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UC9_ErrorHandlingValidation {

    // Custom application exceptions
    public static class BookingException extends RuntimeException {
        private final String errorCode;
        public BookingException(String errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
        }
        public String getErrorCode() { return errorCode; }
    }

    public static class RoomNotFoundException extends BookingException {
        public RoomNotFoundException(String roomId) {
            super("ROOM_404", "Room not found: " + roomId);
        }
    }

    public static class RoomNotAvailableException extends BookingException {
        public RoomNotAvailableException(String roomId) {
            super("ROOM_UNAVAILABLE", "Room is not available: " + roomId);
        }
    }

    public static class InvalidDateException extends BookingException {
        public InvalidDateException(String message) {
            super("INVALID_DATE", message);
        }
    }

    public static class InvalidGuestException extends BookingException {
        public InvalidGuestException(String message) {
            super("INVALID_GUEST", message);
        }
    }

    // Validation result model
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors = new ArrayList<>();

        public ValidationResult(boolean valid) { this.valid = valid; }
        public boolean isValid() { return valid && errors.isEmpty(); }
        public void addError(String error) { errors.add(error); }
        public List<String> getErrors() { return errors; }

        public void printResult() {
            if (isValid()) {
                System.out.println("✔ Validation Passed.");
            } else {
                System.out.println("✘ Validation Failed:");
                errors.forEach(e -> System.out.println("  - " + e));
            }
        }
    }

    // Validator class
    public static class BookingValidator {

        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        private static final Pattern PHONE_PATTERN =
                Pattern.compile("^[6-9]\\d{9}$");

        public ValidationResult validateGuestInfo(String name, String email, String phone) {
            ValidationResult result = new ValidationResult(true);

            if (name == null || name.trim().isEmpty()) {
                result.addError("Guest name is required.");
            } else if (name.trim().length() < 2) {
                result.addError("Guest name must be at least 2 characters.");
            }

            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                result.addError("Invalid email address: " + email);
            }

            if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
                result.addError("Invalid phone number (must be 10 digits, starting with 6-9): " + phone);
            }

            return result;
        }

        public ValidationResult validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
            ValidationResult result = new ValidationResult(true);

            if (checkIn == null) {
                result.addError("Check-in date is required.");
            }
            if (checkOut == null) {
                result.addError("Check-out date is required.");
            }
            if (checkIn != null && checkIn.isBefore(LocalDate.now())) {
                result.addError("Check-in date cannot be in the past.");
            }
            if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
                result.addError("Check-out date must be after check-in date.");
            }

            return result;
        }

        public ValidationResult validateGuests(int numberOfGuests, int maxOccupancy) {
            ValidationResult result = new ValidationResult(true);

            if (numberOfGuests <= 0) {
                result.addError("Number of guests must be at least 1.");
            }
            if (numberOfGuests > maxOccupancy) {
                result.addError("Number of guests (" + numberOfGuests
                        + ") exceeds room occupancy (" + maxOccupancy + ").");
            }

            return result;
        }
    }

    // Global error handler
    public static class ErrorHandler {

        public static void handle(BookingException e) {
            System.out.println("\n[ERROR " + e.getErrorCode() + "] " + e.getMessage());
        }

        public static void handle(Exception e) {
            System.out.println("\n[UNEXPECTED ERROR] " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BookingValidator validator = new BookingValidator();

        System.out.println("=== Testing Guest Validation ===");
        validator.validateGuestInfo("Arjun Kumar", "arjun@email.com", "9876543210").printResult();
        validator.validateGuestInfo("", "bad-email", "12345").printResult();

        System.out.println("\n=== Testing Date Validation ===");
        validator.validateBookingDates(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5)).printResult();
        validator.validateBookingDates(LocalDate.now().minusDays(1), LocalDate.now()).printResult();

        System.out.println("\n=== Testing Guest Count Validation ===");
        validator.validateGuests(2, 4).printResult();
        validator.validateGuests(6, 4).printResult();

        System.out.println("\n=== Testing Exception Handling ===");
        try {
            throw new RoomNotFoundException("R999");
        } catch (BookingException e) {
            ErrorHandler.handle(e);
        }

        try {
            throw new InvalidDateException("Check-out must be after check-in.");
        } catch (BookingException e) {
            ErrorHandler.handle(e);
        }
    }
}
