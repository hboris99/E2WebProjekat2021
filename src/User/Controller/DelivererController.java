package User.Controller;

import Order.Service.OrderService;
import User.Model.Deliverer;
import User.Model.User;
import User.Model.UserRoleType;
import User.Service.UserService;

import java.util.Optional;

import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;
import static spark.route.HttpMethod.put;

public class DelivererController {
    private UserService userService;
    private OrderService orderService;

    public DelivererController(UserService userService, OrderService orderService){
        this.userService = userService;
        this.orderService = orderService;
        put("/deliverer/request", (req, res)-> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Deliverer);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String id = req.body();
                return userService.requestOrder((Deliverer) u.get(), id)
                        ? ok("Requested", res)
                        : badRequest("Couldnt req", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        put("/deliverer/deliver", (req, res)-> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Deliverer);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String id = req.body();
                return orderService.deliverOrder(id)
                        ? ok("Delivered", res)
                        : badRequest("Couldnt deliver", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
    }
}
