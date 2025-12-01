package sayana.models;

public class Category {
    private int categoryId;
    private String categoryName;
    private String description;

    public Category(int categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    // Геттеры и сеттеры
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getDescription() { return description; }
}