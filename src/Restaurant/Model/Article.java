package Restaurant.Model;

public class Article {
    private String name;
    private double price;
    private ArticleType articleType; //moze se i korisiti bool jer imamo samo pice hrana
    private Integer restaurant;
    private double quantity;
    private String description;
    private String logo;
    private boolean deleted;

    public Article(String name, double price, ArticleType articleType, double quantity, String description, String logo, Integer restaurant) {
        this.name = name;
        this.price = price;
        this.articleType = articleType;
        this.restaurant = restaurant;
        this.quantity = quantity;
        this.description = description;
        this.logo = logo;
        this.deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public ArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }

    public Integer getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Integer restaurant) {
        this.restaurant = restaurant;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
