package Restaurant.Model;

import demoWeb.IRepository;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements IRepository<Integer> {
    private Integer ID;
    private String name;
    private RestaurantType restaurantType;
    private String managerUsername;
    private List<Article> articleList;
    private boolean working;
    private Location restaurantLocation;
    private String logo;
    private boolean isDeleted;


    public Restaurant(String name, RestaurantType restaurantType, boolean working, Location restaurantLocation, String logo) {
        this.name = name;
        this.restaurantType = restaurantType;
        this.articleList = new ArrayList<Article>();
        this.working = working;
        this.restaurantLocation = restaurantLocation;
        this.logo = logo;
        this.managerUsername = managerUsername;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
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

    @Override
    public Integer getID() {
            return this.ID;
    }

    @Override
    public void setID(Integer id) {
            this.ID = id;
    }

    @Override
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
    }
}
