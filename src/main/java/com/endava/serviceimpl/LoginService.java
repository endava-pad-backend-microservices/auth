package com.endava.serviceimpl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.endava.bean.JwtToken;
import com.endava.bean.SecurityConstants;
import com.endava.repository.JwtTokenRepository;
import com.endava.service.ILoginService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@Service
public class LoginService implements ILoginService {
	@Autowired
	private com.endava.provider.JwtTokenProvider jwtTokenProvider;
	@Autowired
	private JwtTokenRepository jwtTokenRepository;

	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public String login(String username, String password) {
		Application userApp = eurekaClient.getApplication(SecurityConstants.EUREKA_APP);
		InstanceInfo instanceInfo = userApp.getInstances().get(0);

		List<String> ms = Arrays.asList(SecurityConstants.PROT_CALL, String.valueOf(instanceInfo.getIPAddr()), ":",
				String.valueOf(instanceInfo.getPort()), SecurityConstants.GET_USER_ENDPOINT);

		String url = String.join("", ms);
                    
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject map = new JSONObject();
		map.put(SecurityConstants.KEY_USERNAME,username);
		map.put(SecurityConstants.KEY_PASSWORD, password);

		HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(map,headers);

		ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request, JSONObject.class);

		Boolean success = (Boolean) response.getBody().get(SecurityConstants.KEY_SUCCESS);
		if (success) {
			String jsonString = new JSONObject((LinkedHashMap) response.getBody().get(SecurityConstants.KEY_DATA)).toString();
			JSONParser parser = new JSONParser();
			JSONObject data = null;
			try {
				data = (JSONObject) parser.parse(jsonString);
			} catch (ParseException e) {

			}
			JSONArray roles = (data.get(SecurityConstants.KEY_ROLES)) != null ? (JSONArray) data.get(SecurityConstants.KEY_ROLES) : null;

			String userId = (data.get(SecurityConstants.KEY_ID)) != null ? String.valueOf((Long) data.get(SecurityConstants.KEY_ID))
					: "No userId detected";

			return roles != null && userId != null
					? jwtTokenProvider.createToken(username, userId.toString(), roles.get(0).toString())
					: "Role or userId invalid";
		} else {
			return (String) response.getBody().get(SecurityConstants.KEY_MESSAGE);
		}
	}

	@Override
	public boolean logout(String token) {
		try {
			jwtTokenRepository.delete(new JwtToken(token));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean isValidToken(String token) {
		return jwtTokenProvider.validateToken(token);
	}

}
