package Restaurant.Service;

import Restaurant.DTO.RestaurantRequest;
import Restaurant.Model.Article;
import Restaurant.Model.Restaurant;
import Restaurant.Repository.RestaurantRepository;
import User.DTO.ArticleRequest;
import User.Model.Manager;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestaurantService {
    RestaurantRepository restaurantRepository;
    Restaurant currentRestaurant;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Optional<Restaurant> createRestaurant(RestaurantRequest restaurantRequest, String filename) throws IOException {
        System.out.println(restaurantRequest.getName());
        Restaurant r = new Restaurant(restaurantRequest.getName(), restaurantRequest.getRestaurantType(), false, restaurantRequest.getLocation(), filename, 0);
        r.setManagerUsername(restaurantRequest.getManagerUsername());
        Integer id = restaurantRepository.getNextID();
        r.setID(id);
        if (!restaurantRepository.Create(r)) {
            return Optional.empty();
        }
        return Optional.of(r);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.getAll().stream().filter(r -> !r.isDeleted()).collect(Collectors.toList());
    }
   /*public Optional<Restaurant> getRestaurantByID(Integer id){

        this.currentRestaurant = restaurantRepository.get(id).get();
        return restaurantRepository.get(id);
   }*/
        public boolean createArticle (Integer id, ArticleRequest articleRequest, String fileName, Manager m){
            Article a = new Article(articleRequest.getName(), articleRequest.getPrice(), articleRequest.getArticleType(), articleRequest.getQuantity(), articleRequest.getDescription(), fileName, id);
            Restaurant r = m.getRestaurant();
            System.out.println(a.getName() + " " + a.getRestaurant() + " " + a.getPrice());
            Optional<Article> article = r.getArticleList().stream().filter(ar -> ar.getName().equals(articleRequest.getName())).findFirst();
            if (article.isPresent() && !article.get().isDeleted()) {
                return false;
            }
            if (article.isPresent() && article.get().isDeleted()) {
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

   /* public List<Article> getAllArticles() {
        return this.currentRestaurant.getArticleList();
    }*/

        public boolean deleteArticle (String articleName, Manager m){

            Restaurant r = m.getRestaurant();

            for (Article a : r.getArticleList()) {
                if (a.getName().equals(articleName)) {
                    a.setDeleted(true);
                    return restaurantRepository.Update(r);
                }
            }
            return false;
        }

        public Article getArticle (String articleName, Manager m){
            Restaurant r = m.getRestaurant();
            for (Article a : r.getArticleList()) {
                if (a.getName().equals(articleName)) {
                    return a;
                }
            }
            return null;
        }

        public boolean update (ArticleRequest articleRequest, String fileName, Manager m){
            Restaurant r = m.getRestaurant();
            if (r == null) {
                return false;
            }
            Optional<Article> a = r.getArticleList().stream().filter(ar -> ar.getName().equals(articleRequest.getName()) && !ar.isDeleted()).findFirst();
            if (!a.isPresent()) {
                return false;
            }

            a.get().setName(articleRequest.getName());
            a.get().setArticleType(articleRequest.getArticleType());
            a.get().setQuantity(articleRequest.getQuantity());
            a.get().setDescription(articleRequest.getDescription());
            a.get().setPrice(articleRequest.getPrice());
            a.get().setLogo(fileName);
            return restaurantRepository.Update(r);
        }



    public Optional<Restaurant> getByName(String params) {
        if(params.isBlank()) {
            return Optional.empty();
        }
        return restaurantRepository.getAll().stream().filter(r -> r.getName().equals(params)).findFirst();
    }

    public List<Article> getArticlesByRestaurantName(String name) {
        Optional<Restaurant> r = getByName(name);
        if(!r.isPresent()) {
            return Collections.emptyList();
        }
        return r.get().getArticleList().stream().filter(a -> !a.isDeleted()).collect(Collectors.toList());
    }
}
