package User.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Buyer extends User {
    private List<String> orderList;
    private Cart cart;
    private double points;
    private BuyerType buyerType;


    public Buyer(String username, String password, String name, String surname, GenderType genderType,
                 Date dateOfBirth, Cart cart, double points,
                 BuyerType buyerType) {

        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Buyer);
        orderList = new ArrayList<String>();
        this.cart = cart;
        this.points = points;
        this.buyerType = buyerType;
    }

    public Buyer(String username, String password, String name, String surname, GenderType genderType, Date dateOfBirth) {
        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Buyer);
        this.points = 0.0;
        this.buyerType = new BuyerType(BuyerTypeRank.Bronze,0,3000);
        this.cart = new Cart();
        orderList = new ArrayList<String>();

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

    public BuyerType getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(BuyerType buyerType) {
        this.buyerType = buyerType;
    }
}
