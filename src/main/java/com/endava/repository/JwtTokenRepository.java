package com.endava.repository;

import com.endava.bean.JwtToken;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends MongoRepository<JwtToken,String> {
	JwtToken findByToken(String token);
}
