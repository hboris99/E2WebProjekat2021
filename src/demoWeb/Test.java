package demoWeb;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.File;
import java.util.Date;

import Restaurant.Repository.RestaurantRepository;
import User.Controller.AdminController;
import User.Controller.UserController;
import User.Model.*;
import User.Repository.UserRepository;
import User.Service.UserService;
import com.google.gson.Gson;


import com.google.gson.GsonBuilder;

public class Test {
    public static Gson gson;
    public static String UPLOAD_DIR = "uploads";


    public static void main(String[] args) throws Exception {
        new File(UPLOAD_DIR).mkdir();
        RuntimeTypeAdapterFactory<User> userAdapterFactory = RuntimeTypeAdapterFactory.of(User.class)
                .registerSubtype(Buyer.class)
                .registerSubtype(Admin.class)
                .registerSubtype(Manager.class)
                .registerSubtype(Deliverer.class);
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(userAdapterFactory).setDateFormat("yyyy-MM-dd").create();
        port(8080);
        staticFiles.externalLocation(new File("./static").getCanonicalPath());
        Date dt = new Date(1999,8,23);
        User u1 = new User("boki","boki","boki","boki", GenderType.MALE,dt, UserRoleType.Admin);




        UserRepository userRepository = new UserRepository("users.json");
        RestaurantRepository restaurantRepository = new RestaurantRepository("restoraunts.json");
        //userRepository.LoadAdminUsers("adminUsers.json");

        UserService userService = new UserService(userRepository);
        AdminController adminController = new AdminController(userService);
        UserController userController = new UserController(userService);
       
    }

}
