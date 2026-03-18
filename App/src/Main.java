abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method to display room-specific details
    public abstract void displayDetails();
}

// -------------------- Concrete Room Types --------------------
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 50.0);
    }

    @Override
    public void displayDetails() {
        System.out.println(getRoomType() + " | Beds: " + getBeds() + " | Price: $" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 90.0);
    }

    @Override
    public void displayDetails() {
        System.out.println(getRoomType() + " | Beds: " + getBeds() + " | Price: $" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 150.0);
    }

    @Override
    public void displayDetails() {
        System.out.println(getRoomType() + " | Beds: " + getBeds() + " | Price: $" + getPrice());
    }
}

// -------------------- Main Class --------------------
public class UseCase2RoomInitialization {
    public static void main(String[] args) {
        // Step 1: Initialize static availability counts
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        // Step 2: Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Step 3: Display room details and availability
        System.out.println("=== Room Types & Availability ===");
        single.displayDetails();
        System.out.println("Available: " + singleRoomAvailable + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleRoomAvailable + "\n");

        suite.displayDetails();
        System.out.println("Available: " + suiteRoomAvailable + "\n");
    }
}