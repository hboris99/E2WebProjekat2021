package demoWeb;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.File;
import java.security.Key;
import java.util.Date;

import com.google.gson.Gson;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import spark.Session;
public class Test {

    private static Gson g = new Gson();


    public static void main(String[] args) throws Exception {
        port(8080);
        staticFiles.externalLocation(new File("./static").getCanonicalPath());


        get("/rest/demo/test", (req, res) -> {
            return "RADI BRE";
        });
    }

}
