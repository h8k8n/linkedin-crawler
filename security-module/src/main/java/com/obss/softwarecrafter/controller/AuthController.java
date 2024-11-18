package com.obss.softwarecrafter.controller;


import com.obss.softwarecrafter.model.contract.AuthRequest;
import com.obss.softwarecrafter.model.contract.AuthResponse;
import com.obss.softwarecrafter.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/security")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("login called", request);
        return userDetailsService.findByUsername(request.getUsername())
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .map(userDetails -> new AuthResponse(jwtService.generateToken(userDetails), userDetails.getUsername()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }
}