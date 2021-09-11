package Restaurant.DTO;

import Restaurant.Model.Adress;
import Restaurant.Model.Location;
import Restaurant.Model.RestaurantType;

public class RestaurantRequest {
    private String name;
    private RestaurantType restaurantType;
    private String managerUsername;
    private Location location;

    public RestaurantRequest(String name, RestaurantType restaurantType, String managerUsername, Location location) {
        this.name = name;
        this.restaurantType = restaurantType;
        this.managerUsername = managerUsername;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
