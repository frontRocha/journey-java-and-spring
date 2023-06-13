package br.com.book.utils.jwtDecode;

import br.com.book.security.filter.JWTAuthenticate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JWTDecode {

    public static Integer extractUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JWTAuthenticate.TOKEN_SENHA))
                .build()
                .verify(token);

        String user = decodedJWT.getSubject();
        verifyUserEnull(user);

        return decodedJWT.getClaim("id").asInt();
    }

    public String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        };
        return null;
    }

    public static Object verifyUserEnull(String user) {
        if (user == null) {
            return null;
        }

        return user;
    }
}
