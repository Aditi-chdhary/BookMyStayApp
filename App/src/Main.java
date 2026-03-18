import java.util.*;

// -------------------- Reservation Model --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId;
    }
}

// -------------------- Booking History --------------------
class BookingHistory {
    // Ordered storage (chronological)
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all reservations (read-only)
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history); // defensive copy
    }
}

// -------------------- Reporting Service --------------------
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n=== Booking History ===");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {
        System.out.println("\n=== Booking Summary Report ===");

        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Total Bookings: " + reservations.size());

        System.out.println("\nBookings by Room Type:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " → " + roomTypeCount.get(type));
        }
    }
}

// -------------------- Main Class --------------------
public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {

        // Step 1: Create Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES-101", "Alice", "Single", "S-123ABC"));
        history.addReservation(new Reservation("RES-102", "Bob", "Suite", "SU-456DEF"));
        history.addReservation(new Reservation("RES-103", "Charlie", "Single", "S-789GHI"));
        history.addReservation(new Reservation("RES-104", "Diana", "Double", "D-321JKL"));

        // Step 3: Reporting Service
        BookingReportService reportService = new BookingReportService();

        // Step 4: Admin views booking history
        List<Reservation> allReservations = history.getAllReservations();
        reportService.displayAllBookings(allReservations);

        // Step 5: Admin generates summary report
        reportService.generateSummaryReport(allReservations);
    }
}