package com.kiran.mstraining;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIGatewayConfiguration {

	//customize routes
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/get")
					.filters(f -> f.addRequestHeader("TestHeader", "TestURI")
							.addRequestParameter("Param", "SomeValue"))
					.uri("http://httpbin.org:80"))
				.route(p -> p.path("/currency-exchange/**")
						.uri("lb://currency-exchange"))
				.route(k -> k.path("/currency-conversion/**")
						.uri("lb://currency-conversion"))
				.route(k -> k.path("/currency-conversion-feign/**")
						.uri("lb://currency-conversion"))
				.route(k -> k.path("/currency-conversion-new/**")
						.filters( f-> f.rewritePath(
								"/currency-conversion-new/<?<segment>.*", 
								"/currency-conversion-feign/${segment}"))
						.uri("lb://currency-conversion"))
				.build();
	}
}
