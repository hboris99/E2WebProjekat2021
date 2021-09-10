package User.Service;

import Order.Model.Order;
import Order.Model.OrderStatusType;
import Order.Service.OrderService;
import Restaurant.Model.Article;
import Restaurant.Model.Restaurant;
import Restaurant.Service.RestaurantService;
import User.DTO.CartRequest;
import User.DTO.LogInReq;
import User.Model.User;
import User.Model.UserRoleType;
import User.DTO.RegisterReq;
import User.Model.*;
import User.Repository.UserRepository;
import demoWeb.JSONWebTokenUtil;
import spark.Request;

import java.util.Collections;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private UserRepository userRepository;
    private RestaurantService restaurantService;
    private OrderService orderService;

    public UserService(UserRepository userRepository, RestaurantService restaurantService, OrderService orderService){

        this.restaurantService = restaurantService;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }


    public Optional<User> validateJWT(Request r, UserRoleType role){
        Optional<String> jwt = JSONWebTokenUtil.parseJws(r);
        if(!jwt.isPresent()){
            return Optional.empty();
        }
        String username = JSONWebTokenUtil.getUsername(jwt.get());
        Optional<User> user = userRepository.get(username);
        if(!user.isPresent() || !user.get().getUserRoleType().equals(role)){
            return Optional.empty();
        }
        return user;
    }
    public Optional<User> validateJWT(Request req) {
        Optional<String> jws = JSONWebTokenUtil.parseJws(req);
        if(!jws.isPresent()) {
            return Optional.empty();
        }
        String username = JSONWebTokenUtil.getUsername(jws.get());
        Optional<User> u = getByUsername(username);
        return u;
    }
    public Optional<String> loginUser(LogInReq req){
        Optional<User> user = userRepository.get(req.getUsername());
        if(!user.isPresent() || !user.get().getPassword().equals(req.getPassword())){
            return Optional.empty();
        }

        return Optional.of(JSONWebTokenUtil.generateJws(user.get()));

    }

    public List<User> getAllUsers(){
        return userRepository.getAll().stream().filter(u -> !u.isDeleted()).collect(Collectors.toList());
    }
    public Optional<User> getByUsername(String username) {
        System.out.println(userRepository.get(username));
        return userRepository.get(username);
    }
    public boolean registerUser(RegisterReq req) throws ParseException {
        Optional<User> reg = userRepository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Buyer newBuyer = new Buyer(req.getUsername(),req.getPassword(),req.getName(), req.getSurname(),req.getGender(),req.getBirthDate());
        return userRepository.Create(newBuyer);
    }

    public boolean registerDeliverer(RegisterReq req) {
        if(getByUsername(req.getUsername()).isPresent()){
            return false;
        }
        Deliverer deliverer = new Deliverer(req.getUsername(), req.getPassword(),req.getName(), req.getSurname(), req.getGender(), req.getBirthDate());
        System.out.println(deliverer.getClass().getName());
        return userRepository.Create(deliverer);
    }

    public boolean registerManager(RegisterReq req) {
        if(getByUsername(req.getUsername()).isPresent()){
            return false;
        }

        Manager manager = new Manager(req.getUsername(), req.getPassword(), req.getName(), req.getSurname(), req.getGender(), req.getBirthDate());
        System.out.println(manager.getClass().getName());

        return userRepository.Create(manager);
    }

    public boolean deleteUser(String username) {
        return userRepository.Delete(username);
    }


    public boolean updateUser(RegisterReq req) {
        Optional<User> u = getByUsername(req.getUsername());
        if (!u.isPresent()) {
            return false;
        }
        User r = u.get();
        r.setDateOfBirth(req.getBirthDate());
        r.setGenderType(req.getGender());
        r.setName(req.getName());
        r.setSurname(req.getSurname());
        if(req.getPassword() != null && !req.getPassword().isBlank() && req.getPassword().length() >= 4)
            r.setPassword(req.getPassword());
        return userRepository.Update(r);
    }

    public boolean blockUser(String username) {
        Optional<User> u = getByUsername(username);
        if(!u.isPresent()) {
            return false;
        }
        u.get().setBlocked(true);
        return updateUser(u.get());
    }
    public boolean unblockUser(String username) {
        Optional<User> u = getByUsername(username);
        if(!u.isPresent()) {
            return false;
        }
        u.get().setBlocked(false);
        return updateUser(u.get());
    }
    public boolean updateUser(User user) {
        return userRepository.Update(user);
    }

    public List<Manager> getRestaurantlessManagers() {

        return   getManagers().stream().filter(m -> m.getRestaurant() == null)
                .collect(Collectors.toList());
    }
    public List<Manager> getManagers() {



        return getAllUsers().stream().filter(u -> u.getUserRoleType().equals(UserRoleType.Manager)  && u instanceof Manager)
                .map(u -> (Manager) u)
                .collect(Collectors.toList());
    }

    public boolean addRestaurantToManager(String managerUsername, Restaurant restaurant) {
        Optional<User> u = getByUsername(managerUsername);
        if(!u.isPresent() || !u.get().getUserRoleType().equals(UserRoleType.Manager)){
            return false;
        }
        Manager m = (Manager) u.get();
        m.setRestaurant(restaurant);
        return userRepository.Update(m);
    }

    public boolean addToCart(Buyer buyer, List<CartRequest> products, String restaurantName) {
        List<Article> articles = restaurantService.getArticlesByRestaurantName(restaurantName);
        List<CartItem> newItems;

        double discountModifier = 1.0;
        if(buyer.getBuyerType().getBuyerTypeRank().equals(BuyerTypeRank.Silver)){
            discountModifier = 0.95;
        }
        if(buyer.getBuyerType().getBuyerTypeRank().equals(BuyerTypeRank.Gold)){
            discountModifier = 90;
        }
        if(buyer.getBuyerType().getBuyerTypeRank().equals(BuyerTypeRank.Platinum)){
            discountModifier = 85;
        }
        final double finalM = discountModifier;

        newItems = products.stream().flatMap(i -> articles.stream().filter(a -> a.getName().equals(i.getArticleName()))

                    .map(ci -> new CartItem(ci, i.getAmmount())))
                .collect(Collectors.toList());

        newItems.forEach(newItem -> {
            newItem.getArticle().setPrice(newItem.getArticle().getPrice() * finalM);
            buyer.getCart().addArticle(newItem);
        });

        buyer.getCart().setRestaurantName(restaurantName);
        return userRepository.Update(buyer);
    }

    public boolean removeFromCart(Buyer buyer, String name) {
        return buyer.getCart().removeArticle(name) && updateUser(buyer);
    }
    public List<Order> getUserOrders(User user) {
        switch(user.getUserRoleType()){
            case Buyer:{
                    return orderService.getByUsername(user.getUsername());
            }
            case Manager:{
                return orderService.getByManagerUsername(user.getUsername());
            }
            case Deliverer:
                return orderService.getByDelivererUsername(user.getUsername());
        }
        return Collections.emptyList();

    }

    public boolean createOrder(Buyer bueyr) {
        String restaurant = bueyr.getCart().getRestaurantName();
        if(restaurant == null || restaurant.isBlank()){
            return false;
        }
        Optional<Restaurant> rest = restaurantService.getByName(restaurant);
        if(!rest.isPresent()){
            return false;
        }
        bueyr.addOrder(orderService.createOrder(bueyr.getCart(), rest.get(), bueyr.getUsername(), bueyr.getName(), bueyr.getSurname()));
        bueyr.addPoints(bueyr.getCart().getPrice()/ 1000 * 133);
        bueyr.getCart().clear();

        return userRepository.Update(bueyr);
    }

    public boolean cancelOrder(Buyer buyer, String id) {
        double orPrice = orderService.cancel(id);
        if(orPrice <= 0.0){
            return false;
        }
        buyer.subPoints(orPrice /1000 * 133 * 4);
        buyer.increaseOrderCancelCount();
        return updateUser(buyer);
    }

    public boolean requestOrder(Deliverer deliverer, String id) {
        Optional<Order> o = orderService.get(id);
        if(!o.isPresent() || !o.get().getStats().equals(OrderStatusType.Waiting)){
            return false;
        }

        Optional<User> u = getByUsername(o.get().getRestaurant().getManagerUsername());
        if(!u.isPresent() || !u.get().getUserRoleType().equals(UserRoleType.Manager)){
            return false;
        }
        Manager m = (Manager) u.get();
        m.addDeliveryRequest(new DeliveryRequest(id, deliverer.getUsername()));
        return updateUser(m);


    }

    public boolean approveOrder(Manager manager, DeliveryRequest dr) {
        Optional<Order> o = orderService.get(dr.getOrderId());
        if(!o.isPresent() || !o.get().getStats().equals(OrderStatusType.Waiting)){
            return false;
        }
        Optional<User> u = getByUsername(dr.getUsername());
        if(!u.isPresent()){
            return false;
        }
        Deliverer d = (Deliverer) u.get();
        o.get().setStats(OrderStatusType.Transport);
        o.get().setDelivererUsername(d.getUsername());
        d.addOrder(o.get().getID());
        manager.removeRequest(dr);
        return updateUser(manager) && updateUser(d) && orderService.update(o.get());



    }

    public Object declineRequest(Manager manager, DeliveryRequest dr) {
        Optional<User> u = getByUsername(dr.getUsername());
        if(!u.isPresent()){
            return false;
        }
        return  manager.removeRequest(dr) && userRepository.Update(manager);
    }

    public List<Buyer> getRestaurantCustomers(Manager manager) {
        Restaurant r = manager.getRestaurant();

        if(r==null){
            return Collections.emptyList();
        }
        return orderService.getRestaurantCustomerUsernames(r.getName()).stream().flatMap(u -> getAllUsers().stream()
        .filter(us -> us.getUsername().equals(u))
        .map(user-> (Buyer) user)).collect(Collectors.toList());
    }
}
