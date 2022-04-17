package com.example.digitalcollege.security.jwt;

import com.example.digitalcollege.security.SecurityConstants;
import com.example.digitalcollege.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    public static final Logger LOG = LoggerFactory.getLogger(JwtProvider.class);

    public String generateToken(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireTime = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();

    }

    public boolean validateToken (String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOG.error("Invalid JWT signature: {}", e);
        } catch (MalformedJwtException e) {
            LOG.error("Invalid JWT token: {}", e);
        } catch (ExpiredJwtException e) {
            LOG.error("JWT token is expired: {}", e);
        } catch (UnsupportedJwtException e) {
            LOG.error("JWT token is unsupported: {}", e);
        } catch (IllegalArgumentException e) {
            LOG.error("JWT claims string is empty: {}", e);
        }

        return false;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();

        String id = (String) claims.get("id");

        return Long.parseLong(id);
    }
}
