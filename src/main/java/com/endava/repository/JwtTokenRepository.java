package com.endava.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.endava.bean.JwtToken;

@Repository
public interface JwtTokenRepository extends MongoRepository<JwtToken,String> {
	JwtToken findByToken(String token);
}
