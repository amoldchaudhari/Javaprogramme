import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class MenuItem {
    private int menuID;
    private String name;
    private double price;

    public MenuItem(int menuID, String name, double price) {
        this.menuID = menuID;
        this.name = name;
        this.price = price;
    }

    // Getters and setters

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Order {
    private int orderID;
    private List<MenuItem> menuItems;
    private List<Integer> quantities;
    private double totalBillAmount;
    private Date date;
    private String status;

    public Order(int orderID, List<MenuItem> menuItems, List<Integer> quantities, Date date, String status) {
        this.orderID = orderID;
        this.menuItems = menuItems;
        this.quantities = quantities;
        this.date = date;
        this.status = status;
        calculateTotalBillAmount();
    }

    // Getters and setters

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        calculateTotalBillAmount();
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
        calculateTotalBillAmount();
    }

    public double getTotalBillAmount() {
        return totalBillAmount;
    }

    private void calculateTotalBillAmount() {
        double billAmount = 0;
        for (int i = 0; i < menuItems.size(); i++) {
            billAmount += menuItems.get(i).getPrice() * quantities.get(i);
        }
        this.totalBillAmount = billAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class CollectionReport {
    private Date date;
    private double totalCollectionAmount;

    public CollectionReport(Date date, double totalCollectionAmount) {
        this.date = date;
        this.totalCollectionAmount = totalCollectionAmount;
    }

    // Getters and setters

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalCollectionAmount() {
        return totalCollectionAmount;
    }

    public void setTotalCollectionAmount(double totalCollectionAmount) {
        this.totalCollectionAmount = totalCollectionAmount;
    }
}

class GetFileData {
    private static final String MENU_ITEMS_FILE = "menu_items.csv";
    private static final String ORDER_DETAILS_FILE = "order_details.csv";
    private static final String COLLECTION_REPORT_FILE = "collection_report.csv";

    public static List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MENU_ITEMS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int menuID = Integer.parseInt(data[0]);
                String name = data[1];
                double price = Double.parseDouble(data[2]);
                menuItems.add(new MenuItem(menuID, name, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public static List<Order> getOrderDetails() {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ORDER_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int orderID = Integer.parseInt(data[0]);
                List<MenuItem> menuItems = getMenuItemsByIds(Arrays.asList(data[1].split(";")));
                List<Integer> quantities = getQuantities(Arrays.asList(data[2].split(";")));
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data[3]);
                String status = data[4];
                orders.add(new Order(orderID, menuItems, quantities, date, status));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return orders;
    }

    static List<MenuItem> getMenuItemsByIds(List<String> menuIDs) {
        List<MenuItem> menuItems = getMenuItems();
        List<MenuItem> selectedMenuItems = new ArrayList<>();
        for (String menuID : menuIDs) {
            int id = Integer.parseInt(menuID);
            for (MenuItem menuItem : menuItems) {
                if (menuItem.getMenuID() == id) {
                    selectedMenuItems.add(menuItem);
                    break;
                }
            }
        }
        return selectedMenuItems;
    }

    static List<Integer> getQuantities(List<String> quantities) {
        List<Integer> quantityList = new ArrayList<>();
        for (String quantity : quantities) {
            quantityList.add(Integer.parseInt(quantity));
        }
        return quantityList;
    }

    public static List<CollectionReport> getCollectionReports() {
        List<CollectionReport> collectionReports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COLLECTION_REPORT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data[0]);
                double totalCollectionAmount = Double.parseDouble(data[1]);
                collectionReports.add(new CollectionReport(date, totalCollectionAmount));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return collectionReports;
    }
}

class RestaurantApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<MenuItem> menuItems = GetFileData.getMenuItems();
    private static final List<Order> orders = GetFileData.getOrderDetails();
    private static final List<CollectionReport> collectionReports = GetFileData.getCollectionReports();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome to Quick-Bites Restaurant Management System!");
            System.out.println("1. Place an order");
            System.out.println("2. Cancel an order");
            System.out.println("3. Track daily collections");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    placeOrder();
                    break;
                case 2:
                    cancelOrder();
                    break;
                case 3:
                    trackCollections();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void placeOrder() {
        System.out.println("Menu Items:");
        for (MenuItem menuItem : menuItems) {
            System.out.println(menuItem.getMenuID() + ". " + menuItem.getName() + " - $" + menuItem.getPrice());
        }

        System.out.print("Enter the menu item IDs (pizza, burger, sandwich, coffee, tea): ");
        String[] menuIDs = scanner.nextLine().split(",");
        List<MenuItem> selectedMenuItems = GetFileData.getMenuItemsByIds(Arrays.asList(menuIDs));

        System.out.print("Enter the quantities (comma-separated): ");
        String[] quantityArray = scanner.nextLine().split(",");
        List<Integer> quantities = GetFileData.getQuantities(Arrays.asList(quantityArray));

        System.out.print("Enter the order date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format!");
            return;
        }

        int lastOrderID = getLastOrderID();
        Order order = new Order(lastOrderID + 1, selectedMenuItems, quantities, date, "Placed");
        orders.add(order);
        System.out.println("Order placed successfully! Order ID: " + order.getOrderID());
    }

    private static int getLastOrderID() {
        int lastOrderID = 0;
        for (Order order : orders) {
            if (order.getOrderID() > lastOrderID) {
                lastOrderID = order.getOrderID();
            }
        }
        return lastOrderID;
    }

    private static void cancelOrder() {
        System.out.print("Enter the order ID to cancel: ");
        int orderID = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        Order orderToCancel = null;
        for (Order order : orders) {
            if (order.getOrderID() == orderID) {
                orderToCancel = order;
                break;
            }
        }

        if (orderToCancel != null) {
            orderToCancel.setStatus("Cancelled");
            System.out.println("Order with ID " + orderID + " has been cancelled successfully!");
        } else {
            System.out.println("Order with ID " + orderID + " does not exist!");
        }
    }

    private static void trackCollections() {
        System.out.print("Enter the date to track collections (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format!");
            return;
        }

        double totalCollection = 0;
        for (Order order : orders) {
            if (order.getDate().equals(date) && order.getStatus().equals("Placed")) {
                totalCollection += order.getTotalBillAmount();
            }
        }

        CollectionReport collectionReport = new CollectionReport(date, totalCollection);
        collectionReports.add(collectionReport);
        System.out.println("Total collection for " + dateStr + ": $" + totalCollection);
    }
}