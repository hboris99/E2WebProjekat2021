package User.Model;

import Restaurant.Model.Restaurant;

import java.util.Date;

public class Manager extends User {
    private Restaurant restaurant;

    public Manager(String username, String password, String name,
                   String surname, GenderType genderType, Date dateOfBirth
                   ) {
        super(username, password, name, surname, genderType, dateOfBirth, UserRoleType.Manager);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
