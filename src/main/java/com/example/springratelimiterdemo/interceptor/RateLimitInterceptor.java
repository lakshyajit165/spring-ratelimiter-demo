package com.example.springratelimiterdemo.interceptor;

import com.example.springratelimiterdemo.payload.ApiResponse;
import com.example.springratelimiterdemo.service.RateLimitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_API_KEY = "X-api-key";
    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String apiKey = request.getHeader(HEADER_API_KEY);
        ObjectMapper mapper = new ObjectMapper();

        if (apiKey == null || apiKey.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(new ApiResponse("Missing header: " + HEADER_API_KEY)));
            return false;
        }

        Bucket tokenBucket = rateLimitService.resolveBucket(apiKey);

        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(new ApiResponse("You have exhausted your limit for this api! Please try after sometime")));
            return false;
        }
    }
}