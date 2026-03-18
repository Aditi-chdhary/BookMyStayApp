import java.util.*;

// -------------------- Add-On Service Model --------------------
class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// -------------------- Add-On Service Manager --------------------
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> reservationServicesMap = new HashMap<>();

    // Add services to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service Added → " + service + " for Reservation ID: " + reservationId);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of add-ons
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        double total = 0;
        for (AddOnService service : services) {
            total += service.getPrice();
        }
        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\n=== Add-On Services for Reservation: " + reservationId + " ===");

        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService service : services) {
            System.out.println(service);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// -------------------- Main Class --------------------
public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {

        // Step 1: Assume reservation IDs (from Use Case 6)
        String reservation1 = "RES-101";
        String reservation2 = "RES-102";

        // Step 2: Create Add-On Services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService extraBed = new AddOnService("Extra Bed", 800);

        // Step 3: Create Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Step 4: Guest selects services
        serviceManager.addService(reservation1, breakfast);
        serviceManager.addService(reservation1, airportPickup);

        serviceManager.addService(reservation2, extraBed);

        // Step 5: Display services and cost
        serviceManager.displayServices(reservation1);
        serviceManager.displayServices(reservation2);
    }
}
