package User.DTO;

import User.Model.Buyer;
import User.Model.User;

public class BuyerResponse extends UserResponse {
    private double points;
    private String memberType;
    private boolean suspicious;

    public BuyerResponse(String username, String password, String name, String surname, String gender, String birthDate, String profileImage) {
        super(username, password, name, surname, gender, birthDate, profileImage);
        this.points = points;
        this.memberType = memberType;
        this.suspicious = suspicious;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setSuspicious(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public BuyerResponse(Buyer user) {
        super(user);
        this.points = user.getPoints();
        this.memberType = user.getBuyerType().getBuyerTypeRank().name();
        this.suspicious = user.getSus();
    }
}
