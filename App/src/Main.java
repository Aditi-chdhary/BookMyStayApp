import java.util.*;

// -------------------- Reservation (Request Model) --------------------
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

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Room Type: " + roomType;
    }
}

// -------------------- Inventory Service --------------------
class InventoryService {
    private Map<String, Integer> availabilityMap = new HashMap<>();

    public void addRoomType(String type, int count) {
        availabilityMap.put(type, count);
    }

    public int getAvailableCount(String type) {
        return availabilityMap.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        availabilityMap.put(type, availabilityMap.get(type) - 1);
    }
}

// -------------------- Booking Request Queue --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- Booking Service (Allocation Logic) --------------------
class BookingService {
    private InventoryService inventory;

    // Track allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → assigned room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processRequests(BookingRequestQueue requestQueue) {

        while (!requestQueue.isEmpty()) {
            Reservation request = requestQueue.getNextRequest();

            String type = request.getRoomType();
            System.out.println("\nProcessing → " + request);

            // Check availability
            if (inventory.getAvailableCount(type) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(type);

                // Ensure uniqueness (defensive)
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(type);
                }

                // Allocate room (atomic logical step)
                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.decrementRoom(type);

                // Confirm reservation
                System.out.println("✅ Booking Confirmed!");
                System.out.println("Guest   : " + request.getGuestName());
                System.out.println("Room ID : " + roomId);
                System.out.println("Type    : " + type);

            } else {
                System.out.println("❌ Booking Failed (No availability for " + type + ")");
            }
        }
    }

    // Room ID generator
    private String generateRoomId(String type) {
        return type.substring(0, 1).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
}

// -------------------- Main Class --------------------
public class UseCase6RoomAllocationService {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Suite", 1);

        // Step 2: Setup Booking Queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("Diana", "Single")); // Should fail

        // Step 3: Process Bookings
        BookingService bookingService = new BookingService(inventory);
        bookingService.processRequests(queue);
    }
}