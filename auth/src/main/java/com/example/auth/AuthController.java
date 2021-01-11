package com.example.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthController {

  List<User> users = new ArrayList<>();

  public AuthController(){
		users.add(User.builder().id(1).username("user1").password("P4ssword").role("user").build());
		users.add(User.builder().id(2).username("user2").password("P4ssword").role("admin").build());
  }
  
	@PostMapping("/api/1.0/auth")
	public ResponseEntity<?> auth(@RequestBody Credentials credentials) throws JsonProcessingException {
		Optional<User> optional = users.stream()
			.filter(u -> u.getUsername().equals(credentials.getUsername()) && u.getPassword().equals(credentials.getPassword())).findFirst();

		if(!optional.isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid creds"));
		
    User user = optional.get();
    
    Date expiresAt = new Date(System.currentTimeMillis() + 20*1000);

    String token = Jwts.builder().
			claim("username", user.getUsername()).
      claim("id", ""+user.getId()).
      signWith(SignatureAlgorithm.HS512, "my-app-secret").
      setExpiration(expiresAt).
      compact();

		return ResponseEntity.ok(Collections.singletonMap("token", token));
	}

}
