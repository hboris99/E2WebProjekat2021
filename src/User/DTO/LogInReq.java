package User.DTO;

public class LogInReq {
    private String username;
    private String password;


    public LogInReq(String username, String pass){
        this.username = username;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
