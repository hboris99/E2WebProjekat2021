package User.Model;

import Order.Model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Deliverer extends User {

    private List<String> orderList;

    public Deliverer(String username, String password, String name, String surname,
                     GenderType genderType, Date dateOfBirth) {
        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Deliverer);
        orderList = new ArrayList<String>();
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public void addOrder(String id) {
        if(!orderList.contains(id)){
            orderList.add(id);
        }
    }
}
