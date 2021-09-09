package User.Controller;

import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Article;
import Restaurant.Service.RestaurantService;
import User.DTO.ArticleRequest;
import User.DTO.MRestorauntResponse;
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

    public ManagerController(UserService userService, RestaurantService restaurantService){
        this.userService = userService;
        this.restaurantService = restaurantService;

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
        get("/manager/restaurant/getarticles", (req, res) -> {
            Optional<User> user = userService.validateJWT(req, UserRoleType.Manager);
            if(!user.isPresent()){
                return forbidden(res);
            }
            res.type("application/json");

            return gson.toJson(restaurantService.getAllArticles());
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
                Manager m = (Manager) u.get();
                return restaurantService.createArticle(articleRequest, fileName,m) && userService.updateUser(m)
                        ?ok("Added", res)
                        : badRequest("Failed to add", res);

            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
    }
}
