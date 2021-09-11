package User.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Buyer extends User {
    public  List<String> orderList;
    public Cart cart;
    public double points;
    public boolean sus;
    public int orderCancelCount;
    public BuyerType buyerType;


    public boolean getSus(){
        return sus;
    }
    public void setSus(boolean sus){
        this.sus = sus;
    }
    public int getOrderCancelCount(){
        return orderCancelCount;
    }
    public void setOrderCancelCount(int orderCancelCount){
        this.orderCancelCount = orderCancelCount;
    }
    public void increaseOrderCancelCount(){
        ++orderCancelCount;
        if(orderCancelCount > 5){
            this.sus = true;
        }
    }
    public Buyer(String username, String password, String name, String surname, GenderType genderType,
                 Date dateOfBirth, Cart cart, double points,
                 BuyerType buyerType) {

        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Buyer);
        orderList = new ArrayList<String>();
        this.cart = cart;
        this.points = points;
        this.buyerType = buyerType;
        this.sus = false;
        this.orderCancelCount = 0;
    }

    public Buyer(String username, String password, String name, String surname, GenderType genderType, Date dateOfBirth) {
        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Buyer);
        this.points = 0.0;
        this.buyerType = new BuyerType(BuyerTypeRank.Bronze,0,3000);
        this.cart = new Cart(username);
        orderList = new ArrayList<String>();

    }

    public void addOrder(String orderId) {
        if(!orderList.contains(orderId)) {
            orderList.add(orderId);
        }
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

    public void addPoints(double v) {
        points += v;
        if(points >= 4500 && points < 8000){
            buyerType.setBuyerTypeRank(BuyerTypeRank.Silver);
        }
        if(points >= 8000 && points < 15000){
            buyerType.setBuyerTypeRank(BuyerTypeRank.Gold);
        }if(points >= 15000){
            buyerType.setBuyerTypeRank(BuyerTypeRank.Platinum);
        }
    }
    public  void subPoints(double v){
        points -= v;
    }
}
