import java.util.*;

// -------------------- Custom Exception --------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------------------- Reservation Model --------------------
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

// -------------------- Inventory --------------------
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public int getAvailableCount(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) throws InvalidBookingException {
        int count = getAvailableCount(type);

        if (count <= 0) {
            throw new InvalidBookingException("No available rooms for type: " + type);
        }

        availability.put(type, count - 1);
    }
}

// -------------------- Validator --------------------
class BookingValidator {

    public static void validate(Reservation reservation, Inventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailableCount(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "Room not available for type: " + reservation.getRoomType());
        }
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    private Inventory inventory;

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(Reservation reservation) {
        try {
            // Step 1: Validate (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room (only if valid)
            inventory.decrement(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("✅ Booking Successful for " + reservation.getGuestName() +
                    " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking Failed: " + e.getMessage());
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Suite", 0);

        // Step 2: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 3: Test Cases (Valid + Invalid)

        // Valid booking
        bookingService.bookRoom(new Reservation("Alice", "Single"));

        // Invalid: No availability
        bookingService.bookRoom(new Reservation("Bob", "Suite"));

        // Invalid: Unknown room type
        bookingService.bookRoom(new Reservation("Charlie", "Deluxe"));

        // Invalid: Empty guest name
        bookingService.bookRoom(new Reservation("", "Single"));

        // Invalid: Booking after inventory exhausted
        bookingService.bookRoom(new Reservation("David", "Single"));
    }
}