package by.av.test.testavby.config;

import by.av.test.testavby.security.UserDetailsImpl;
import by.av.test.testavby.security.UserDetailsServiceImpl;
import by.av.test.testavby.util.JWTUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            String jwtToken = authorization.substring(7);

            try {
                LOG.atInfo().log("Before check jwt token");
                String email = jwtUtil.validateTokenAndRetrieveClaim(jwtToken);
                UserDetailsImpl personDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        personDetails, personDetails.getPassword(), personDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (JWTVerificationException exception){
                LOG.atError().log("JWT token isn't valid");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT token isn't valid");
                return;
            }
        }

        LOG.atInfo().log("Before do filter");
        filterChain.doFilter(request, response);
    }
}
