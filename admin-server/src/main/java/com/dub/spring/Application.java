package com.dub.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;

import brave.sampler.CountingSampler;
import brave.sampler.Sampler;


@SpringBootApplication
@EnableBinding(Source.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean    
	public Sampler defaultSampler() {
		Sampler sampler = CountingSampler.create(1);
		return sampler;    
	}
	
}
