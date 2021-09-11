package User.DTO;

import User.Model.*;

import java.util.Date;
import java.util.List;

public class BuyerReq extends RegisterReq {
    public List<String> orderList;
    public Cart cart;
    public double points;
    public boolean sus;
    public int orderCancelCount;
    public BuyerType buyerType;

    public BuyerReq(String username, String password, String name, String surname, GenderType gender, Date birthDate, List<String> orderList, Cart cart, double points, boolean sus, int orderCancelCount, BuyerType buyerType) {
        super(username, password, name, surname, gender, birthDate);
        this.orderList = orderList;
        this.cart = cart;
        this.points = points;
        this.sus = sus;
        this.orderCancelCount = orderCancelCount;
        this.buyerType = buyerType;
    }

    public BuyerReq(Buyer buyer) {
        super(buyer);
        this.orderList = buyer.getOrderList();
        this.cart = buyer.getCart();
        this.points = buyer.getPoints();
        this.sus = buyer.getSus();
        this.orderCancelCount = buyer.getOrderCancelCount();
        this.buyerType = buyer.getBuyerType();
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public boolean isSus() {
        return sus;
    }

    public void setSus(boolean sus) {
        this.sus = sus;
    }

    public int getOrderCancelCount() {
        return orderCancelCount;
    }

    public void setOrderCancelCount(int orderCancelCount) {
        this.orderCancelCount = orderCancelCount;
    }

    public BuyerType getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(BuyerType buyerType) {
        this.buyerType = buyerType;
    }
}
