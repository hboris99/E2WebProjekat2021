package Order.Repository;

import Order.Model.Order;
import com.google.gson.reflect.TypeToken;
import demoWeb.JSONRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class OrderRepository extends JSONRepository<Order, String> {

    public OrderRepository(String file) {
        super(file, new TypeToken<List<Order>>(){}.getType());
    }
    public String getNextID(){
        int len = 20;
        String key = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        StringBuilder sv = new StringBuilder(len);
        for(int i = 0; i<len; i++){
            sv.append(key.charAt(new Random().nextInt(key.length())));
        }
        return sv.toString();
    }
}
