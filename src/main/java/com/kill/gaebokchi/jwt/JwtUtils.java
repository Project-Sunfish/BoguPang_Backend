package com.kill.gaebokchi.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtUtils {
    private static final String BEARER = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    private final Key key;

    public JwtUtils(@Value("${jwt.secret-key}")String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject("AccessToken") // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // 토큰 만료 시간 설정
                .claim("email",email)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken){
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer "+accessToken);
        log.info("in sendAccessToken, send : "+accessToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Optional<String> accessToken = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(at -> at.startsWith(BEARER))
                .map(at -> at.replace(BEARER, ""));
        log.info("extract AccessToken : "+accessToken);
        return accessToken;
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            // 토큰에서 클레임 추출
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);

            // EMAIL_CLAIM에 해당하는 값을 문자열로 추출
            String email = claimsJws.getBody().get("email", String.class);
            return Optional.ofNullable(email);
        }catch(Exception e) {
            return Optional.empty();
        }
    }
    public boolean validateToken(String token) {
        log.info("validateToken, token : "+token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
