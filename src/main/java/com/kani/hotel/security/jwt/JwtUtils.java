package com.kani.hotel.security.jwt;

import com.kani.hotel.security.user.HotelUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${security.jwt.secret}")
    private String jwtSecret;
    @Value("${security.jwt.jwtExpirationTime}")
    private int jwtExpirationTime;

    public String generateJwtTokenForUser(Authentication authentication){
        HotelUserDetails hotelUserDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = hotelUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(hotelUserDetails.getUsername())
                .claim("role", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJwt(token).getBody().getSubject();
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        }catch (MalformedJwtException ex){
            logger.error("Invalid Jwt Token : {} ", ex.getMessage());
        }catch (ExpiredJwtException ex){
            logger.error("Expiration Token : {} ", ex.getMessage());
        }catch (UnsupportedJwtException ex){
            logger.error("This Token is not Supported : {} ", ex.getMessage());
        }catch (IllegalArgumentException ex){
            logger.error("No Claims Found : {} ", ex.getMessage());
        }
        return false;
    }
}
