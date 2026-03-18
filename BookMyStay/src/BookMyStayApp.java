import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("-----------------------------------");
    }
}

// Inventory: Centralized State Holder
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only access
    public int getAvailableCount(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Service: Search (Read-Only)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        boolean found = false;

        for (String type : inventory.getAllRoomTypes()) {
            int available = inventory.getAvailableCount(type);

            // Validation Logic: Filter unavailable rooms
            if (available > 0) {
                Room room = roomCatalog.get(type);

                if (room != null) { // Defensive Programming
                    room.displayDetails();
                    System.out.println("Available Count: " + available);
                    System.out.println();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

// Main Class
public class UseCase4RoomSearch {
    public static void main(String[] args) {

        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);  // Unavailable
        inventory.addRoom("Suite", 2);

        // Step 2: Create Room Catalog (Domain Model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 2000, Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double",
                new Room("Double", 3500, Arrays.asList("WiFi", "TV", "AC")));

        roomCatalog.put("Suite",
                new Room("Suite", 6000, Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Step 3: Search Service (Read-only operation)
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Guest performs search
        searchService.searchAvailableRooms();
    }
}