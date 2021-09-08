package Restaurant.Controller;

import Restaurant.Model.Restaurant;
import Restaurant.Service.RestaurantService;

public class RestaurantController {
    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

    }
}
