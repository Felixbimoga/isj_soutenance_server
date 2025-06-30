package com.EasayHelp.EasayHelp.configuration;

import com.EasayHelp.EasayHelp.filter.JwtFilter;
import com.EasayHelp.EasayHelp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                "/api/easayHelp/auth/register/admin",
                                        "/api/easayHelp/auth/register/client",
                                        "/api/easayHelp/auth/register/technicien",
                                        "/api/easayHelp/auth/login",
                                        "/api/easayHelp/auth/{id}/photo",
                                        "/api/easayHelp/auth/logout",
                                        "/api/easayHelp/auth/users",
                                        "/api/easayHelp/auth/role/{role}",
                                        "/api/easayHelp/auth/utilisateur/{id}",
                                        "/api/easayHelp/auth/update/{id}",
                                        "/api/easayHelp/auth/{id}/unblock",
                                        "/api/easayHelp/auth/{id}/block",
                                        "/api/easayHelp/auth/techniciens/service/{id}",

                                        //Pour les categories
                                        "/api/easayHelp/create-categorie",
                                        "/api/easayHelp/categorie/{id}/image",
                                        "/api/easayHelp/categorie/supprimer/{id}",
                                        "/api/easayHelp/categories",
                                        "/api/easayHelp/categorie/{categorieId}/services",
                                        "/api/easayHelp/categorie/updateCategories/{id}",

                                        //Pour les services
                                        "/api/easayHelp/service/create-service",
                                        "/api/easayHelp/service/{serviceId}/techniciens",
                                        "/api/easayHelp/services",
                                        "/api/easayHelp/service/{serviceId}",
                                        "/api/easayHelp/service/supprimer/{id}",
                                        "/api/easayHelp/service/{id}/image",
                                        "/api/easayHelp/service/{serviceId}/statut",
                                        "/api/easayHelp/service/statut/{statut}",
                                        "/api/easayHelp/service/{serviceId}/create-technicien",
                                        "/api/easayHelp/service/updateServices/{id}",

                                        "/api/easayHelp/techniciens",
                                        "/api/easayHelp/technicien/supprimer/{id}",
                                        "/api/easayHelp/technicien/{id}/image",

                                        //Pour les commandes
                                        "/api/easayHelp/commande/create-commande",
                                        "/api/easayHelp/commande/commandes",
                                        "/api/easayHelp/commande/commandes/{id}",
                                        "/api/easayHelp/commande/supprimer/{id}",
                                        "/api/easayHelp/commande/getcommandes/user/{userId}",
                                        "/api/easayHelp/commande/getcommandes/technicien/{technicienId}",
                                        "/api/easayHelp/commande/{commandeId}/assigner/{technicienId}",
                                        "/api/easayHelp/commande/getcommandes/service/{serviceId}",
                                        "/api/easayHelp/commande/updateCommande/{commandeId}",
                                        "/api/easayHelp/commande/{commandeId}/statut",

                                        //Pour les feedbacks
                                        "/api/easayHelp/feedback/{commandeId}/create-feedback",
                                        "/api/easayHelp/feedback/feedbacks",
                                        "/api/easayHelp/feedback/supprimer/{id}",
                                        "/api/easayHelp/feedback/updateFeedback/{id}",

                                        //Pour les Reclamations
                                        "/api/easayHelp/reclamation/{commandeId}/create-reclamation",
                                        "/api/easayHelp/reclamation/reclamations",
                                        "/api/easayHelp/reclamation/supprimer/{id}",
                                        "/api/easayHelp/reclamation/updateFeedback/{id}",

                                        "/api/easayHelp/contact",

                                        //villes
                                        "/api/easayHelp/ville",
                                        "/api/easayHelp/ville/villes/{id}",
                                        "/api/easayHelp/ville/search",
                                        "/api/easayHelp/ville/by-region/{id}",
                                        "/api/easayHelp/ville/update/{id}",
                                        "/api/easayHelp/ville/delete/{id}",

                                        //regions
                                        "/api/easayHelp/region",
                                        "/api/easayHelp/region/regions/{id}",
                                        "/api/easayHelp/region/search",
                                        "/api/easayHelp/region/update/{id}",
                                        "/api/easayHelp/region/delete/{id}"

                                        ).permitAll()
                                .requestMatchers("/api/v1/users").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(userDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }
}