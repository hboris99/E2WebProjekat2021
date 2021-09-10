package Restaurant.Service;

import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Restaurant;
import Restaurant.Repository.RestaurantRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestaurantService {
    RestaurantRepository restaurantRepository;
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    public Optional<Restaurant> createRestaurant(RestaurantRequest restaurantRequest, String filename) throws IOException{
        System.out.println(restaurantRequest.getName());
        Restaurant r= new Restaurant(restaurantRequest.getName(), restaurantRequest.getRestaurantType(),false, restaurantRequest.getLocation(), filename,0);
        r.setManagerUsername(restaurantRequest.getManagerUsername());
        Integer id = restaurantRepository.getNextID();
        r.setID(id);
        if(!restaurantRepository.Create(r)){
            return Optional.empty();
        }
        return Optional.of(r);
    }
    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.getAll().stream().collect(Collectors.toList());
    }
}
