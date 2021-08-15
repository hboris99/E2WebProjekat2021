package Restaurant.Model;

import User.Model.Buyer;

import java.util.Date;
import java.util.List;

public class Order {
    private double id;
    private List<Article> articleList;
    private Restaurant restaurant;
    private Date dateTimeOfOrder;
    private double price;
    private Buyer buyer;
    private OrderStatus orderStatus;

    public Order(double id, List<Article> articleList, Restaurant restaurant, Date dateTimeOfOrder, double price, Buyer buyer, OrderStatus orderStatus) {
        this.id = id;
        this.articleList = articleList;
        this.restaurant = restaurant;
        this.dateTimeOfOrder = dateTimeOfOrder;
        this.price = price;
        this.buyer = buyer;
        this.orderStatus = orderStatus;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Date getDateTimeOfOrder() {
        return dateTimeOfOrder;
    }

    public void setDateTimeOfOrder(Date dateTimeOfOrder) {
        this.dateTimeOfOrder = dateTimeOfOrder;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
