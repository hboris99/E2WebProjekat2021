package User.Model;
import Restaurant.Model.Article;

public class CartItem {

    private double price;
    private double quantity;
    private Article article;

    public CartItem( Article article, double quantity) {
        this.price = article.getPrice() * quantity;
        this.quantity = quantity;
        this.article = article;
    }

    public double getPrice() {
        return article.getPrice() * quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
