package User.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Deliverer extends User {

    private List<Order> orderList;

    public Deliverer(String username, String password, String name, String surname,
                     GenderType genderType, Date dateOfBirth, List<Order> orderList) {
        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Deliverer);
        orderList = new ArrayList<Order>();
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
