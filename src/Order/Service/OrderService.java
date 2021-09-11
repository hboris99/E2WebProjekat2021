package Order.Service;

import Order.Model.Order;
import Order.Model.OrderStatusType;
import Order.Repository.OrderRepository;
import Restaurant.Model.Restaurant;
import User.Model.Cart;
import User.Model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }


    public List<Order> getByDelivererUsername(String username) {
        return orderRepository.getAll().stream().filter(o -> o.getDelivererUsername() != null && o.getDelivererUsername().equals(username)
                                                        || o.getStats().equals(OrderStatusType.Waiting)).collect(Collectors.toList());
    }

    public List<Order> getByManagerUsername(String username) {
        return orderRepository.getAll().stream().filter(o -> o.getRestaurant().getManagerUsername().equals(username)).collect(Collectors.toList());
    }

    public List<Order> getByUsername(String username) {
        List<Order> orders = orderRepository.getAll();
        return orders.stream().filter(o-> o.getUsername().equals(username)).collect(Collectors.toList());
    }

    public String createOrder(Cart cart, Restaurant restaurant, String username, String name, String surname) {
        Order order = new Order(restaurant, LocalDateTime.now(), cart.getPrice(), username, name, surname );
        String orderID = orderRepository.getNextID();
        order.setId(orderID);
        order.setArticles(cart.getArticles());
        orderRepository.Create(order);
        return orderID;

    }

    public double cancel(String id) {
        Optional<Order> o = orderRepository.get(id);
        if(!o.isPresent()){
            return -1.0;
        }
        o.get().setStats(OrderStatusType.Canceled);
        return orderRepository.Update(o.get())
                ? o.get().getPrice()
                : -1.0;
    }

    public boolean toPrepare(String id) {
        Optional<Order> o = orderRepository.get(id);
        if(!o.isPresent() || o.get().getStats().equals(OrderStatusType.Preparing)){
            return false;
        }
        o.get().setStats(OrderStatusType.Preparing);
        return orderRepository.Update(o.get());

    }

    public boolean toWaiting(String id) {
        Optional<Order> o = orderRepository.get(id);
        if(!o.isPresent() || o.get().getStats().equals(OrderStatusType.Waiting)){
            return false;
        }
        o.get().setStats(OrderStatusType.Waiting);
        return orderRepository.Update(o.get());
    }



    public boolean deliverOrder(String id) {
        Optional<Order> o = orderRepository.get(id);

        if(!o.isPresent() || o.get().getStats().equals(OrderStatusType.Delivered)){
            return false;
        }
        o.get().setStats(OrderStatusType.Delivered);
        return orderRepository.Update(o.get());
    }
    public Optional<Order> get(String id) {
        return orderRepository.get(id);
    }

    public boolean update(Order order) {
       return orderRepository.Update(order);
    }

    public List<String> getRestaurantCustomerUsernames(String name) {
        return orderRepository.getAll().stream().collect(Collectors.toMap(
                Order::getUsername, Function.identity(),
                (o1, o2) -> o1)).keySet().stream().collect(Collectors.toList());

    }

    public boolean hasDeliveredOrder(String username, String name) {
        return orderRepository.getAll().stream().filter(o -> o.getUsername().equals(username)
        && o.getStats().equals(OrderStatusType.Delivered) && o.getRestaurant().getName().equals(name)).findFirst().isPresent();
    }
}
