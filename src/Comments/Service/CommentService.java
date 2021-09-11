package Comments.Service;

import Comments.Model.Comment;
import Comments.Model.CommentStatus;
import Comments.Repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentService {
    private CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    public boolean create(String username, String name, String content, int rating, String profileImage) {
        Comment comment = new Comment(username, name, content, rating, profileImage);
        comment.setId(commentRepository.getNextId());
        return commentRepository.Create(comment);
    }

    public List<Comment> getOwnerComments(String restName) {
        return commentRepository.getAll().stream().filter(c -> c.getRestName().equals(restName)
                && !c.getStatus().equals(CommentStatus.Approved)
                && !c.isDeleted()).collect(Collectors.toList());
    }

    public float approve(Integer intID, String name) {
        Optional<Comment> c = commentRepository.get(intID);
        if(!c.isPresent()){
            return -1;
        }
        c.get().setStatus(CommentStatus.Approved);
        if(!commentRepository.Update(c.get())){
            return -1;
        }

        double newAverage = 0.0;
        int count = 0;
        for(Comment com: getByRestaurantName(name)){
            newAverage += com.getRating();
            ++count;
        }

        return (float) (newAverage / count);

    }

    public List<Comment> getByRestaurantName(String name) {
        return commentRepository.getAll().stream().filter(c -> c.getRestName().equals(name)
                && c.getStatus().equals(CommentStatus.Approved) && !c.isDeleted()).collect(Collectors.toList());
    }

    public boolean decline(Integer intID) {
        Optional<Comment> c = commentRepository.get(intID);
        if(!c.isPresent()){
            return false;
        }
        c.get().setStatus(CommentStatus.Declined);
        return commentRepository.Update(c.get());
    }

    public boolean delete(Integer id) {
        return commentRepository.Delete(id);
    }
}
