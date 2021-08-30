package User.Controller;

import User.DTO.LogInReq;
import User.DTO.RegisterReq;
import User.Model.User;
import User.Model.UserRoleType;
import User.Service.UserService;
import demoWeb.JSONWebTokenUtil;
import spark.Request;
import java.util.Optional;
import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;
import User.Service.UserService;

public class AdminController {
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
        get("/admin/users", (req, res) ->{

                Optional<User> user = userService.validateJWT(req, UserRoleType.Admin);
                if(!user.isPresent()){
                    return forbidden(res);
                }
                res.type("application/json");
                for(User u : userService.getAllUsers()){
                    System.out.println(u.getUsername());
                    System.out.println(u.getUserRoleType());
                }
                return gson.toJson(userService.getAllUsers());
        }

        );

        post("/admin/newmanager", (req,res) ->
                {
                    try {
                        Optional<User> user = userService.validateJWT(req, UserRoleType.Admin);
                        if(!user.isPresent()){
                            return forbidden(res);
                        }
                        return userService.registerManager(gson.fromJson(req.body(), RegisterReq.class)) ?
                                ok("Registered manager", res) : badRequest("Failed to register manager", res);
                    }catch(Exception e){
                        e.printStackTrace();
                        return internal(res);                    }
                }
                );
        post("/admin/newdeliverer", (req, res) ->{
            try {
                Optional<User> user = userService.validateJWT(req, UserRoleType.Admin);
                if(!user.isPresent()){
                    return forbidden(res);
                }
                return userService.registerDeliverer(gson.fromJson(req.body(), RegisterReq.class)) ?
                        ok("Registered deliverer", res) : badRequest("Failed to register deliverer", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        }
                );
    }
}
