package User.DTO;

import Restaurant.Model.ArticleType;
import Restaurant.Model.Restaurant;

public class ArticleRequest {
    public Integer restaurantID;
    public String name;
    public double price;
    public ArticleType articleType;
    public double quantity;
    public String description;

    public ArticleRequest(Integer restaurant, String name, double price, ArticleType articleType, double quantity, String description) {
        this.restaurantID = restaurant;
        this.name = name;
        this.price = price;
        this.articleType = articleType;
        this.quantity = quantity;
        this.description = description;
    }

    public Integer getRestaurant() {
        return restaurantID;
    }

    public void setRestaurant(Integer restaurant) {
        this.restaurantID = restaurant;
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
}
