package com.dub.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import brave.sampler.CountingSampler;
import brave.sampler.Sampler;

@EnableDiscoveryClient
@SpringBootApplication
public class Application {

	@Bean    
	public Sampler defaultSampler() {
		Sampler sampler = CountingSampler.create(1);
		return sampler;    
	}
	
	@Bean 
	public RestOperations restTemplate() {
		return new RestTemplate();
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

