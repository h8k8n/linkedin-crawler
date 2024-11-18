package com.obss.softwarecrafter.service;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapUserDetailsService implements ReactiveUserDetailsService {

    private final LdapTemplate ldapTemplate;

    public LdapUserDetailsService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        try {
            LdapQuery query = query()
                    .base("ou=people")
                    .where("uid").is(username);


            DirContextOperations user = ldapTemplate.searchForContext(query);

            Object passwordValue = user.getObjectAttribute("userPassword");
            String password;

            if (passwordValue instanceof byte[] passwordBytes) {
                password = new String(passwordBytes, StandardCharsets.UTF_8);
            } else {
                password = (String) passwordValue;
            }


            return Mono.just(new User(
                    username,
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}