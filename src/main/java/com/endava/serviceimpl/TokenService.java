package com.endava.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.endava.service.ITokenService;
import com.netflix.discovery.EurekaClient;

import io.jsonwebtoken.JwtException;

@Service
public class TokenService implements ITokenService {

	private com.endava.provider.JwtTokenProvider jwtTokenProvider;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	private RestTemplate restTemplate;

	public void checkToken(String token) {
		if (token != null) {
			if (!jwtTokenProvider.isTokenPresentInDB(token)) {
				throw new RuntimeException("Invalid JWT token");
			}
			try {
				jwtTokenProvider.validateToken(token);
			} catch (JwtException | IllegalArgumentException e) {
				throw new RuntimeException("Invalid JWT token");
			}
		}
	}

}
