package com.obss.softwarecrafter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class LinkedinAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkedinAccountApplication.class, args);
    }
}
