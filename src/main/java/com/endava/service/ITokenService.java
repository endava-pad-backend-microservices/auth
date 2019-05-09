package com.endava.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.json.simple.JSONObject;

public interface ITokenService {
	boolean checkToken(JSONObject data);
}
