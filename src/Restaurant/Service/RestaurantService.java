package Restaurant.Service;

import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Restaurant;
import Restaurant.Repository.RestaurantRepository;

import java.io.IOException;
import java.util.Optional;

public class RestaurantService {
    RestaurantRepository restaurantRepository;
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    public Optional<Restaurant> createRestaurant(RestaurantRequest restaurantRequest, String filename) throws IOException{
        System.out.println(restaurantRequest.getName());
        Restaurant r= new Restaurant(restaurantRequest.getName(), restaurantRequest.getRestaurantType(),false, restaurantRequest.getLocation(), filename);
        r.setManagerUsername(restaurantRequest.getManagerUsername());
        Integer id = restaurantRepository.getNextID();
        r.setID(id);
        if(!restaurantRepository.Create(r)){
            return Optional.empty();
        }
        return Optional.of(r);
    }
}
