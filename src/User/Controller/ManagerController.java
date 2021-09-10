package User.Controller;

import Order.Service.OrderService;
import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Article;
import Restaurant.Service.RestaurantService;
import User.DTO.ArticleRequest;
import User.DTO.MRestorauntResponse;
import User.Model.DeliveryRequest;
import User.Model.Manager;
import User.Model.User;
import User.Model.UserRoleType;
import User.Service.UserService;
import demoWeb.Test;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static spark.Spark.*;
import static demoWeb.Responses.*;
import static demoWeb.Test.gson;

public class ManagerController {
    private UserService userService;
    private RestaurantService restaurantService;
    private OrderService orderService;

    public ManagerController(UserService userService, RestaurantService restaurantService, OrderService orderService){
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        get("/manager/requests", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                Manager m = (Manager) u.get();
                return gson.toJson(m.getRequests());
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        post("/manager/requests", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                DeliveryRequest dr = gson.fromJson(req.body(), DeliveryRequest.class);
                return userService.approveOrder((Manager) u.get(), dr)
                        ? ok("Approver", res)
                        : badRequest("Nope", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        put("/manager/requests", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                DeliveryRequest dr = gson.fromJson(req.body(), DeliveryRequest.class);
                return userService.declineRequest((Manager) u.get(), dr);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        put("/manager/order/prepare", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String id = req.body();
                return orderService.toPrepare(id)
                        ?ok("Preparing", res)
                        : badRequest("Cant change", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        put("/manager/order/wait", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String id = req.body();
                return orderService.toWaiting(id)
                        ?ok("Waiting", res)
                        : badRequest("Cant change", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        get("/manager/restaurant", (req, res)->{
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                Manager m = (Manager) u.get();
                res.type("application/json");
                if(m.getRestaurant() == null){
                    return notFound(res);
                }
                System.out.println(m.getRestaurant().getArticleList());
                return gson.toJson(new MRestorauntResponse(m.getRestaurant()));
            } catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        /*get("/manager/restaurant/getarticles", (req, res) -> {
            Optional<User> user = userService.validateJWT(req, UserRoleType.Manager);
            if(!user.isPresent()){
                return forbidden(res);
            }
            res.type("application/json");

            return gson.toJson(restaurantService.getAllArticles());
        });*/
        get("/manager/restaurant/getarticle/:articleName", (req, res) -> {
            try {
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if (!u.isPresent()) {
                    return forbidden(res);
                }
                Manager m = (Manager) u.get();
                String articleName = req.params(":articleName");
                return gson.toJson(restaurantService.getArticle(articleName, m));
            }catch(Exception e){
                e.printStackTrace();
                return badRequest("Failed to get", res);
            }
        });
        post("/manager/restaurant/article", (req, res)->{
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
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
                ArticleRequest articleRequest = gson.fromJson(req.raw().getParameter("req"), ArticleRequest.class);
                System.out.println(articleRequest.getName());
                Manager m = (Manager) u.get();
                return restaurantService.createArticle(m.getRestaurant().getID(), articleRequest, fileName,m) && userService.updateUser(m)
                        ?ok("Added", res)
                        : badRequest("Failed to add", res);

            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        post("/manager/restaurant/updatearticle", (req, res)->{
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
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
                ArticleRequest articleRequest = gson.fromJson(req.raw().getParameter("req"), ArticleRequest.class);
                Manager m = (Manager) u.get();
                return restaurantService.update(articleRequest, fileName,m) && userService.updateUser(m)
                        ?ok("Added", res)
                        : badRequest("Failed to add", res);

            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        delete("/manager/restaurant/deletearticle/:articleName", (req,res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String articleName = req.params(":articleName");
                if(articleName == null || articleName.isBlank()){
                    return badRequest("Invalid article name", res);
                }
                Manager m = (Manager) u.get();
                return restaurantService.deleteArticle(articleName, m) && userService.updateUser(m)
                        ? ok("Article deleted", res)
                        : badRequest("Failed to delete", res);

            }catch (Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
    }
}
