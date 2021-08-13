package Restaurant.Model;

import java.util.List;

public class Restaurant {
    private String name;
    private RestaurantType restaurantType;
    private List<Article> articleList;
    private boolean working;
    private Location restaurantLocation;
    private String logo;

    public Restaurant(String name, RestaurantType restaurantType, List<Article> articleList, boolean working, Location restaurantLocation, String logo) {
        this.name = name;
        this.restaurantType = restaurantType;
        this.articleList = articleList;
        this.working = working;
        this.restaurantLocation = restaurantLocation;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(Location restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
