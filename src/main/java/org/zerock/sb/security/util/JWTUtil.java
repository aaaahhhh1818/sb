package org.zerock.sb.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private final static String secretKey = "helloworld111112222233333333344444444444555555555"; //얘를 암호화 시킬거임

    //key 암호화 작업
    private SecretKey key;

    public JWTUtil() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //Token generate 시켜주는 애
    public String generateToken(String content) {

        long timeAmount = 2; //토큰 유효기간 설정

        //JWT 생성
        String jws = Jwts.builder() // (1)

                .setSubject(content)      // (2)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(timeAmount).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)          // (3)
                .compact();             // (4)

        return jws;
    }

    //JWT 체크 (예외 처리해서)
    public void validateToken(String token) throws JwtException {
        Jws<Claims> jws = Jwts.parserBuilder()  // (1)
                .setSigningKey(key)         // (2)
                .build()                    // (3)
                .parseClaimsJws(token); // (4)

    }

}
