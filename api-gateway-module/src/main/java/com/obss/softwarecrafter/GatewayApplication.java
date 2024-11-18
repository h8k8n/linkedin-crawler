package com.obss.softwarecrafter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/swagger-ui/crawling/v3/api-docs").and().method(HttpMethod.GET).uri("lb://crawler-module"))
				.route(r -> r.path("/swagger-ui/linkedin-profile/v3/api-docs").and().method(HttpMethod.GET).uri("lb://information-extraction-module"))
				.route(r -> r.path("/swagger-ui/linkedin-account/v3/api-docs").and().method(HttpMethod.GET).uri("lb://linkedin-account-module"))
				.route(r -> r.path("/swagger-ui/proxy-server/v3/api-docs").and().method(HttpMethod.GET).uri("lb://proxy-server-module"))
				.route(r -> r.path("/swagger-ui/security/v3/api-docs").and().method(HttpMethod.GET).uri("lb://security-module"))
				.build();
	}

}