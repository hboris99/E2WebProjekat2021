package demoWeb;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import User.Controller.AdminController;
import User.Controller.UserController;
import User.Model.GenderType;
import User.Model.User;
import User.Model.UserRoleType;
import User.Repository.UserRepository;
import User.Service.UserService;
import com.google.gson.Gson;


import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import spark.Session;
public class Test {

    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static String UPLOAD_DIR = "uploads";


    public static void main(String[] args) throws Exception {
        new File(UPLOAD_DIR).mkdir();
        port(8080);
        staticFiles.externalLocation(new File("./static").getCanonicalPath());
        Date dt = new Date(1999,8,23);
        User u1 = new User("boki","boki","boki","boki", GenderType.MALE,dt, UserRoleType.Admin);




        UserRepository userRepository = new UserRepository("users.json");

        //userRepository.LoadAdminUsers("adminUsers.json");

        UserService userService = new UserService(userRepository);
        AdminController adminController = new AdminController(userService);
        UserController userController = new UserController(userService);
       
    }

}
