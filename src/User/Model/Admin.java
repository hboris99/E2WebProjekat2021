package User.Model;

import java.util.Date;

public class Admin extends User {
    public Admin(String username, String password, String name, String surname, GenderType genderType, Date dateOfBirth, UserRoleType userRoleType) {
        super(username, password, name, surname, genderType, dateOfBirth, userRoleType);
    }
    }
