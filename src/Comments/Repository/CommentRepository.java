package Comments.Repository;

import Comments.Model.Comment;
import com.google.gson.reflect.TypeToken;
import demoWeb.JSONRepository;

import java.lang.reflect.Type;
import java.util.List;

public class CommentRepository extends JSONRepository<Comment, Integer> {
    public CommentRepository(String file) {
        super(file, new TypeToken<List<Comment>>(){}.getType());
    }
    public Integer getNextId() {
        return getAll().size() + 1;
    }
}
