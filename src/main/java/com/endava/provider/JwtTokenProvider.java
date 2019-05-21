package com.endava.provider;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.bean.JwtToken;
import com.endava.repository.JwtTokenRepository;
import com.endava.service.ITokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	private static final String AUTH = "auth";
	private static final String AUTHORIZATION = "Authorization";
	private String secretKey = "secret-key";
	private long validityInMilliseconds = 3600000; // 1h

	@Autowired
	private JwtTokenRepository jwtTokenRepository;
	
	@Autowired
	private ITokenService iTokenService;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String username, String userId, String role) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH,role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        String token =  Jwts.builder()
        		.setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        
        jwtTokenRepository.save(new JwtToken(token,userId));
        return token;
    }

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader(AUTHORIZATION);
		
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) { 
			  return bearerToken.substring(7, bearerToken.length()); 
		}
		 
		if (bearerToken != null) {
			return bearerToken;
		}
		return null;
	}

	public boolean validateToken(String token) throws JwtException, IllegalArgumentException {
		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		return true;
	}

	public boolean isTokenPresentInDB(String token) {
		return jwtTokenRepository.findById(token).isPresent();
	}

	public List<String> getRoleList(String token) {
		return (List<String>) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(AUTH);
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

}
