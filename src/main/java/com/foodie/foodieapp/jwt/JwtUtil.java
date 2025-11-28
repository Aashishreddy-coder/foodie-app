package com.foodie.foodieapp.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import com.foodie.foodieapp.domain.AppUser;



@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;






    public String generateToken(AppUser user){
        String jwtToken = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        jwtToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
        return jwtToken;
    }

    public Claims validateToken(String token) throws Exception {
        try{
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        }catch(Exception e){
            throw new Exception("Invalid token");
        }
    }
    

}
