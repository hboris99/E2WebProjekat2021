package User.DTO;

import Restaurant.Model.Article;
import Restaurant.Model.Location;
import Restaurant.Model.Restaurant;
import Restaurant.Model.RestaurantType;

import java.util.List;

public class MRestorauntResponse {
    private Integer ID;
    private String name;
    private RestaurantType restaurantType;
    private String managerUsername;
    private List<Article> articleList;
    private boolean working;
    private Location restaurantLocation;
    private String logo;
    private boolean isDeleted;

    public MRestorauntResponse(Restaurant r){
            this.ID = r.getID();
            this.name = r.getName();
            this.logo = r.getLogo();
            this.restaurantType = r.getRestaurantType();
            this.managerUsername = r.getManagerUsername();
            this.articleList = r.getArticleList();
            this.working = r.isWorking();
            this.restaurantLocation = r.getRestaurantLocation();
            this.isDeleted = r.isDeleted();
    }
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
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

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public MRestorauntResponse(Integer ID, String name, RestaurantType restaurantType, String managerUsername, List<Article> articleList, boolean working, Location restaurantLocation, String logo, boolean isDeleted) {
        this.ID = ID;
        this.name = name;
        this.restaurantType = restaurantType;
        this.managerUsername = managerUsername;
        this.articleList = articleList;
        this.working = working;
        this.restaurantLocation = restaurantLocation;
        this.logo = logo;
        this.isDeleted = isDeleted;
    }
}
