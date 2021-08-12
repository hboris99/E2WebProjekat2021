package User.Model;

import java.util.Date;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private GenderType genderType;
    private UserRoleType userRoleType;
    private Date dateOfBirth;

    public User(String username, String password, String name, String surname, GenderType genderType, Date dateOfBirth, UserRoleType userRoleType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.genderType = genderType;
        this.dateOfBirth = dateOfBirth;
        this.userRoleType = userRoleType;
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

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public UserRoleType getUserRoleType() {
        return userRoleType;
    }

    public void setUserRoleType(UserRoleType userRoleType) {
        this.userRoleType = userRoleType;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
