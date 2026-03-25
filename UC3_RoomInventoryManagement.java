/**
 * UC3 - Centralized Room Inventory Management
 * Book My Stay App
 * Manages centralized room inventory including adding, updating, and removing rooms.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UC3_RoomInventoryManagement {

    // Room entity
    public static class Room {
        private String roomId;
        private String roomType;
        private double pricePerNight;
        private boolean isAvailable;
        private int floor;
        private String description;

        public Room(String roomId, String roomType, double pricePerNight, int floor, String description) {
            this.roomId = roomId;
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
            this.isAvailable = true;
            this.floor = floor;
            this.description = description;
        }

        // Getters & Setters
        public String getRoomId() { return roomId; }
        public String getRoomType() { return roomType; }
        public double getPricePerNight() { return pricePerNight; }
        public boolean isAvailable() { return isAvailable; }
        public int getFloor() { return floor; }
        public String getDescription() { return description; }
        public void setAvailable(boolean available) { isAvailable = available; }
        public void setPricePerNight(double price) { pricePerNight = price; }

        @Override
        public String toString() {
            return String.format("Room[%s | %s | Floor %d | ₹%.2f | %s]",
                    roomId, roomType, floor, pricePerNight, isAvailable ? "Available" : "Booked");
        }
    }

    // Centralized Inventory Manager
    public static class RoomInventoryManager {
        private final Map<String, Room> inventory = new HashMap<>();

        public boolean addRoom(Room room) {
            if (inventory.containsKey(room.getRoomId())) {
                System.out.println("Room ID " + room.getRoomId() + " already exists.");
                return false;
            }
            inventory.put(room.getRoomId(), room);
            System.out.println("Room added: " + room);
            return true;
        }

        public boolean removeRoom(String roomId) {
            if (!inventory.containsKey(roomId)) {
                System.out.println("Room ID " + roomId + " not found.");
                return false;
            }
            inventory.remove(roomId);
            System.out.println("Room " + roomId + " removed from inventory.");
            return true;
        }

        public boolean updateRoomPrice(String roomId, double newPrice) {
            Room room = inventory.get(roomId);
            if (room == null) {
                System.out.println("Room not found: " + roomId);
                return false;
            }
            room.setPricePerNight(newPrice);
            System.out.println("Updated price for " + roomId + " to ₹" + newPrice);
            return true;
        }

        public List<Room> getAvailableRooms() {
            List<Room> available = new ArrayList<>();
            for (Room room : inventory.values()) {
                if (room.isAvailable()) available.add(room);
            }
            return available;
        }

        public void displayInventory() {
            System.out.println("\n===== Room Inventory =====");
            if (inventory.isEmpty()) {
                System.out.println("No rooms in inventory.");
                return;
            }
            for (Room room : inventory.values()) {
                System.out.println(room);
            }
            System.out.println("Total Rooms: " + inventory.size());
        }

        public Room getRoom(String roomId) {
            return inventory.get(roomId);
        }
    }

    public static void main(String[] args) {
        RoomInventoryManager manager = new RoomInventoryManager();

        manager.addRoom(new Room("R101", "Single", 1500.00, 1, "Standard single room"));
        manager.addRoom(new Room("R201", "Double", 2500.00, 2, "Comfortable double room"));
        manager.addRoom(new Room("R301", "Suite", 5000.00, 3, "Luxury suite with sea view"));
        manager.addRoom(new Room("R202", "Deluxe", 3500.00, 2, "Deluxe room with balcony"));

        manager.displayInventory();
        manager.updateRoomPrice("R101", 1800.00);
        manager.removeRoom("R202");
        manager.displayInventory();

        System.out.println("\nAvailable Rooms:");
        manager.getAvailableRooms().forEach(System.out::println);
    }
}
