package com.wetube.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class AuthApplication {

	public static void main(String[] args)
    {
		SpringApplication.run(AuthApplication.class, args);
        System.out.println("**prueba de diagnostico: log V2");
	}

}
