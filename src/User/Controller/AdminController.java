package User.Controller;

import Order.Service.OrderService;
import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Restaurant;
import Restaurant.Service.RestaurantService;
import User.DTO.LogInReq;
import User.DTO.RegisterReq;
import User.Model.User;
import User.Model.UserRoleType;
import User.Service.UserService;
import demoWeb.JSONWebTokenUtil;
import demoWeb.Test;
import spark.Request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;
import User.Service.UserService;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

public class AdminController {
    private UserService userService;
    private RestaurantService restaurantService;

    public AdminController(UserService userService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantService = restaurantService;

        delete("/admin/comment/:id", (req, res) -> {
            try {
                Optional<User> u = userService.validateJWT(req, UserRoleType.Admin);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String idstr = req.params(":id");
                if(idstr == null || idstr.isBlank()) {
                    return badRequest("Not deleted", res);
                }

                Integer id = Integer.parseInt(idstr);

                return userService.deleteComment(id)
                        ? ok("Deleted", res)
                        : badRequest("Not deleted", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/admin/users", (req, res) ->{

                Optional<User> user = userService.validateJWT(req, UserRoleType.Admin);
                if(!user.isPresent()){
                    return forbidden(res);
                }
                res.type("application/json");
                for(User u : userService.getAllUsers()){
                    System.out.println(u.getUsername());
                    System.out.println(u.getClass().getName());
                    System.out.println(u.getUserRoleType());
                }
                return gson.toJson(userService.getAllUsers());
        }

        );
        post("/admin/restaurant", (req, res) -> {
           try{
               Optional<User> u = userService.validateJWT(req, UserRoleType.Admin);
               if(!u.isPresent()){
                   return forbidden(res);
               }
               String location = "image";
               long maxFileSize = 100000000;
               long maxReqSize = 1000000000;
               int filesizeThreshold = 1024;
               MultipartConfigElement mce = new MultipartConfigElement(location, maxFileSize, maxReqSize, filesizeThreshold);
               req.raw().setAttribute("org.eclipse.jetty.multipartConfig", mce);
               Collection<Part> parts  = req.raw().getParts();
               String fileName = req.raw().getPart("file").getSubmittedFileName();
               Path out = Paths.get(Test.UPLOAD_DIR  + File.separator + fileName);
               try(final InputStream in = req.raw().getPart("file").getInputStream()){
                   if(!Files.exists(out))
                       Files.copy(in, out);
               }
               RestaurantRequest restaurantRequest = gson.fromJson(req.raw().getParameter("req"), RestaurantRequest.class);
               Optional<Restaurant> r = restaurantService.createRestaurant(restaurantRequest, fileName);

               if(!r.isPresent()){
                   return badRequest("Bad reques", res);
               }
               return userService.addRestaurantToManager(restaurantRequest.getManagerUsername(), r.get())
                       ? ok("Added", res)
                       : badRequest("Failed to add", res);
           }catch(Exception e){
               e.printStackTrace();
               return internal(res);
           }
        });
        post("/admin/user/block/:username", (req, res) -> {
           try{
               Optional<User> u = userService.validateJWT(req, UserRoleType.Admin);
               if(!u.isPresent()){
                   return forbidden(res);
               }
               String username = req.params(":username");
               if(username == null || username.isBlank()){
                   return badRequest("Invalid username", res);
               }

               return userService.blockUser(username)
                       ?ok("User blocked", res)
                       : badRequest("Failed to block user", res);

           }catch(Exception e){
               e.printStackTrace();
               return internal(res);
           }
        });
        post("/admin/user/unblock/:username", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Admin);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String username = req.params(":username");
                if(username == null || username.isBlank()){
                    return badRequest("Invalid username", res);
                }

                return userService.unblockUser(username)
                        ?ok("User blocked", res)
                        : badRequest("Failed to block user", res);

            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        delete("/admin/user/:username", (req, res) ->
        {
            try{
                    Optional<User> u = userService.validateJWT(req, UserRoleType.Admin);
                    if(!u.isPresent()){
                        return forbidden(res);
                    }
                    String username = req.params(":username");
                    if(username == null || username.isBlank()){
                        return badRequest("Invalid username", res);
                    }
                    return userService.deleteUser(username)
                            ? ok("User Deleted", res)
                            : badRequest("User was not deleted", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
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
        get("/admin/getmanagers", (req, res) -> {
           try{
               Optional<User> user = userService.validateJWT(req, UserRoleType.Admin);
               if(!user.isPresent()){
                   return forbidden(res);
               }
               return  gson.toJson(userService.getRestaurantlessManagers());
           }catch(Exception e){
               e.printStackTrace();
               return internal(res);
           }
        });
    }
}
