package com.endava.serviceimpl;

import java.util.Arrays;

import com.endava.bean.CheckTokenRequest;
import com.endava.provider.JwtTokenProvider;
import com.endava.service.ITokenService;

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

	public boolean checkToken(CheckTokenRequest data) {
		if (data.getToken() != null) {
			if (!jwtTokenProvider.isTokenPresentInDB(data.getToken())) {
				return false;// throw new RuntimeException("Invalid JWT token");
			}
			try {
				jwtTokenProvider.validateToken(data.getToken());
			} catch (JwtException | IllegalArgumentException e) {
				return false;// throw new RuntimeException("Invalid JWT token");
			}
		}

		if (data.getUrl() != null) {
			return !Arrays.stream(excludes_urls).anyMatch(x -> data.getUrl().equals(x.toString()));
		}
		return true;
	}

}
