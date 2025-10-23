// Grocery Delivery Application
// Developed by: Abijin Suvedha A

import java.io.*;
import java.util.*;

class Product {
    int id;
    String name;
    double price;
    String category;

    Product(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String toString() {
        return id + ". " + name + " - ₹" + price + " (" + category + ")";
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getTotal() {
        return product.price * quantity;
    }

    public String toString() {
        return product.name + " x " + quantity + " = ₹" + getTotal();
    }
}

public class GroceryApp {
    static Scanner sc = new Scanner(System.in);
    static List<Product> products = new ArrayList<>();
    static List<CartItem> cart = new ArrayList<>();
    static final String ORDER_FILE = "orders.txt";

    public static void main(String[] args) {
        loadProducts();
        int choice;
        do {
            showDashboard();
            System.out.println("Grocery Delivery Application");
            System.out.println("1. View All Products");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. View Dashboard Summary");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1 -> viewProducts();
                case 2 -> addToCart();
                case 3 -> viewCart();
                case 4 -> checkout();
                case 5 -> showDashboardSummary();
                case 6 -> System.out.println("Thank you for using Grocery Delivery App!");
                default -> System.out.println("Invalid choice. Try again!");
            }
        } while (choice != 6);
    }

    static void loadProducts() {
        products.add(new Product(1, "Rice 5kg Bag", 450.0, "Grains"));
        products.add(new Product(2, "Wheat Flour 1kg", 65.0, "Grains"));
        products.add(new Product(3, "Sugar 1kg", 50.0, "Essentials"));
        products.add(new Product(4, "Milk 1L", 60.0, "Dairy"));
        products.add(new Product(5, "Bread", 40.0, "Bakery"));
        products.add(new Product(6, "Butter 500g", 250.0, "Dairy"));
        products.add(new Product(7, "Apples 1kg", 180.0, "Fruits"));
        products.add(new Product(8, "Bananas (Dozen)", 90.0, "Fruits"));
    }

    static void viewProducts() {
        System.out.println("\nAvailable Products:");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    static void addToCart() {
        System.out.print("\nEnter Product ID to add to cart: ");
        int id = getIntInput();
        Product selected = null;

        for (Product p : products) {
            if (p.id == id) {
                selected = p;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = getIntInput();
        cart.add(new CartItem(selected, qty));
        System.out.println("Added to cart: " + selected.name + " x " + qty);
    }

    static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        System.out.println("\nYour Cart:");
        double total = 0;
        for (CartItem item : cart) {
            System.out.println(item);
            total += item.getTotal();
        }
        System.out.println("");
        System.out.println("Total Amount: ₹" + total);
    }

    static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Please add items before checkout.");
            return;
        }

        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        double total = 0;
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("\n\n");
        orderDetails.append("Customer: ").append(name).append("\n");
        orderDetails.append("Address: ").append(address).append("\n");
        orderDetails.append("Ordered Items:\n");

        for (CartItem item : cart) {
            orderDetails.append(item).append("\n");
            total += item.getTotal();
        }
        orderDetails.append("Total Amount: ₹").append(total).append("\n");
        orderDetails.append("Order Time: ").append(new Date()).append("\n");
        orderDetails.append("\n");

        saveOrder(orderDetails.toString());
        System.out.println("\nOrder placed successfully!");
        System.out.println("Your total bill is ₹" + total);
        System.out.println("Order details saved to: " + ORDER_FILE);

        cart.clear();
    }

    static void saveOrder(String orderText) {
        try (FileWriter fw = new FileWriter(ORDER_FILE, true)) {
            fw.write(orderText);
        } catch (IOException e) {
            System.out.println("Error saving order: " + e.getMessage());
        }
    }

    static void showDashboard() {
        System.out.println("\n");
        System.out.println("🛒 Grocery Delivery System Dashboard");
        System.out.println("");
        System.out.println("Total Products Available: " + products.size());
        System.out.println("Items Currently in Cart: " + cart.size());
        System.out.println("Total Orders Placed: " + getTotalOrders());
        System.out.println("Total Revenue Earned: ₹" + getTotalRevenue());
        System.out.println("\n");
    }

    static void showDashboardSummary() {
        System.out.println("\nDashboard Summary");
        System.out.println("Total Products: " + products.size());
        System.out.println("Items in Cart: " + cart.size());
        System.out.println("Total Orders Recorded: " + getTotalOrders());
        System.out.println("Total Revenue (From File): ₹" + getTotalRevenue());
        System.out.println("");
    }

    static int getTotalOrders() {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Customer:")) count++;
            }
        } catch (IOException e) {
            return 0;
        }
        return count;
    }

    static double getTotalRevenue() {
        double total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Total Amount:")) {
                    String[] parts = line.split("₹");
                    total += Double.parseDouble(parts[1].trim());
                }
            }
        } catch (IOException e) {
            return 0;
        }
        return total;
    }

    static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}