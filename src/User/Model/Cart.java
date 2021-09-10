package User.Model;

import Restaurant.Model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Cart {

    private List<CartItem> cartItemsList;
    private String buyer; //Moze posle da se pretvori samo u string i da bude buyer username posto je jedinstven.
    private double price;
    private String restaurantName;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Cart(String buyer) {
        this.cartItemsList = new ArrayList<CartItem>();
        this.buyer = buyer;
        this.price = 0.0;
    }
    public Cart (){

    }
    public List<Article> getArticles(){
        return cartItemsList.stream().map(ci -> ci.getArticle()).collect(Collectors.toList());
    }
    public List<CartItem> getCartItemsList() {
        return cartItemsList;
    }

    public void setCartItemsList(List<CartItem> cartItemsList) {
        this.cartItemsList = cartItemsList;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addArticle(CartItem newItem) {
        Optional<CartItem> item = cartItemsList.stream().filter(i -> i.getArticle().getName().equals(newItem.getArticle().getName()))
                .findFirst();
        if(item.isPresent()) {
            item.get().setQuantity(item.get().getQuantity() + newItem.getQuantity());
            recalculatePrice();
            return;
        }
        cartItemsList.add(newItem);
        recalculatePrice();
    }

    private void recalculatePrice() {
        price = 0.0;
        for(CartItem i : cartItemsList) {

            price += i.getPrice();
        }
    }
    public boolean removeArticle(String name) {
        Optional<CartItem> ci = cartItemsList.stream().filter(i -> i.getArticle().getName().equals(name)).findFirst();
        if(!ci.isPresent()){
            return false;
        }

        cartItemsList.removeIf(item -> item.getArticle().getName().equals(name));
        recalculatePrice();
        return true;
    }

    public void clear() {
        cartItemsList.clear();
        restaurantName="";
        recalculatePrice();
    }
}
