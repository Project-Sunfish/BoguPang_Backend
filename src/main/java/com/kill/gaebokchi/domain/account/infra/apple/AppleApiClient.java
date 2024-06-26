package com.kill.gaebokchi.domain.account.infra.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kill.gaebokchi.domain.account.dto.response.OAuthResponse;
import com.kill.gaebokchi.domain.account.infra.OAuthApiClient;
import com.kill.gaebokchi.domain.account.infra.SocialType;
import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import io.jsonwebtoken.*;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppleApiClient implements OAuthApiClient {
    private final RestTemplate restTemplate;
    @Value("${social-login.provider.apple.team-id}")
    private String appleTeamId;
    @Value("${social-login.provider.apple.key-id}")
    private String appleKeyId;
    @Value("${social-login.provider.apple.private-key}")
    private String applePrivateKey;
    @Value("${social-login.provider.apple.client-id}")
    private String appleClientId;

    @Override
    public OAuthResponse requestOAuthInfo(String authorizationCode) {
        log.info("AppleApiClient");
        AppleCodeResponse appleAuthToken = generateAuthToken(authorizationCode);
        log.info("apple auth token : {}", appleAuthToken);
        String appleId = getAppleId(appleAuthToken.getIdToken());
        log.info("apple id : {}", appleId);
        return OAuthResponse.builder()
                .socialType(SocialType.APPLE)
                .email(appleId + "@APPLE")
                .build();
    }

    public AppleCodeResponse generateAuthToken(String authorizationCode){
        log.info("call generateAuthToken()");
        String url = "https://appleid.apple.com/auth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", appleClientId);
        params.add("grant_type", "authorization_code");
        params.add("client_secret", createClientSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<AppleCodeResponse> response = restTemplate.postForEntity(url, httpEntity, AppleCodeResponse.class);
        return response.getBody();
    }
    public String createClientSecret(){
        log.info("call createClientSecret()");
        String authUrl = "https://appleid.apple.com/auth/token";
        Date expiredDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", appleKeyId);
        jwtHeader.put("alog", "ES256");

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiredDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(appleClientId)
                .signWith(SignatureAlgorithm.ES256,getPrivateKey())
                .compact();
    }
    private PrivateKey getPrivateKey(){
        log.info("call getPrivateKey()");

        try {
            String privateKey = applePrivateKey;
            Reader pemReader = new StringReader(privateKey);
            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            return converter.getPrivateKey(object);
        } catch (Exception e) {
            throw new BadRequestException(ExceptionCode.APPLE_LOGIN_ERROR);
        }
    }

    //public key 생성
    private AppleKey getPublicKey(){
        log.info("call getPublicKey()");
        String url = SocialType.APPLE.getUrl();
        HttpMethod httpMethod = SocialType.APPLE.getHttpMethod();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<AppleKey> response = restTemplate.exchange(url, httpMethod, request, AppleKey.class);
        return response.getBody();
    }
    //identity token 검증
    private String getAppleId(String identityToken){
        log.info("call getAppleId()");
        AppleKey response = getPublicKey();
        try{
            String headerToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerToken), StandardCharsets.UTF_8), Map.class);
            AppleKey.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg")).orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getN());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();

            return claims.getSubject();
        }catch(Exception e) {
            throw new BadRequestException(ExceptionCode.APPLE_LOGIN_ERROR);
        }
    }
}
