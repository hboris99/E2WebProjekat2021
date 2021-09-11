package User.Controller;

import Comments.DTO.CommentReq;
import Comments.Service.CommentService;
import Order.Model.Order;
import Order.Service.OrderService;
import User.DTO.*;
import User.Model.Buyer;
import User.Model.CartItem;
import User.Model.User;

import User.Model.UserRoleType;
import User.Service.UserService;
import com.google.gson.reflect.TypeToken;
import demoWeb.JSONWebTokenUtil;
import demoWeb.Test;
import spark.Request;

import javax.lang.model.element.TypeElement;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.ECField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static demoWeb.Test.gson;
import static demoWeb.Responses.*;
import static spark.Spark.*;

public class UserController {

    private UserService userService;
    private OrderService orderService;
    private CommentService commentService;
    public UserController(UserService userService, OrderService orderService, CommentService commentService){
        this.userService = userService;
        this.orderService= orderService;
        this.commentService = commentService;
        get("/user/cancomment/:name", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String name = req.params(":name");
                if(name == null || name.isBlank()){
                    return badRequest("Lose", res);
                }
                return orderService.hasDeliveredOrder(u.get().getUsername(), name)
                        ? ok("Ok", res)
                        : badRequest("Bad", res);

            }catch (Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });

        get("/owner/:name/comment",(req, res) -> {
            try {
                Optional<User> u = userService.validateJWT(req, UserRoleType.Manager);
                if(!u.isPresent()) {
                    u = userService.validateJWT(req, UserRoleType.Admin);
                    if(!u.isPresent()) {
                        return forbidden(res);
                    }
                }

                String restName = req.params(":name");
                if(restName == null || restName.isBlank()) {
                    return badRequest("Bad request", res);
                }

                return gson.toJson(commentService.getOwnerComments(restName));
            } catch(Exception e) {
                return internal(res);
            }
        });
        post("/user/comment/:name", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
                if(!u.isPresent()){
                    return forbidden(res);
                }
                String name = req.params(":name");
                if(name == null || name.isBlank()){
                    return badRequest("Lose", res);
                }

                CommentReq cr = gson.fromJson(req.body(), CommentReq.class);

                return userService.addComment((Buyer) u.get(), cr, name)
                        ?ok("Posted", res)
                        :badRequest("Bad", res);

            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });



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
        get("/user/discount", (req,res) -> {
                try{
                    Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
                    if(!u.isPresent()){
                        return forbidden(res);
                    }
                    Buyer b = (Buyer) u.get();
                    switch(b.getBuyerType().getBuyerTypeRank()){
                        case Bronze:{
                            return 0.0;
                        }
                        case Silver:{
                            return 5.0;
                        }
                        case Gold:{
                            return 10.0;
                        } case Platinum:{
                            return 15.0;
                        }
                    }
                    return 0.0;
                }catch(Exception e){
                    e.printStackTrace();
                    return internal(res);

                }
        });
        get("/user/order", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req);

                if (!u.isPresent()) {
                    return forbidden(res);
                }

                String restaurantName = req.queryParams("rest");
                String priceFrom = req.queryParams("pricefrom");
                String priceTo = req.queryParams("priceto");
                String dateFrom = req.queryParams("datefrom");
                String dateTo = req.queryParams("dateto");

                List<Order> orders = userService.getUserOrders(u.get());

                if(priceFrom != null && !priceFrom.isBlank()) {
                    final double price = Double.parseDouble(priceFrom);
                    orders = orders.stream().filter(o -> o.getPrice() >= price).collect(Collectors.toList());
                }

                if(priceTo != null && !priceTo.isBlank()) {
                    final double price = Double.parseDouble(priceTo);
                    orders = orders.stream().filter(o -> o.getPrice() <= price).collect(Collectors.toList());
                }

                if(dateFrom != null && !dateFrom.isBlank()) {
                    final LocalDate date = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    orders = orders.stream().filter(o -> {
                        LocalDate oDate = o.getDate().toLocalDate();
                        if(oDate.compareTo(date) >= 0) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                }

                if(dateTo != null && !dateTo.isBlank()) {
                    final LocalDate date = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    orders = orders.stream().filter(o -> {
                        LocalDate oDate = o.getDate().toLocalDate();
                        if(oDate.compareTo(date) <= 0) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                }
                    return gson.toJson(orders);

            }catch (Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        delete("/user/order/:id", (req,res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
                if(!u.isPresent()){
                    return forbidden(res);
                }

                String id = req.params(":id");
                if(id == null || id.isBlank()){
                    return badRequest("Bad id", res);
                }
                return userService.cancelOrder((Buyer) u.get(), id)
                        ? ok("Canceled", res)
                        : badRequest("Cant cancel", res);
            }catch (Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        post("/user/order", (req, res) -> {
            try {
                Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                Buyer bueyr = (Buyer) u.get();
                return userService.createOrder(bueyr)
                        ? ok("Added", res)
                        : badRequest("Not added", res);
            } catch(Exception e) {
                return internal(res);
            }
        });
        delete("user/cart/delete/:aritclename", (req, res) -> {
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);

                if (!u.isPresent()) {
                    return forbidden(res);
                }
                String name = req.params(":aritclename  ");
                if (name == null || name.isBlank()) {
                    return badRequest("bad name", res);
                }
                return userService.removeFromCart((Buyer) u.get(), name)
                        ? ok("Deleted", res)
                        : badRequest("Cant delete", res);
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }

        });
        post("/user/cart/:name", (req, res)->{
            try{
                Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
                if(!u.isPresent()){
                    return forbidden(res);
                }

                String restaurantName = req.params(":name");
                if(restaurantName == null || restaurantName.isBlank()){
                    return badRequest("Badreques", res);
                }
                Type type = new TypeToken<List<CartRequest>>(){}.getType();
                List<CartRequest> products = gson.fromJson(req.body(), type);
                return  userService.addToCart((Buyer) u.get(), products, restaurantName)
                        ? ok("Added", res)
                        : badRequest("Failed", res);

            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
        get("/user/cart", (req, res) -> {
           try{
               Optional<User> u = userService.validateJWT(req, UserRoleType.Buyer);
               if(!u.isPresent()){
                   return forbidden(res);
               }

                Buyer b = (Buyer) u.get();
               return gson.toJson(b.getCart());


            }catch(Exception e){
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
        get("/user", (req, res) -> {
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
                if(u.get().getUserRoleType() == UserRoleType.Buyer) {
                    return gson.toJson(new BuyerResponse((Buyer) u.get()));
                }
                return gson.toJson(new UserResponse(u.get()));
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });
        post("/user/profile", (req, res) -> {
            try {
                Optional<User> u = userService.validateJWT(req);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String location = "image";
                long maxFileSize = 100000000;
                long maxRequestSize = 100000000;
                int fileSizeThreshold = 1024;
                MultipartConfigElement mce = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
                req.raw().setAttribute("org.eclipse.jetty.multipartConfig", mce);
                Collection<Part> parts = req.raw().getParts();
                String fname = req.raw().getPart("file").getSubmittedFileName();
                Path out = Paths.get(Test.UPLOAD_DIR + File.separator + fname);
                try (final InputStream in = req.raw().getPart("file").getInputStream()) {
                    if(!Files.exists(out))
                        Files.copy(in, out);
                }
                u.get().setProfileImage(fname);
                return userService.updateUser(u.get())
                        ? ok("Updated", res)
                        : badRequest("Not updated", res);
            } catch(Exception e) {
                return internal(res);
            }
        });
        post("/user/update", (req, res) -> {
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
                // Contains most fields
                RegisterReq updateRequest = gson.fromJson(req.body(), RegisterReq.class);
                return userService.updateUser(updateRequest) ?
                        ok("Updated", res)
                        : badRequest("Failed to update", res);
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });
    }
}
