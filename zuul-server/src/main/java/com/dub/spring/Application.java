package com.dub.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import brave.sampler.CountingSampler;
import brave.sampler.Sampler;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class Application {
	
	@LoadBalanced
	@Bean   
	public RestTemplate getRestTemplate() {    
		return new RestTemplate();   
	}

	@Bean    
	public Sampler defaultSampler() {
		Sampler sampler = CountingSampler.create(1);
		return sampler;    
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

