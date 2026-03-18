import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Thread-Safe Inventory --------------------
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public Inventory() {
        availability.put("Single", 2);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String roomType) {
        int count = availability.getOrDefault(roomType, 0);

        if (count > 0) {
            // Simulate processing delay (to expose race conditions if not synchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            availability.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nFinal Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " → " + availability.get(type));
        }
    }
}

// -------------------- Shared Booking Queue --------------------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// -------------------- Booking Processor (Thread) --------------------
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(String name, BookingQueue queue, Inventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation request;

            // Synchronize queue access
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break;
            }

            // Try allocation (thread-safe)
            boolean success = inventory.allocateRoom(request.getRoomType());

            if (success) {
                System.out.println(getName() + " ✅ Booked for "
                        + request.getGuestName());
            } else {
                System.out.println(getName() + " ❌ Failed for "
                        + request.getGuestName());
            }
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {

        // Step 1: Shared Resources
        Inventory inventory = new Inventory();
        BookingQueue queue = new BookingQueue();

        // Step 2: Add multiple booking requests (more than availability)
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("Diana", "Single"));

        // Step 3: Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Step 4: Start threads
        t1.start();
        t2.start();
        t3.start();

        // Step 5: Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 6: Final inventory check
        inventory.display();
    }
}