package User.Controller;

import User.DTO.BuyerReq;
import User.DTO.LogInReq;
import User.Model.Buyer;
import User.Model.User;
import User.DTO.RegisterReq;

import User.Model.UserRoleType;
import User.Service.UserService;
import demoWeb.JSONWebTokenUtil;
import demoWeb.Test;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
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
                    return gson.toJson(new BuyerReq((Buyer) u.get()));
                }
                return gson.toJson(new RegisterReq(u.get()));
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
