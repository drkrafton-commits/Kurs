package sayana.models;

public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private String description;
    private double price;
    private String composition;
    private boolean availabilityStatus;

    public Product(int productId, int categoryId, String productName, String description,
                   double price, String composition, boolean availabilityStatus) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.composition = composition;
        this.availabilityStatus = availabilityStatus;
    }

    // Геттеры и сеттеры
    public int getProductId() { return productId; }
    public int getCategoryId() { return categoryId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getComposition() { return composition; }
    public boolean isAvailabilityStatus() { return availabilityStatus; }
}