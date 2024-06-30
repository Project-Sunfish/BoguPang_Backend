package com.kill.gaebokchi.domain.account.infra.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kill.gaebokchi.domain.account.dto.response.OAuthResponse;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.infra.OAuthApiClient;
import com.kill.gaebokchi.domain.account.infra.SocialType;
import com.kill.gaebokchi.global.error.AuthException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
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
    private static String keyPath;
    @Value("${social-login.provider.apple.client-id}")
    private String appleClientId;

    @Value("${social-login.provider.apple.key-path}")
    private void setKeyPath(String keyPath){
        AppleApiClient.keyPath=keyPath;
    }
    @Override
    public OAuthResponse requestOAuthInfo(String authorizationCode) {
        log.info("AppleApiClient");
        AppleTokenResponse appleAuthToken = generateAuthToken(authorizationCode);
        log.info("apple auth token : {}", appleAuthToken);
        String appleId = getAppleId(appleAuthToken.getIdToken());
        log.info("apple id : {}", appleId);
        return OAuthResponse.builder()
                .socialType(SocialType.APPLE)
                .refreshToken(appleAuthToken.getRefreshToken())
                .email(appleId + "@APPLE")
                .build();
    }

    public AppleTokenResponse generateAuthToken(String authorizationCode){
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
        ResponseEntity<AppleTokenResponse> response = restTemplate.postForEntity(url, httpEntity, AppleTokenResponse.class);
        return response.getBody();
    }
    public String createClientSecret(){
        log.info("call createClientSecret()");

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(appleKeyId).build();
        Date now = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(appleTeamId)
                .issueTime(now)
                .expirationTime(new Date(now.getTime()+3600000))
                .audience("https://appleid.apple.com")
                .subject(appleClientId)
                .build();

        SignedJWT jwt = new SignedJWT(header, claimsSet);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readPrivateKey());

        try{
            KeyFactory kf = KeyFactory.getInstance("EC");
            ECPrivateKey ecPrivateKey = (ECPrivateKey)kf.generatePrivate(spec);
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey);
            jwt.sign(jwsSigner);
        }catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }catch (JOSEException e) {
            e.printStackTrace();
        }

        return jwt.serialize();
    }

    //for creating client secret
    private static byte[] readPrivateKey(){
        log.info("call getPrivateKey()");
        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;
        try (InputStream keyInputStream = resource.getInputStream();
             InputStreamReader keyReader = new InputStreamReader(keyInputStream);
             PemReader pemReader = new PemReader(keyReader)) {
            PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    //create public key for verification of identity token
    private AppleKey getPublicKey(){
        log.info("call getPublicKey()");
        String url = SocialType.APPLE.getUrl();
        HttpMethod httpMethod = SocialType.APPLE.getHttpMethod();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<AppleKey> response = restTemplate.exchange(url, httpMethod, request, AppleKey.class);
        return response.getBody();
    }
    //verify identity token
    private String getAppleId(String identityToken){
        log.info("call getAppleId()");
        AppleKey response = getPublicKey();
        log.info("get public key");
        try{
            String headerToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerToken), StandardCharsets.UTF_8), Map.class);
            AppleKey.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg")).orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            //because n, e is encoded by base64 url-safe
            //so have to decode
            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();

            return claims.getSubject();
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new AuthException(ExceptionCode.APPLE_LOGIN_ERROR);
        }
    }

    public void revoke(Member member) throws IOException{
        String url = "https://appleid.apple.com/auth/revoke";
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleClientId);
        params.add("client_secret", createClientSecret());
        params.add("token", member.getRefreshToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        restTemplate.postForEntity(url, httpEntity, String.class);
    }
}
