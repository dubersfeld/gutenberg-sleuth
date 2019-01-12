package com.dub.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.dub.spring.controller.config.ServiceConfig;

import brave.sampler.CountingSampler;
import brave.sampler.Sampler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(Sink.class)
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);	
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		
		logger.debug("--Application started--");
	
	}
	
	@Autowired 
	private ServiceConfig serviceConfig;
		
	@Bean 
	public RestOperations restTemplate() {
		return new RestTemplate();
	}
		
	@Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnFactory 
        		= new JedisConnectionFactory(redisStandaloneConfig());       
        return jedisConnFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfig() {
    	RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
    	
    	redisStandaloneConfig.setHostName(serviceConfig.getRedisServer());
    	
    	redisStandaloneConfig.setPort(serviceConfig.getRedisPort());
    	    	
    	return redisStandaloneConfig;
    }
    
    @Bean    
	public Sampler defaultSampler() {
		Sampler sampler = CountingSampler.create(1);
		return sampler;    
	}
	
}

