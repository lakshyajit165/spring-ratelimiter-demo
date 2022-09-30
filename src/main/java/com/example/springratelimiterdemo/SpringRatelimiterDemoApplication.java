package com.example.springratelimiterdemo;

import com.example.springratelimiterdemo.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringRatelimiterDemoApplication implements WebMvcConfigurer {

	@Autowired
	@Lazy
	private RateLimitInterceptor rateLimitInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(rateLimitInterceptor)
				.addPathPatterns("/api/v1/**");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringRatelimiterDemoApplication.class, args);
	}

}
