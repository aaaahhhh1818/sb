package org.zerock.sb.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.security.util.JWTUtil;

@SpringBootTest
@Log4j2
public class JWTTests {

    @Autowired
    JWTUtil jwtUtil;

    @Test
    public void testGenerate() {

        String jwtStr = jwtUtil.generateToken("user11");

        log.info(jwtStr);

    }

    @Test
    public void testValidate() {

        String str = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE2MzQ4NzA0NTAsImV4cCI6MTYzNDg3MDQ1MH0.-v6ht05zJj1tniTBEDjeEl2WbxUBvGiT7rdqGPc_RFo";

        try {
            jwtUtil.validateToken(str);
        }catch (ExpiredJwtException ex) {
            log.error("expired......................");
            log.error(ex.getMessage());
        }catch (JwtException ex) {
            log.error(ex.getMessage());
        }
    }

}
