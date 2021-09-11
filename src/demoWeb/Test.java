package demoWeb;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.File;
import java.util.Date;
import java.util.concurrent.CompletionException;

import Comments.Repository.CommentRepository;
import Comments.Service.CommentService;
import Order.Repository.OrderRepository;
import Order.Service.OrderService;
import Restaurant.Controller.RestaurantController;
import Restaurant.Repository.RestaurantRepository;
import Restaurant.Service.RestaurantService;
import User.Controller.AdminController;
import User.Controller.DelivererController;
import User.Controller.ManagerController;
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
        //User u1 = new User("boki","boki","boki","boki", GenderType.MALE,dt, UserRoleType.Admin);
        new File(UPLOAD_DIR).mkdir();



        UserRepository userRepository = new UserRepository("users.json");
        RestaurantRepository restaurantRepository = new RestaurantRepository("restoraunts.json");
        OrderRepository orderRepository = new OrderRepository("orders.json");
        CommentRepository commentRepository = new CommentRepository("comments.json");
        OrderService orderService = new OrderService(orderRepository);
        RestaurantService restaurantService = new RestaurantService(restaurantRepository);
        CommentService commentService = new CommentService(commentRepository);
        UserService userService = new UserService(userRepository, restaurantService, orderService, commentService);

        //userRepository.LoadAdminUsers("adminUsers.json");
        DelivererController delivererController = new DelivererController(userService, orderService);
        RestaurantController restaurantController = new RestaurantController(restaurantService, commentService);
        ImageController imageController = new ImageController(UPLOAD_DIR);
        ManagerController managerController = new ManagerController(userService, restaurantService,orderService);
        AdminController adminController = new AdminController(userService, restaurantService);
        UserController userController = new UserController(userService,orderService,commentService);
       
    }

}
