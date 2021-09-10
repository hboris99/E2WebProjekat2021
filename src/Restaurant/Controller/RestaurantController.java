package Restaurant.Controller;

import Restaurant.Model.Restaurant;
import Restaurant.Service.RestaurantService;
import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;

import java.util.Optional;

import static spark.route.HttpMethod.get;

public class RestaurantController {
    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;

     get("/getRestaurants", (req, res) ->{
         res.type("application/json");
         return gson.toJson(restaurantService.getAllRestaurants());

     }
     );
    }
}
