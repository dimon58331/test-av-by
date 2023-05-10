package by.av.test.testavby.config;

import by.av.test.testavby.payload.response.InvalidLoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        LOG.atInfo().log("Bad credentials");
        InvalidLoginResponse invalidLoginResponse = new InvalidLoginResponse();
        String jsonInvalidLoginResponse = new ObjectMapper().writeValueAsString(invalidLoginResponse);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().println(jsonInvalidLoginResponse);
    }
}
