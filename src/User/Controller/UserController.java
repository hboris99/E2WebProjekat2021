package User.Controller;

import User.DTO.LogInReq;
import User.Model.User;
import User.Service.UserService;
import demoWeb.JSONWebTokenUtil;
import spark.Request;
import java.util.Optional;
import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;

public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;


        get("/user/role", (req, res) -> {
            try {
                Optional<String> jws = JSONWebTokenUtil.parseJws(req);
                if(!jws.isPresent()) {
                    return badRequest("Missing jws", res);
                }
                String username = JSONWebTokenUtil.getUsername(jws.get());
                Optional<User> u = userService.getByUsername(username);
                if(!u.isPresent()) {
                    return notFound(res);
                }
                res.type("application/json");
                return u.get().getUserRoleType();
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });
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
    }
}
