package com.endava.serviceimpl;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.endava.bean.SecurityConstants;
import com.endava.provider.JwtTokenProvider;
import com.endava.service.ITokenService;
import com.netflix.discovery.EurekaClient;

import io.jsonwebtoken.JwtException;

@Service
public class TokenService implements ITokenService {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	private RestTemplate restTemplate;

	public boolean checkToken(JSONObject data) {
		String token = data.get("token").toString();
		String url = data.get("url").toString();

		if (token != null) {
			if (!jwtTokenProvider.isTokenPresentInDB(token)) {
				return false;//throw new RuntimeException("Invalid JWT token");
			}
			try {
				jwtTokenProvider.validateToken(token);
			} catch (JwtException | IllegalArgumentException e) {
				return false;//throw new RuntimeException("Invalid JWT token");
			}
		}

		if (url != null) {
			if (StringUtils.endsWithAny(url, SecurityConstants.UNSECURED_URLS)) {
				return false;
			}
		}
		return true;
	}

}
