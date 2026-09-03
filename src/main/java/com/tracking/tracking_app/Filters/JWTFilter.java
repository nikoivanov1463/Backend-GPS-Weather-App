package com.tracking.tracking_app.Filters;

import com.tracking.tracking_app.JWT.JWTProvider;
import com.tracking.tracking_app.Entities.User;
import com.tracking.tracking_app.Repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;
    private final UserRepository userRepository;

    public JWTFilter(JWTProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    private static final List<String> WHITELIST = List.of(
            "/api/register",
            "/api/login",
            "/error",
            "/web/**",
            "/js/**",
            "/css/**",
            "/api/session-check",
            "/api/logout",
            "/favicon.ico"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return WHITELIST.stream().anyMatch(whitelisted -> pathMatcher.match(whitelisted, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = getAccessToken(request);

        if (token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            return;
        }

        if (!jwtProvider.validateJWToken(token.get())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        String userId = jwtProvider.getUserIdFromJWT(token.get());

        User authUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        authUser,
                        null,
                        authUser.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private Optional<String> getAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }

        return Optional.empty();
    }
}
