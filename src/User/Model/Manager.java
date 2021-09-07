package User.Model;

import Restaurant.Model.Restaurant;

import java.util.Date;
import java.util.Optional;

public class Manager extends User {
    public Restaurant restaurant;

    public Manager(User u) {
        super(u.username, u.password, u.name, u.surname, u.genderType, u.dateOfBirth, UserRoleType.Manager);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
