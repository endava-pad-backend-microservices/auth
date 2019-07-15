package com.endava.service;

import com.endava.bean.CheckTokenRequest;

public interface ITokenService {
	boolean checkToken(CheckTokenRequest data);
}
