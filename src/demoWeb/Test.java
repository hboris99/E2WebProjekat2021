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


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import spark.Session;
public class Test {

    public static Gson gson = new Gson();


    public static void main(String[] args) throws Exception {
        port(8080);
        staticFiles.externalLocation(new File("./static").getCanonicalPath());
        List<User> users = new ArrayList<User>();
        Date dt = new Date(1999,8,23);
        User u1 = new User("boki","boki","boki","boki", GenderType.MALE,dt, UserRoleType.Admin);
        User u2= new User("zoki","boki","boki","boki", GenderType.MALE,dt, UserRoleType.Manager );
        User u3 = new User("marina","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u4 = new User("s","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u5 = new User("g","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u6 = new User("gas","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u7 = new User("das","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u8 = new User("dgafas","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u9 = new User("gdasfda","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );
        User u10 = new User("hasfafdas","boki","boki","boki", GenderType.FEMALE,dt, UserRoleType.Manager );



        UserRepository userRepository = new UserRepository("users.json");
        userRepository.Create(u1);
        userRepository.Create(u2);
        userRepository.Create(u3);
        userRepository.Create(u4);
        userRepository.Create(u5);
        userRepository.Create(u6);
        userRepository.Create(u7);
        userRepository.Create(u8);
        userRepository.Create(u9);
        userRepository.Create(u10);


        UserService userService = new UserService(userRepository);
        AdminController adminController = new AdminController(userService);
        UserController userController = new UserController(userService);

    }

}
