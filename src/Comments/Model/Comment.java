package Comments.Model;

import demoWeb.IRepository;

public class Comment implements IRepository<Integer> {
    private Integer id;
    private String username;
    private String restName;
    private String content;
    private int rating;
    private String userImage;
    private CommentStatus status;
    private boolean deleted;

    public Comment(Integer id, String username, String restName, String content, int rating, String userImage) {
        this.id = id;
        this.username = username;
        this.restName = restName;
        this.content = content;
        this.rating = rating;
        this.userImage = userImage;
        this.status = CommentStatus.Pending;

    }

    public Comment(String username, String restName, String content, int rating, String userImage) {
        this.username = username;
        this.restName = restName;
        this.content = content;
        this.rating = rating;
        this.userImage = userImage;
        this.status = CommentStatus.Pending;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
    }
}
