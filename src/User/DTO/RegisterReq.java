package User.DTO;

import User.Model.GenderType;
import User.Model.User;

import java.util.Date;

public class RegisterReq {
    private String username;
    private String password;
    private String name;
    private String surname;
    private GenderType gender;
    private Date birthDate;

    public RegisterReq(String username, String password, String name, String surname, GenderType gender, Date birthDate) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
    }
    public  RegisterReq(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.gender = user.getGenderType();
        this.birthDate = user.getDateOfBirth();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {

        this.gender = gender;
    }

    public Date getBirthDate() {

        return birthDate;
    }

    public void setBirthDate(Date birthDate) {

        this.birthDate = birthDate;
    }
}

