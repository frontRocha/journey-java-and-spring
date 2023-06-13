package br.com.book.controllers;

import br.com.book.businessException.BusinessException;
import br.com.book.dtos.errorResponse.ErrorResponseDto;
import br.com.book.models.user.User;
import br.com.book.repository.user.UserRepository;
import br.com.book.security.filter.JWTAuthenticate;
import br.com.book.dtos.authenticationResponse.AuthenticationResponseDto;
import br.com.book.services.userDetailServiceImpl.UserDetailServiceImpl;
import br.com.book.utils.tokenGenerator.TokenGenerator;
import br.com.book.utils.tokenGenerator.TokenGenerate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class UserController implements TokenGenerator {

    private final UserRepository repository;
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder encoder;
    private final ErrorResponseDto errorResponseDto;
    private final AuthenticationResponseDto authenticationResponseDto;
    private final TokenGenerate tokenGenerate;

    private static Map<String, Object> userData = new HashMap<>();

    public UserController(UserRepository repository,
                          UserDetailServiceImpl userDetailService,
                          PasswordEncoder encoder,
                          ErrorResponseDto errorResponseDto,
                          AuthenticationResponseDto authenticationResponseDto,
                          TokenGenerate tokenGenerate) {
        this.repository = repository;
        this.userDetailService = userDetailService;
        this.encoder = encoder;
        this.errorResponseDto = errorResponseDto;
        this.authenticationResponseDto = authenticationResponseDto;
        this.tokenGenerate = tokenGenerate;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user) {
        try {
            existsByLoginUser(user);
            setUserPassword(user);
            setDateUser(user);

            ResponseEntity.ok(repository.save(user));

            String tokenBuilder = tokenGenerate.generateTokenAuthentication(user.getLogin(), user.getId());
            String token = tokenBuilder;
            setTokenInAuthenticationResponse(token);

            defineUserIdInUserData(user.getId());
            defineUserLoginInUserData(user.getLogin());
            setUserDataInAuthenticationResponse();

            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponseDto);
        } catch(BusinessException err) {
            setMessageError(err.getMessage());
            setCodeError(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
        }
    }

    @Override
    public String generateTokenAuthentication(String login, Integer id) {
        return JWT.create()
                .withSubject(login)
                .withClaim("id", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTAuthenticate.TOKEN_EXPIRACAO))
                .sign(Algorithm.HMAC512(JWTAuthenticate.TOKEN_SENHA));
    }

    private void existsByLoginUser(User user) throws BusinessException {
        userDetailService.existsByLogin(user.getLogin());
    }

    private void setUserPassword(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

    private void setDateUser(User user) {
        user.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
    }

    private void defineUserLoginInUserData(String login) {
        userData.put("login", login);
    }

    private void defineUserIdInUserData(Integer id) {
        userData.put("id", id);
    }

    private void setMessageError(String message) {
        errorResponseDto.setMessageError(message);
    }

    private void setCodeError(Integer code) {
        errorResponseDto.setCodeError(code);
    }

    private void setTokenInAuthenticationResponse(String token) {
        authenticationResponseDto.setToken(token);
    }

    private void setUserDataInAuthenticationResponse() {
        authenticationResponseDto.setData(userData);
    }
}
