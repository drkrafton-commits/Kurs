package sayana.models;

public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private String description;
    private double price;
    private String composition;
    private boolean availabilityStatus;
    private String imageUrl; // Добавляем поле для URL изображения

    public Product(int productId, int categoryId, String productName, String description,
                   double price, String composition, boolean availabilityStatus) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.composition = composition;
        this.availabilityStatus = availabilityStatus;
        this.imageUrl = null; // По умолчанию null
    }

    // Полный конструктор с изображением (для удобства)
    public Product(int productId, int categoryId, String productName, String description,
                   double price, String composition, boolean availabilityStatus, String imageUrl) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.composition = composition;
        this.availabilityStatus = availabilityStatus;
        this.imageUrl = imageUrl;
    }

    // Геттеры и сеттеры
    public int getProductId() { return productId; }
    public int getCategoryId() { return categoryId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getComposition() { return composition; }
    public boolean isAvailabilityStatus() { return availabilityStatus; }
    public String getImageUrl() { return imageUrl; }

    public void setProductId(int productId) { this.productId = productId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setComposition(String composition) { this.composition = composition; }
    public void setAvailabilityStatus(boolean availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}