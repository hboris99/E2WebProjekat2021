package Restaurant.Controller;

import Comments.Service.CommentService;
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
    private CommentService commentService;

    public RestaurantController(RestaurantService restaurantService, CommentService commentService){
        this.restaurantService = restaurantService;
        this.commentService = commentService;


        get("/restaurant/:name/comment", (req, res) -> {
           res.type("application/json");
           String name = req.params(":name");
           if(name == null || name.isBlank()){
               return gson.toJson(Collections.emptyList());
           }
           return gson.toJson(commentService.getByRestaurantName(name));
        });

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
