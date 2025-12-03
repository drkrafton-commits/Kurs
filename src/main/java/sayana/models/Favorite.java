package sayana.models;

public class Favorite {
    private int favoriteId;
    private int userId;
    private int productId;

    public Favorite(int favoriteId, int userId, int productId) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.productId = productId;
    }

    // Геттеры и сеттеры
    public int getFavoriteId() { return favoriteId; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }

    public void setFavoriteId(int favoriteId) { this.favoriteId = favoriteId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setProductId(int productId) { this.productId = productId; }
}

