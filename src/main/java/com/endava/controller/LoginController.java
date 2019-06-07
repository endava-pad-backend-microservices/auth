package com.endava.controller;

import com.endava.bean.LoginRequest;
import com.endava.service.ILoginService;
import com.endava.service.ITokenService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class LoginController {

	@Autowired
	private ILoginService iLoginService;

	@Autowired
	private ITokenService iTokenService;
	
	@PostMapping("/signin")
	@ResponseBody
	public String login(@RequestBody LoginRequest loginRequest) {
		return iLoginService.login(loginRequest.getUsername(), loginRequest.getPassword());
	}

	@PostMapping("/signout")
	@ResponseBody
	public void logout(@RequestHeader(value = "Authorization") String token) {
		iLoginService.logout(token);
	}

	@PostMapping("/checkToken")
	@ResponseBody
	public boolean checkToken(@RequestBody JSONObject data) {
		return iTokenService.checkToken(data);
	}
}
