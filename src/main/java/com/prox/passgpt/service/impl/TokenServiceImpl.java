package com.prox.passgpt.service.impl;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.prox.passgpt.service.AESService;
import com.prox.passgpt.service.SecurityService;
import com.prox.passgpt.service.TokenGptService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Consumer;

@Service
@Log4j2
public class TokenServiceImpl implements SecurityService {
    @Value("${token.expiration}")
    private long EXPIRATION_TIME;
    @Value("${key.secretKey}")
    private String secretKey;
    @Value("${key.expiration}")
    private long keyExpiration;

    @Autowired
    private AESService aesService;
    private Key KEY;

    @PostConstruct
    private void JwtService() {
        try {
            KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } catch (Exception e) {
            log.error(e);
        }
    }



    public String createJwt(String subject) {
        JwtBuilder jwt = Jwts.builder()
                .setSubject(subject)
                .signWith(KEY);
        jwt.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        return jwt.compact();
    }

    public String parseJwt(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public String getToken() {
        return aesService.encode(createJwt("Gpt" + System.currentTimeMillis()));
    }

    @Override
    public void parseToken(String token) throws JwtException {
        parseJwt(token);
    }

    @Override
    public String getKey(long timeStamps) {
        return hashHmacSha512(timeStamps, secretKey);
    }

    @Override
    public void parseKey(long timeStamps, String key) throws JwtException {
        String detailKey = hashHmacSha512(timeStamps, secretKey);
        if((System.currentTimeMillis() - timeStamps) > keyExpiration) throw new JwtException("Time stamps over");
        if(!key.equals(detailKey)) throw new JwtException("Key Error");
    }

    public static String hashHmacSha512(long valueToHash, String secretKey) {
        try {
            // Chuyển đổi giá trị long thành byte array
            byte[] valueBytes = String.valueOf(valueToHash).getBytes();
            // Khởi tạo Mac object với thuật toán HMAC-SHA512 và khóa bí mật
            Mac hmacSha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
            hmacSha512.init(keySpec);

            // Mã hóa giá trị và chuyển đổi sang dạng Base64
            byte[] hashBytes = hmacSha512.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e);
            return null;
        }
    }


}
