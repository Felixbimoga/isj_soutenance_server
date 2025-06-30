package com.EasayHelp.EasayHelp.filter;

import com.EasayHelp.EasayHelp.configuration.JwtUtils;
import com.EasayHelp.EasayHelp.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt =  authHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateToken(jwt, userDetails)) {
                // Récupérer les rôles à partir des claims
                String role = jwtUtils.extractClaim(jwt, claims -> claims.get("role", String.class));

                // Récupérer les authorities en fonction du rôle
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                // Créer une nouvelle liste d'autorités avec celles de l'utilisateur et celle extraite du JWT
                List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
                authorities.add(authority);  // Ajouter l'autorité du rôle


                // Créer un token d'authentification avec les rôles
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                // Ajouter le rôle aux autorités (si nécessaire)
                //if (role != null) {
                //authenticationToken.getAuthorities().add(new SimpleGrantedAuthority("ROLE_" + role));
                //}

                // Ajouter les détails d'authentification
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Placer l'authentification dans le contexte
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Passer la requête au prochain filtre de la chaîne
        filterChain.doFilter(request, response);
    }
}
