package com.dub.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin2.server.internal.EnableZipkinServer;

//@EnableZuulProxy
@SpringBootApplication
//@EnableDiscoveryClient
@EnableZipkinServer
public class Application {
 
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
