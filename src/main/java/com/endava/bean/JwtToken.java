package com.endava.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "token")
public class JwtToken {
	@Id
	private String token;

	private String userId;

	public String getUserID() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JwtToken(String token) {
		this.token = token;
	}

	public JwtToken() {
	}

	public JwtToken(String token, String userId) {
		this.userId = userId;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
