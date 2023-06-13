package br.com.book.security.filter;

import br.com.book.data.UserDetail;
import br.com.book.models.user.User;
import br.com.book.dtos.authenticationResponse.AuthenticationResponseDto;
import br.com.book.utils.tokenGenerator.TokenGenerate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticate extends UsernamePasswordAuthenticationFilter {


    private final TokenGenerate tokenGenerate;
    private final AuthenticationResponseDto authenticationResponseDto;
    private final AuthenticationManager authenticationManager;

    private static Map<String, Object> userData = new HashMap<>();
    public static final int TOKEN_EXPIRACAO = 600_000;
    public static final String TOKEN_SENHA = "463408a1-54c9-4307-bb1c-6cced559f5a7";

    public JWTAuthenticate(TokenGenerate tokenGenerate, AuthenticationManager authenticationManager, AuthenticationResponseDto authenticationResponseDto) {
        this.tokenGenerate = tokenGenerate;
        this.authenticationManager = authenticationManager;
        this.authenticationResponseDto = authenticationResponseDto;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getLogin(),
                    user.getPassword(),
                    new ArrayList<>()
            ));

        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuario", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetail user = (UserDetail) authResult.getPrincipal();

        String token = tokenGenerate.generateTokenAuthentication(user.getUsername(), user.getId());
        setTokenInAuthenticationResponse(token);

        defineUserLoginInUserData(user.getUsername());
        defineUserIdInUserData(user.getId());
        setUserDataInAuthenticationResponse();
        dataResponse(response);
    }

    private void setTokenInAuthenticationResponse(String token) {
        authenticationResponseDto.setToken(token);
    }

    private void setUserDataInAuthenticationResponse() {
        authenticationResponseDto.setData(userData);
    }

    private void defineUserLoginInUserData(String login) {
        userData.put("login", login);
    }

    private void defineUserIdInUserData(Integer id) {
        userData.put("id", id);
    }

    private IOException dataResponse(HttpServletResponse response) {
        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(authenticationResponseDto);

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();

            return null;
        } catch(IOException err) {
            return err;
        }
    }
}

