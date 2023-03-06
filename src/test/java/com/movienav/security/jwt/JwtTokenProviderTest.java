package com.movienav.security.jwt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JwtTokenProviderTest {

    @Autowired JwtTokenProvider jwtTokenProvider;
    @PersistenceContext EntityManager em;

    @Value("${jwt.secret}")
    private String secret;

    //refresh token 발급

    //access token 발급

    //get access token

    //get refresh token

    //send access token

    //send refresh token

    //validate token

    //get expiration

    //get username

    //get authentication

}