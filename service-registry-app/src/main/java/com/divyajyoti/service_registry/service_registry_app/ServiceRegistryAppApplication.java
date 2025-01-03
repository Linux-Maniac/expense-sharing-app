package com.divyajyoti.service_registry.service_registry_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryAppApplication.class, args);
	}

}
