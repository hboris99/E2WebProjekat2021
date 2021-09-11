package User.Model;

public class BuyerType {
    private BuyerTypeRank buyerTypeRank;
    private double discount;
    private double pointsNeededForType;

    public BuyerType(BuyerTypeRank buyerTypeRank, double discount, double pointsNeededForType) {
        this.buyerTypeRank = buyerTypeRank;
        this.discount = discount;
        this.pointsNeededForType = pointsNeededForType;
    }

    public BuyerTypeRank getBuyerTypeRank() {
        return buyerTypeRank;
    }

    public void setBuyerTypeRank(BuyerTypeRank buyerTypeRank) {
        this.buyerTypeRank = buyerTypeRank;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPointsNeededForType() {
        return pointsNeededForType;
    }

    public void setPointsNeededForType(double pointsNeededForType) {
        this.pointsNeededForType = pointsNeededForType;
    }
}
