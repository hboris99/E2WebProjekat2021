package User.Service;

import User.DTO.LogInReq;
import User.DTO.RegisterReq;
import User.Model.*;
import User.Repository.UserRepository;
import demoWeb.JSONWebTokenUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<String> loginUser(LogInReq req){
        Optional<User> user = userRepository.get(req.getUsername());
        if(!user.isPresent() || !user.get().getPassword().equals(req.getPassword())){
            return Optional.empty();
        }

        return Optional.of(JSONWebTokenUtil.generateJws(user.get()));

    }
    public boolean registerUser(RegisterReq req) throws ParseException {
        Optional<User> reg = userRepository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Buyer newBuyer = new Buyer(req.getUsername(),req.getPassword(),req.getName(), req.getSurname(),req.getGender(),req.getBirthDate());
        return userRepository.Create(newBuyer);
    }
}
