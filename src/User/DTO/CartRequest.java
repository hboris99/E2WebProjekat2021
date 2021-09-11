package User.DTO;

public class CartRequest {
    public String articleName;
    public int ammount;

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public CartRequest(String articleName, int ammount) {
        this.articleName = articleName;
        this.ammount = ammount;
    }
}
