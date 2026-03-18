import java.util.*;

// -------------------- Reservation Model --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | " + roomId +
                " | Status: " + (isActive ? "ACTIVE" : "CANCELLED");
    }
}

// -------------------- Inventory --------------------
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public void increment(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public void display() {
        System.out.println("\n=== Inventory Status ===");
        for (String type : availability.keySet()) {
            System.out.println(type + " → " + availability.get(type));
        }
    }
}

// -------------------- Booking History --------------------
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        System.out.println("\n=== Booking History ===");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// -------------------- Cancellation Service --------------------
class CancellationService {

    private Inventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing Cancellation → " + reservationId);

        Reservation reservation = history.getReservation(reservationId);

        // Validation
        if (reservation == null) {
            System.out.println("❌ Cancellation Failed: Reservation does not exist.");
            return;
        }

        if (!reservation.isActive()) {
            System.out.println("❌ Cancellation Failed: Already cancelled.");
            return;
        }

        // Step 1: Record room ID in rollback stack
        rollbackStack.push(reservation.getRoomId());

        // Step 2: Restore inventory
        inventory.increment(reservation.getRoomType());

        // Step 3: Update reservation status
        reservation.cancel();

        // Step 4: Confirmation
        System.out.println("✅ Cancellation Successful!");
        System.out.println("Released Room ID: " + reservation.getRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("\n=== Rollback Stack (LIFO) ===");
        if (rollbackStack.isEmpty()) {
            System.out.println("No rollback data.");
            return;
        }

        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println(rollbackStack.get(i));
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase10BookingCancellation {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Suite", 0);

        // Step 2: Setup Booking History (Assume confirmed bookings)
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("RES-101", "Alice", "Single", "S-111AAA");
        Reservation r2 = new Reservation("RES-102", "Bob", "Suite", "SU-222BBB");

        history.addReservation(r1);
        history.addReservation(r2);

        // Step 3: Cancellation Service
        CancellationService cancelService = new CancellationService(inventory, history);

        // Step 4: Perform cancellations
        cancelService.cancelBooking("RES-101"); // valid
        cancelService.cancelBooking("RES-999"); // invalid
        cancelService.cancelBooking("RES-101"); // already cancelled

        // Step 5: Display results
        history.displayAll();
        inventory.display();
        cancelService.displayRollbackStack();
    }
}