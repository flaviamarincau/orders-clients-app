package model;
/**
 * The Product class represents a product in the system.
 * It holds information about the product such as its id, name, description, price, and stock quantity.
 * This class is used to create and manipulate Product objects.
 */
public class Product {
    private int product_id;
    private String name;
    private String description;
    private double price;
    private int stock_quantity;

    public Product() {}

    public Product(int product_id, String name, String description, double price, int stock_quantity) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
