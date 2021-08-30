package User.Controller;

import User.DTO.LogInReq;
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
    }
}
