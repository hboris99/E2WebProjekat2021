package User.DTO;

import User.Model.User;

import java.text.SimpleDateFormat;

public class UserResponse {
    protected String username;
    protected String password;
    protected String name;
    protected String surname;
    protected String gender;
    protected String birthDate;
    protected String profileImage;

    public UserResponse(String username, String password, String name, String surname, String gender, String birthDate, String profileImage) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImage = profileImage;
    }
    public UserResponse(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.gender = user.getGenderType().name();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.birthDate = format.format(user.getDateOfBirth());
        this.profileImage = user.getProfileImage();
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
