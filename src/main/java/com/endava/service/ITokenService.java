package com.endava.service;

import org.json.simple.JSONObject;

public interface ITokenService {
	boolean checkToken(JSONObject data);
}
