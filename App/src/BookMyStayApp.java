// Version 2.1 – Refactored Version
// Demonstrates abstraction, inheritance, polymorphism and static availability

// Abstract class
abstract class Room {

    protected String roomType;
    protected int numberOfBeds;
    protected int roomSize;
    protected double pricePerNight;

    public Room(String roomType, int numberOfBeds, int roomSize, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.roomSize = roomSize;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Room Size: " + roomSize + " sq.ft");
        System.out.println("Price per Night: $" + pricePerNight);
    }
}


// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 80);
    }
}


// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 150);
    }
}


// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 300);
    }
}


// Main Application Class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        // Static availability variables
        int singleRoomAvailable = 10;
        int doubleRoomAvailable = 7;
        int suiteRoomAvailable = 3;

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("===== BOOK MY STAY - ROOM DETAILS =====\n");

        System.out.println("Single Room Details:");
        single.displayRoomDetails();
        System.out.println("Available: " + singleRoomAvailable);
        System.out.println();

        System.out.println("Double Room Details:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleRoomAvailable);
        System.out.println();

        System.out.println("Suite Room Details:");
        suite.displayRoomDetails();
        System.out.println("Available: " + suiteRoomAvailable);
        System.out.println();

        System.out.println("Application Terminated.");
    }
}