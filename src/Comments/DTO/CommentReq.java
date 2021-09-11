package Comments.DTO;

public class CommentReq {
    private String content;
    private int rating;

    public CommentReq(String content, int rating){
        this.content = content;
        this.rating = rating;
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
}
