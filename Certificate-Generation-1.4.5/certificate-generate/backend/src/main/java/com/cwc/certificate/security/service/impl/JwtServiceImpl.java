package com.cwc.certificate.security.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.security.service.JwtService;
import com.cwc.certificate.utility.IniConfigManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

//   @Deprecated
//    @Autowired
//    private IniConfigManager iniConfigManager;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() + ConstantValue.ACCESS_TOKEN_VALIDITY_SECONDS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
//        INIConfiguration jwtKey = iniConfigManager.getConfig("config");
        //byte[] keyBytes = Decoders.BASE64.decode(jwtKey.getString("jwt.singing"));//skip jwtSigningKey directly
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
