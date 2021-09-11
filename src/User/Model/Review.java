package User.Model;

import Restaurant.Model.Restaurant;

public class Review {
    private Buyer buyer;
    private Restaurant restaurant;
    private String body;
    private double mark;

    public Review(Buyer buyer, Restaurant restaurant, String body, double mark) {
        this.buyer = buyer;
        this.restaurant = restaurant;
        this.body = body;
        this.mark = mark;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
