package com.example.springratelimiterdemo;

import com.example.springratelimiterdemo.controller.RateLimitController;
import com.example.springratelimiterdemo.interceptor.RateLimitInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringRatelimiterDemoApplicationTests  {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testDevelopersAPIWithNoAPIKey() throws Exception {
		Map<String, String> map = new HashMap<>();
		// no valid api-key provided
		map.put("X-api-key", "");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
						.get("/api/v1/developers")
						.headers(httpHeaders)
						.contentType(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), 400);

	}

	@Test
	public void testDevelopersAPIWithInvalidAPIKey() throws Exception {
		Map<String, String> map = new HashMap<>();
		// invalid api-key provided => server sets the default rate limits applicable to the api.
		map.put("X-api-key", "Test");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		for(int i = 0; i < 40; i++){
			mockMvc.perform(MockMvcRequestBuilders
					.get("/api/v1/developers")
					.headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON)).andReturn();
		}
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/developers")
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), 429);

	}

	@Test
	public void testDevelopersAPIWithUser1APIKey() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("X-api-key", "USER1_DEV");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		for(int i = 0; i < 100; i++){
			mockMvc.perform(MockMvcRequestBuilders
					.get("/api/v1/developers")
					.headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON)).andReturn();
		}
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/developers")
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), 429);

	}

	@Test
	public void testDevelopersAPIWithUser2APIKey() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("X-api-key", "USER2_DEV");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		for(int i = 0; i < 50; i++){
			mockMvc.perform(MockMvcRequestBuilders
					.get("/api/v1/developers")
					.headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON)).andReturn();
		}
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/developers")
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), 429);

	}

	@Test
	public void testOrganizationAPIWithUser1APIKey() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("X-api-key", "USER1_ORG");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		for(int i = 0; i < 250; i++){
			mockMvc.perform(MockMvcRequestBuilders
					.get("/api/v1/oranizations")
					.headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON)).andReturn();
		}
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/organizations")
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), 429);

	}

	@Test
	public void testOrganizationAPIWithUser2APIKey() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("X-api-key", "USER2_ORG");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		for(int i = 0; i < 500; i++){
			mockMvc.perform(MockMvcRequestBuilders
					.get("/api/v1/organizations")
					.headers(httpHeaders)
					.contentType(MediaType.APPLICATION_JSON)).andReturn();
		}
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/organizations")
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), 429);

	}

}
