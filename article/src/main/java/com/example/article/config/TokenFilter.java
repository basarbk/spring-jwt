package com.example.article.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class TokenFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    if (authorization != null) {

      String token = authorization.substring(7);
      try {
        JwtParser parser = Jwts.parser().setSigningKey("my-app-secret");
        parser.parse(token);
        Claims claims = parser.parseClaimsJws(token).getBody();
        String username = (String) claims.get("username");
        Long userId = Long.valueOf((String) claims.get("id"));

        AppUser appUser = new AppUser(username, userId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(appUser, null,
            appUser.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    filterChain.doFilter(request, response);
  }

}
