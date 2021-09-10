package Order.Model;

import Restaurant.Model.Article;
import Restaurant.Model.Restaurant;
import demoWeb.IRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order implements IRepository<String> {
    private String id;
    private List<Article> articles;
    private Restaurant restaurant;
    private LocalDateTime date;
    private double price;
    private String username;
    private String firstname;
    private String surname;
    private OrderStatusType stats;
    private boolean deleted;
    private String delivererUsername;

    public Order(String id, Restaurant restaurant, LocalDateTime date, double price, String username, String firstname, String surname, OrderStatusType stats ) {
        this.id = id;
        articles = new ArrayList<Article>();
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.stats = stats;
        this.deleted = false;
    }
    public Order( Restaurant restaurant, LocalDateTime date, double price, String username, String firstname, String surname, OrderStatusType stats ) {
        articles = new ArrayList<Article>();
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.stats = stats;
        this.deleted = false;
    }
    public Order( Restaurant restaurant, LocalDateTime date, double price, String username, String firstname, String surname) {
        articles = new ArrayList<Article>();
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.stats = OrderStatusType.Pending;
        this.deleted = false;
    }
    public void addArticle(Article a) {
        Optional<Article> article = articles.stream().filter(ar -> ar.getName().equals(a.getName())).findFirst();
        if(!article.isPresent()) {
            articles.add(a);
        }
    }
    public boolean removeArticle(String articleName) {
        return articles.removeIf(a -> a.getName().equals(articleName));
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public OrderStatusType getStats() {
        return stats;
    }

    public void setStats(OrderStatusType stats) {
        this.stats = stats;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDelivererUsername() {
        return delivererUsername;
    }

    public void setDelivererUsername(String delivererUsername) {
        this.delivererUsername = delivererUsername;
    }
}
