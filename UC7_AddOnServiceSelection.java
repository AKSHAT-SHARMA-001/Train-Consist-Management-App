/**
 * UC7 - Add-On Service Selection
 * Book My Stay App
 * Allows guests to select additional services along with their room booking.
 */
import java.util.ArrayList;
import java.util.List;

public class UC7_AddOnServiceSelection {

    // Add-On Service enum
    public enum AddOnService {
        BREAKFAST("Breakfast",           250.00, "Daily breakfast included"),
        AIRPORT_PICKUP("Airport Pickup", 800.00, "One-way airport transfer"),
        SPA("Spa Session",               1500.00, "60-minute relaxation spa"),
        LAUNDRY("Laundry Service",       200.00, "Per day laundry"),
        PARKING("Parking",               150.00, "Per day parking slot"),
        DINNER("Dinner Buffet",          500.00, "Inclusive dinner buffet"),
        GYM("Gym Access",               100.00, "Full day gym access"),
        TOUR("City Tour",               1200.00, "Half-day guided city tour");

        private final String name;
        private final double price;
        private final String description;

        AddOnService(String name, double price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getDescription() { return description; }
    }

    // Selected Add-On with quantity
    public static class SelectedAddOn {
        private AddOnService service;
        private int quantity;

        public SelectedAddOn(AddOnService service, int quantity) {
            this.service = service;
            this.quantity = quantity;
        }

        public AddOnService getService() { return service; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return service.getPrice() * quantity; }

        @Override
        public String toString() {
            return String.format("%-20s x%-3d ₹%-8.2f = ₹%.2f",
                    service.getName(), quantity, service.getPrice(), getTotalPrice());
        }
    }

    // Add-On Service Manager
    public static class AddOnServiceManager {
        private final String bookingId;
        private final List<SelectedAddOn> selectedServices = new ArrayList<>();

        public AddOnServiceManager(String bookingId) {
            this.bookingId = bookingId;
        }

        public void displayAvailableServices() {
            System.out.println("\n======= Available Add-On Services =======");
            System.out.printf("%-5s %-20s %-10s %s%n", "No.", "Service", "Price", "Description");
            System.out.println("--------------------------------------------------");
            AddOnService[] services = AddOnService.values();
            for (int i = 0; i < services.length; i++) {
                System.out.printf("%-5d %-20s ₹%-9.2f %s%n",
                        (i + 1), services[i].getName(), services[i].getPrice(), services[i].getDescription());
            }
            System.out.println("==================================================");
        }

        public boolean addService(AddOnService service, int quantity) {
            if (quantity <= 0) {
                System.out.println("Invalid quantity for service: " + service.getName());
                return false;
            }
            selectedServices.add(new SelectedAddOn(service, quantity));
            System.out.println("Added: " + service.getName() + " x" + quantity);
            return true;
        }

        public double calculateTotalAddOnCost() {
            return selectedServices.stream().mapToDouble(SelectedAddOn::getTotalPrice).sum();
        }

        public void displaySelectedServices() {
            System.out.println("\n===== Selected Add-On Services for Booking: " + bookingId + " =====");
            if (selectedServices.isEmpty()) {
                System.out.println("No add-on services selected.");
                return;
            }
            selectedServices.forEach(System.out::println);
            System.out.printf("%nTotal Add-On Cost: ₹%.2f%n", calculateTotalAddOnCost());
            System.out.println("================================================================");
        }
    }

    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager("BKA1B2C3D4");

        manager.displayAvailableServices();

        // Simulate guest selections
        manager.addService(AddOnService.BREAKFAST, 3);
        manager.addService(AddOnService.AIRPORT_PICKUP, 1);
        manager.addService(AddOnService.SPA, 2);
        manager.addService(AddOnService.PARKING, 5);

        manager.displaySelectedServices();
    }
}
