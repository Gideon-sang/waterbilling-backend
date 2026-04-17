package com.estate.waterbilling.dto;

//── Login Response (what the backend sends back) ──
public class LoginResponse {
 private String token;
 private String username;

 public LoginResponse(String token, String username) {
     this.token = token;
     this.username = username;
 }

 public String getToken() { return token; }
 public String getUsername() { return username; }
}
