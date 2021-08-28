package User.Service;

import User.DTO.LogInReq;
import User.Model.User;
import User.Model.UserRoleType;
import User.Repository.UserRepository;
import demoWeb.JSONWebTokenUtil;
import spark.Request;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public Optional<User> validateJWT(Request r, UserRoleType role){
        Optional<String> jwt = JSONWebTokenUtil.parseJws(r);
        if(!jwt.isPresent()){
            return Optional.empty();
        }
        String username = JSONWebTokenUtil.getUsername(jwt.get());
        Optional<User> user = userRepository.get(username);
        if(!user.isPresent() || !user.get().getUserRoleType().equals(role)){
            return Optional.empty();
        }
        return user;
    }

    public Optional<String> loginUser(LogInReq req){
        Optional<User> user = userRepository.get(req.getUsername());
        if(!user.isPresent() || !user.get().getPassword().equals(req.getPassword())){
            return Optional.empty();
        }

        return Optional.of(JSONWebTokenUtil.generateJws(user.get()));

    }
    public List<User> getAllUsers(){
        return userRepository.getAll().stream().filter(u -> !u.isDeleted()).collect(Collectors.toList());
    }
    public Optional<User> getByUsername(String username) {
        System.out.println(userRepository.get(username));
        return userRepository.get(username);
    }
}
