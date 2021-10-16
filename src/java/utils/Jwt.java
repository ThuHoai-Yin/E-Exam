package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.Cookie;
import model.Authentication;

public class Jwt {

    private static final String secret = "3&wTRu}.G@>s]MWU";
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    public static String generateToken(Authentication auth) {
        return Jwts.builder()
                .setClaims(new HashMap<String, Object>() {
                    {
                        put("id", auth.getID());
                        put("role", auth.getRole());
                    }
                }).setSubject(auth.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Authentication validateToken(Cookie cookie) {
        if (cookie == null) {
            return null;
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(cookie.getValue()).getBody();
            return new Authentication(
                    claims.get("id", Integer.class),
                    claims.getSubject(),
                    claims.get("role", String.class));
        } catch (Exception ex) {
            return null;
        }
    }
}
