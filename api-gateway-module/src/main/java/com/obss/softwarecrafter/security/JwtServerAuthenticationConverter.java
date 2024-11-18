package com.obss.softwarecrafter.security;

import com.obss.softwarecrafter.model.JwtToken;
import com.obss.softwarecrafter.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtUtil jwtService;
    private static final String BEARER = "Bearer ";

    @Value("${gateway.auth.token}")
    private String gatewayToken;

    JwtServerAuthenticationConverter(JwtUtil jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        if(exchange.getRequest().getHeaders().containsKey("X-Gateway-Auth") && gatewayToken.equals(exchange.getRequest().getHeaders().getFirst("X-Gateway-Auth"))) {
            JwtToken jwtToken = new JwtToken(null, User.builder()
                    .username("microservice")
                    .password("microservice")
                    .authorities(new SimpleGrantedAuthority("USER_ROLE")).build());
            jwtToken.setAuthenticated(true);
            return Mono.just(jwtToken);
        }
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.substring(BEARER.length()))
                .map(token -> new JwtToken(token, createUserDetails(token)));
    }

    private UserDetails createUserDetails(String token) {
        String username = jwtService.getUsernameFromToken(token);
        return User.builder()
                .username(username)
                .authorities(createAuthorities(token))
                .password("")
                .build();
    }

    private List<SimpleGrantedAuthority> createAuthorities(String token) {
        return jwtService.extractRoles(token).stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}