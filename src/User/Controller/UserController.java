package User.Controller;

import User.DTO.LogInReq;
import User.DTO.RegisterReq;
import User.Service.UserService;
import spark.Request;
import java.util.Optional;
import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;

public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;



        post("/user/login", (req, res)->{
            try{
                Optional<String> jws = userService.loginUser(gson.fromJson(req.body(), LogInReq.class));
                if(!jws.isPresent()){
                    return badRequest("Bad login", res);
                }
                res.type("application/json");
                return jws.get();
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });

        post("/user/register", (req, res) -> {
            try {
                if(userService.registerUser(gson.fromJson(req.body(), RegisterReq.class))) {
                    return ok("Registered", res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
            return badRequest("Failed to register", res);
        });

    }
}
