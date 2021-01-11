package com.example.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
  
  long id;
  
  String username;
  
  String password;
  
  String role;
  
}