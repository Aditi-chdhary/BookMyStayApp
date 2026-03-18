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

// -------------------- Booking Request Queue --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request Added → " + reservation);
    }

    // View all requests (without removing)
    public void viewRequests() {
        System.out.println("\n=== Booking Request Queue (FIFO Order) ===");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            System.out.println(r);
        }
    }

    // Get next request (for future processing)
    public Reservation getNextRequest() {
        return queue.peek(); // Read-only (no removal)
    }
}

// -------------------- Main Class --------------------
public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {

        // Step 1: Create Booking Request Queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Step 2: Guests submit booking requests
        requestQueue.addRequest(new Reservation("Alice", "Single"));
        requestQueue.addRequest(new Reservation("Bob", "Suite"));
        requestQueue.addRequest(new Reservation("Charlie", "Double"));
        requestQueue.addRequest(new Reservation("Diana", "Single"));

        // Step 3: View queue (FIFO order preserved)
        requestQueue.viewRequests();

        // Step 4: Peek next request (no removal, no allocation yet)
        System.out.println("\nNext Request to Process:");
        System.out.println(requestQueue.getNextRequest());
    }
}git