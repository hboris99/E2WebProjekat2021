package Restaurant.Controller;

import Restaurant.Model.Restaurant;
import Restaurant.Service.RestaurantService;
import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class RestaurantController {
    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;

     get("/getRestaurants", (req, res) ->{

         res.type("application/json");
         return gson.toJson(restaurantService.getAllRestaurants());

     }
     );
     get("/restaurant/:name/article", (req, res) -> {
         res.type("application/json");
         String name = req.params(":name");
         if(name == null || name.isBlank()) {
             return gson.toJson(Collections.emptyList());
         }
         return gson.toJson(restaurantService.getArticlesByRestaurantName(name));
     });
        get("/restaurant/:name", (req, res) -> {
            res.type("application/json");
            Optional<Restaurant> r = restaurantService.getByName(req.params(":name"));
            if(!r.isPresent()) {
                return notFound(res);
            }
            return gson.toJson(r.get());
        });
    }
}
