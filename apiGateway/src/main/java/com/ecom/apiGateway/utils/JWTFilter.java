package com.ecom.apiGateway.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private myUserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final int MAX_REQUEST = 40;
    private final long EXPIRY_TIME = 60; // in seconds
    private final long WINDOW = 60;      // in seconds

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUserName(token);
        }

        String userKey = "rate_limit:" + ((username != null) ? username : request.getRemoteAddr());

        // increment count
        Long requestCount = redisTemplate.opsForValue().increment(userKey);
        System.out.println("Count: " + requestCount);

        if (requestCount != null && requestCount == 1) {
            redisTemplate.expire(userKey, WINDOW, TimeUnit.SECONDS);
        }

        // check block key first
        Boolean isBlocked = redisTemplate.hasKey(userKey + ":blocked");
        if (Boolean.TRUE.equals(isBlocked)) {
            response.setStatus(HttpServletResponse.SC_REQUEST_URI_TOO_LONG);
            response.getWriter().write("You are blocked for 1 minute.");
            return;
        }

        // block if exceeded
        if (requestCount != null && requestCount > MAX_REQUEST) {
            redisTemplate.opsForValue().set(userKey + ":blocked", "true", EXPIRY_TIME, TimeUnit.SECONDS);
            response.setStatus(HttpServletResponse.SC_REQUEST_URI_TOO_LONG);
            response.getWriter().write("Too many requests. You are blocked for 1 minute.");
            return;
        }

        // normal authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
