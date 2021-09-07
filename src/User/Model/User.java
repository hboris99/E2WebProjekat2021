package User.Model;

import demoWeb.IRepository;

import java.io.Serializable;
import java.util.Date;

public class User implements IRepository<String>, Serializable {
    public String username;
    public String password;
    public String name;
    public String surname;
    public GenderType genderType;
    public UserRoleType userRoleType;
    public Date dateOfBirth;
    public String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    private Boolean isBlocked;
    private Boolean deleted;

    public User(String username, String password, String name, String surname, GenderType genderType, Date dateOfBirth, UserRoleType userRoleType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.genderType = genderType;
        this.dateOfBirth = dateOfBirth;
        this.userRoleType = userRoleType;
        this.profileImage = "";
        this.isBlocked = false;
        this.deleted = false;
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

    @Override
    public String getID() {
        return username;
    }

    @Override
    public void setID(String id) {
        this.username = id;
    }

    @Override
    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
    }


}
