package br.com.book.utils.tokenGenerator;


import br.com.book.security.filter.JWTAuthenticate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGenerate implements TokenGenerator {

    @Override
    public String generateTokenAuthentication(String login, Integer id) {
        return JWT.create()
                .withSubject(login)
                .withClaim("id", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTAuthenticate.TOKEN_EXPIRACAO))
                .sign(Algorithm.HMAC512(JWTAuthenticate.TOKEN_SENHA));
    }
}
