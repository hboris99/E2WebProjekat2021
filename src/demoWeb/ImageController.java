package demoWeb;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import static demoWeb.Responses.*;
import static spark.Spark.*;
public class ImageController {
    private String uploadDirectory;
    public ImageController(String uploadDirectory){
        this.uploadDirectory = uploadDirectory;
        get("image/:filename", (req, res) -> {
            String filename = req.params(":filename");
            try{
                return Files.readAllBytes(Paths.get(uploadDirectory + File.separator + filename));
            }catch(Exception e){
                e.printStackTrace();
                return internal(res);
            }
        });
    }
}
