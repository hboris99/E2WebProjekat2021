package User.Model;

import Restaurant.Model.Restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Manager extends User {
    public Restaurant restaurant;
    private List<DeliveryRequest> requests;

    public Manager(String username, String password, String firstName, String lastName, GenderType sex, Date birthDate) {
        super(username, password, firstName, lastName, sex,birthDate,UserRoleType.Manager);
        isBlocked = false;
        requests = new ArrayList<DeliveryRequest>();

    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
