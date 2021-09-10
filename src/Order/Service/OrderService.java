package Order.Service;

import Order.Model.Order;
import Order.Model.OrderStatusType;
import Order.Repository.OrderRepository;
import Restaurant.Model.Restaurant;
import User.Model.Cart;
import User.Model.User;

import java.time.LocalDateTime;
import java.util.List;
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
}
