package br.com.book.security.filter;

import br.com.book.businessException.BusinessException;
import br.com.book.dtos.errorResponse.ErrorResponseDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTValidate extends OncePerRequestFilter {

    public static final String HEADER_ATRIBUTO = "Authorization";
    public static final String ATRIBUTO_PREFIXO = "Bearer ";

    private final ErrorResponseDto errorResponseDto;

    public JWTValidate(ErrorResponseDto errorResponseDto) {
        this.errorResponseDto = errorResponseDto;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            processRequest(request, response, chain);

            String atributo = request.getHeader(HEADER_ATRIBUTO);
            validateHeaders(atributo);

            String token = atributo.replace(ATRIBUTO_PREFIXO, "");
            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch(BusinessException err) {
            setMessageError(err.getMessage());
            setCodeError(HttpStatus.UNAUTHORIZED.value());
            writerResponseError(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if ("/signup".equals(request.getRequestURI()) || "/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
    }

    private void writerResponseError(HttpServletResponse response) throws IOException {
        response.setStatus(errorResponseDto.getCodeError());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"" + errorResponseDto.getMessageError() + "\", \"code\":\"" + errorResponseDto.getCodeError() + "\"}");
        response.getWriter().flush();
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) throws BusinessException {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        validateExpirationToken(expirationDate);
        
        String user = decodedJWT.getSubject();

        verifyUser(user);
        
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }

    private void validateExpirationToken(Date expirationDate) throws BusinessException {
        if (expirationDate != null && expirationDate.before(new Date())) {
            throw new BusinessException("Token expired");
        }
    }

    private String verifyUser(String user) {
        if (user == null) {
            return null;
        }
        
        return user;
    }

    private void setMessageError(String message) {
        errorResponseDto.setMessageError(message);
    }

    private void setCodeError(Integer code) {
        errorResponseDto.setCodeError(code);
    }

    private void validateHeaders(String atributo) throws BusinessException {
        if (atributo == null || !atributo.startsWith(ATRIBUTO_PREFIXO)) {
            throw new BusinessException("Token not found");
        }
    }
}