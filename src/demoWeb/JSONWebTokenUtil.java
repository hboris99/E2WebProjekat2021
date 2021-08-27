package demoWeb;

import User.Model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import spark.Request;

import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

public class JSONWebTokenUtil {

    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
   private static String secretString;


    public static Optional<String> parseJws(Request request)  {
        String auth = request.headers("Authorization");
        if (auth == null || auth.isEmpty() || !auth.contains("Bearer")) {
            return Optional.empty();
        }

        String jws = auth.substring(auth.indexOf("Bearer") + 6);
        if(jws.equalsIgnoreCase("undefined") || auth.length() <= 6) {
            return Optional.empty();
        }
        return Optional.of(jws);
    }

    public static String generateJws(User user) {
        secretString = Encoders.BASE64.encode(key.getEncoded());
        return Jwts.builder()
                .setSubject(user.getID())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .signWith(key)
                .compact();
    }

    public static String getUsername(String jws) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject();
    }
}
