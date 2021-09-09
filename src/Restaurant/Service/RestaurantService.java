package Restaurant.Service;

import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Article;
import Restaurant.Model.Restaurant;
import Restaurant.Repository.RestaurantRepository;
import User.DTO.ArticleRequest;
import User.Model.Manager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RestaurantService {
    RestaurantRepository restaurantRepository;
    Restaurant currentRestaurant;
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
   public Optional<Restaurant> getRestaurantByID(Integer id){

        this.currentRestaurant = restaurantRepository.get(id).get();
        return restaurantRepository.get(id);
   }
    public boolean createArticle(ArticleRequest articleRequest, String fileName, Manager m) {
        Article a = new Article(articleRequest.getName(), articleRequest.getPrice(), articleRequest.getArticleType(), articleRequest.getQuantity(), articleRequest.getDescription(), fileName, articleRequest.getRestaurant());
        Restaurant r = m.getRestaurant();

        Optional<Article> article = r.getArticleList().stream().filter(ar -> ar.getName().equals(articleRequest.getName())).findFirst();
        if(article.isPresent() && !article.get().isDeleted()){
            return false;
        }
        if(article.isPresent() && article.get().isDeleted()){
            article.get().setDeleted(false);
            article.get().setName(articleRequest.getName());
            article.get().setArticleType(articleRequest.getArticleType());
            article.get().setQuantity(articleRequest.getQuantity());
            article.get().setPrice(articleRequest.getPrice());
            article.get().setDescription(articleRequest.getDescription());
            return restaurantRepository.Update(r);
        }
        r.addArticle(a);
        return restaurantRepository.Update(r);

    }

    public List<Article> getAllArticles() {
        return this.currentRestaurant.getArticleList();
    }
}
