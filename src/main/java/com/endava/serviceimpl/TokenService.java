package com.endava.serviceimpl;

import java.util.Arrays;

import com.endava.provider.JwtTokenProvider;
import com.endava.service.ITokenService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;

@Service
@RefreshScope
public class TokenService implements ITokenService {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Value("#{'${auth.endpoints}'.split(',')}")
	private String[] excludes_urls;

	public boolean checkToken(JSONObject data) {
		String token = data.get("token").toString();
		String url = data.get("url").toString();

		if (token != null) {
			if (!jwtTokenProvider.isTokenPresentInDB(token)) {
				return false;// throw new RuntimeException("Invalid JWT token");
			}
			try {
				jwtTokenProvider.validateToken(token);
			} catch (JwtException | IllegalArgumentException e) {
				return false;// throw new RuntimeException("Invalid JWT token");
			}
		}

		if (url != null) {
			return !Arrays.stream(excludes_urls).anyMatch(x -> url.equals(x.toString()));
		}
		return true;
	}

}
