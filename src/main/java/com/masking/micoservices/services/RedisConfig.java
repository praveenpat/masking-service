package com.masking.micoservices.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;





@Configuration
@ConfigurationProperties
@EnableRedisRepositories
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String REDIS_HOSTNAME;
	
	@Value("${spring.redis.port}")
	private int REDIS_PORT;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(REDIS_HOSTNAME, REDIS_PORT));
		  
	}
	
	
	@Bean
	public RedisTemplate<String, MaskedResult> redisTemplate() {
		RedisTemplate<String, MaskedResult> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}
	
}