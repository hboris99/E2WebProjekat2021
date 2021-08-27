package User.Model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> cartItemsList;
    private Buyer buyer; //Moze posle da se pretvori samo u string i da bude buyer username posto je jedinstven.
    private double price;

    public Cart(Buyer buyer, double price) {
        cartItemsList = new ArrayList<CartItem>();
        this.buyer = buyer;
        this.price = 0.0;
    }
    public Cart (){

    }

    public List<CartItem> getCartItemsList() {
        return cartItemsList;
    }

    public void setCartItemsList(List<CartItem> cartItemsList) {
        this.cartItemsList = cartItemsList;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
