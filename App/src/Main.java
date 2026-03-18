import java.io.*;
import java.util.*;

// -------------------- Reservation (Serializable) --------------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// -------------------- Inventory (Serializable) --------------------
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public Map<String, Integer> getAvailability() {
        return availability;
    }

    public void display() {
        System.out.println("\n=== Inventory ===");
        for (String type : availability.keySet()) {
            System.out.println(type + " → " + availability.get(type));
        }
    }
}

// -------------------- Booking History (Serializable) --------------------
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void display() {
        System.out.println("\n=== Booking History ===");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// -------------------- Wrapper for Full System State --------------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Inventory inventory;
    BookingHistory history;

    public SystemState(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// -------------------- Persistence Service --------------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("\n✅ System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("\n⚠ No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n❌ Error loading state. Starting with empty data.");
        }

        // Fallback (safe state)
        return new SystemState(new Inventory(), new BookingHistory());
    }
}

// -------------------- Main Class --------------------
public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {

        // Step 1: Load previous state (if exists)
        SystemState state = PersistenceService.load();

        Inventory inventory = state.inventory;
        BookingHistory history = state.history;

        // Step 2: If fresh start, initialize data
        if (inventory.getAvailability().isEmpty()) {
            inventory.addRoomType("Single", 3);
            inventory.addRoomType("Suite", 2);

            history.addReservation(new Reservation("RES-101", "Alice", "Single"));
            history.addReservation(new Reservation("RES-102", "Bob", "Suite"));
        }

        // Step 3: Display current state
        inventory.display();
        history.display();

        // Step 4: Simulate new booking
        history.addReservation(new Reservation("RES-103", "Charlie", "Single"));
        System.out.println("\nNew booking added...");

        // Step 5: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, history));

        System.out.println("\n🔄 Restart the program to see recovered state.");
    }
}