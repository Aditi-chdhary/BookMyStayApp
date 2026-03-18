// Version 3.1 – Refactored Version
// Demonstrates centralized inventory using HashMap

import java.util.HashMap;
import java.util.Map;

// RoomInventory class responsible for managing room availability
class RoomInventory {

    // HashMap to store room type and availability
    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with initial availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 7);
        inventory.put("Suite Room", 3);
    }

    // Method to get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Method to display current inventory
    public void displayInventory() {
        System.out.println("===== CURRENT ROOM INVENTORY =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

// Main class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        System.out.println();

        // Example: Update availability
        System.out.println("Updating Single Room availability...\n");
        inventory.updateAvailability("Single Room", 8);

        // Display updated inventory
        inventory.displayInventory();
    }
}