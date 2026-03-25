/**
 * UC11 - Concurrent Booking Simulation
 * Book My Stay App
 * Simulates concurrent booking requests and demonstrates thread-safe room allocation.
 */
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

public class UC11_ConcurrentBookingSimulation {

    // Thread-safe room availability tracker
    public static class ConcurrentRoomManager {
        // Room ID -> available count
        private final ConcurrentHashMap<String, AtomicInteger> roomAvailability = new ConcurrentHashMap<>();
        private final AtomicInteger successfulBookings = new AtomicInteger(0);
        private final AtomicInteger failedBookings = new AtomicInteger(0);

        public ConcurrentRoomManager() {
            roomAvailability.put("R201", new AtomicInteger(3)); // 3 double rooms
            roomAvailability.put("R301", new AtomicInteger(1)); // 1 suite
            roomAvailability.put("R101", new AtomicInteger(5)); // 5 single rooms
        }

        // Thread-safe booking attempt
        public boolean attemptBooking(String roomId, String guestName) {
            AtomicInteger count = roomAvailability.get(roomId);
            if (count == null) {
                System.out.println("[FAIL] " + guestName + " -> Room " + roomId + " does not exist.");
                failedBookings.incrementAndGet();
                return false;
            }

            // Atomic decrement — only succeeds if > 0
            int remaining;
            do {
                remaining = count.get();
                if (remaining <= 0) {
                    System.out.println("[FAIL] " + guestName + " -> Room " + roomId + " fully booked.");
                    failedBookings.incrementAndGet();
                    return false;
                }
            } while (!count.compareAndSet(remaining, remaining - 1));

            System.out.println("[SUCCESS] " + guestName + " booked room " + roomId
                    + " | Remaining: " + (remaining - 1));
            successfulBookings.incrementAndGet();
            return true;
        }

        public void printStats() {
            System.out.println("\n===== Booking Simulation Stats =====");
            System.out.println("Successful Bookings : " + successfulBookings.get());
            System.out.println("Failed Bookings     : " + failedBookings.get());
            System.out.println("\nFinal Room Availability:");
            roomAvailability.forEach((room, count) ->
                    System.out.println("  " + room + " : " + count.get() + " remaining"));
            System.out.println("=====================================");
        }
    }

    // Booking task for each guest thread
    public static class BookingTask implements Callable<Boolean> {
        private final ConcurrentRoomManager manager;
        private final String guestName;
        private final String roomId;

        public BookingTask(ConcurrentRoomManager manager, String guestName, String roomId) {
            this.manager = manager;
            this.guestName = guestName;
            this.roomId = roomId;
        }

        @Override
        public Boolean call() {
            // Simulate slight processing delay
            try { Thread.sleep(new Random().nextInt(50)); } catch (InterruptedException ignored) {}
            return manager.attemptBooking(roomId, guestName);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentRoomManager manager = new ConcurrentRoomManager();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        System.out.println("===== Concurrent Booking Simulation Started =====\n");

        List<Future<Boolean>> futures = new ArrayList<>();

        // Simulate 10 guests trying to book the suite (only 1 available)
        String[] suiteGuests = {"Alice", "Bob", "Charlie", "Diana", "Eve",
                                "Frank", "Grace", "Hank", "Ivy", "Jack"};
        for (String guest : suiteGuests) {
            futures.add(executor.submit(new BookingTask(manager, guest, "R301")));
        }

        // Simulate 5 guests booking double rooms (3 available)
        String[] doubleGuests = {"Kumar", "Priya", "Ravi", "Sneha", "Vivek"};
        for (String guest : doubleGuests) {
            futures.add(executor.submit(new BookingTask(manager, guest, "R201")));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        manager.printStats();
    }
}
