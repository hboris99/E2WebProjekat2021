package Restaurant.Repository;

import Restaurant.Model.Restaurant;
import User.Model.User;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import demoWeb.JSONRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class RestaurantRepository extends JSONRepository<Restaurant, Integer> {
    public RestaurantRepository(String file) {
        super(file,new TypeToken<List<Restaurant>>(){}.getType());    }

    public Integer getNextID() throws JsonSyntaxException, IOException{
        return getAll().size()+1;
    }
}
