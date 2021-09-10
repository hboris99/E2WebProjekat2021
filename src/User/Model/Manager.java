package User.Model;

import Restaurant.Model.Restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Manager extends User {
    public Restaurant restaurant;
    private List<DeliveryRequest> requests;

    public Manager(String username, String password, String firstName, String lastName, GenderType sex, Date birthDate) {
        super(username, password, firstName, lastName, sex,birthDate,UserRoleType.Manager);
        isBlocked = false;
        requests = new ArrayList<DeliveryRequest>();

    }

    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<DeliveryRequest> requests) {
        this.requests = requests;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
        if(!requests.stream().filter(req -> req.getUsername().equals(deliveryRequest.getUsername()) && req.getOrderId().equals(deliveryRequest.getOrderId())).findFirst().isPresent()){
            requests.add(deliveryRequest);
        }
    }

    public boolean removeRequest(DeliveryRequest dr) {
        return requests.removeIf(re -> re.getOrderId().equals(dr.getOrderId()) && re.getUsername().equals(dr.getUsername()));
    }
}
