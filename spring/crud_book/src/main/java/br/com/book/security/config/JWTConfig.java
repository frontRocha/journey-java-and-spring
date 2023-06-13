package br.com.book.security.config;

import br.com.book.dtos.authenticationResponse.AuthenticationResponseDto;
import br.com.book.dtos.errorResponse.ErrorResponseDto;
import br.com.book.security.filter.JWTValidate;
import br.com.book.security.filter.JWTAuthenticate;
import br.com.book.services.userDetailServiceImpl.UserDetailServiceImpl;
import br.com.book.utils.tokenGenerator.TokenGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class JWTConfig{

    private final UserDetailServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationResponseDto authenticationResponseDto;
    private final TokenGenerate tokenGenerate;
    private final ErrorResponseDto errorResponseDto;

    public JWTConfig(UserDetailServiceImpl usuarioService, PasswordEncoder passwordEncoder, AuthenticationConfiguration authenticationConfiguration, AuthenticationResponseDto authenticationResponseDto, TokenGenerate tokenGenerate, ErrorResponseDto errorResponseDto) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationConfiguration = authenticationConfiguration;
        this.authenticationResponseDto = authenticationResponseDto;
        this.tokenGenerate = tokenGenerate;
        this.errorResponseDto = errorResponseDto;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests((authorize) -> {
                    try {
                        authorize
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                                .anyRequest().authenticated().and()
                                .addFilter(new JWTAuthenticate(tokenGenerate, authenticationConfiguration.getAuthenticationManager(), authenticationResponseDto))
                                .addFilterAfter(new JWTValidate(errorResponseDto), JWTAuthenticate.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}