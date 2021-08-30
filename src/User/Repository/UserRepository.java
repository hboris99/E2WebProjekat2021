package User.Repository;
import User.Model.User;
import com.google.gson.reflect.TypeToken;
import demoWeb.JSONRepository;

import java.lang.reflect.Type;
import java.util.List;

public class UserRepository extends JSONRepository<User,String>{
    public UserRepository(String file) {
        super(file,new TypeToken<List<User>>(){}.getType());
        List<User> users = getAll();

    }
}
