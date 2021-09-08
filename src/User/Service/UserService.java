package User.Service;

import User.DTO.LogInReq;
import User.Model.User;
import User.Model.UserRoleType;
import User.DTO.RegisterReq;
import User.Model.*;
import User.Repository.UserRepository;
import demoWeb.JSONWebTokenUtil;
import spark.Request;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    public Optional<User> validateJWT(Request req) {
        Optional<String> jws = JSONWebTokenUtil.parseJws(req);
        if(!jws.isPresent()) {
            return Optional.empty();
        }
        String username = JSONWebTokenUtil.getUsername(jws.get());
        Optional<User> u = getByUsername(username);
        return u;
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
    public boolean registerUser(RegisterReq req) throws ParseException {
        Optional<User> reg = userRepository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Buyer newBuyer = new Buyer(req.getUsername(),req.getPassword(),req.getName(), req.getSurname(),req.getGender(),req.getBirthDate());
        return userRepository.Create(newBuyer);
    }

    public boolean registerDeliverer(RegisterReq req) {
        if(getByUsername(req.getUsername()).isPresent()){
            return false;
        }
        Deliverer deliverer = new Deliverer(req.getUsername(), req.getPassword(),req.getName(), req.getSurname(), req.getGender(), req.getBirthDate());
        return userRepository.Create(deliverer);
    }

    public boolean registerManager(RegisterReq req) {
        if(getByUsername(req.getUsername()).isPresent()){
            return false;
        }
        Manager manager = new Manager(req.getUsername(), req.getPassword(),req.getName(), req.getSurname(), req.getGender(), req.getBirthDate());
        return userRepository.Create(manager);
    }

    public boolean deleteUser(String username) {
        return userRepository.Delete(username);
    }

    public boolean updateUser(User user) {
        return userRepository.Update(user);
    }
    public boolean updateUser(RegisterReq req) {
        Optional<User> u = getByUsername(req.getUsername());
        if (!u.isPresent()) {
            return false;
        }
        User r = u.get();
        r.setDateOfBirth(req.getBirthDate());
        r.setGenderType(req.getGender());
        r.setName(req.getName());
        r.setSurname(req.getSurname());
        if(req.getPassword() != null && !req.getPassword().isBlank() && req.getPassword().length() >= 4)
            r.setPassword(req.getPassword());
        return userRepository.Update(r);
    }
}