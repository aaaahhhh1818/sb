package org.zerock.sb.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.sb.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class TokenCheckFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public TokenCheckFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("-------------TokenChekFilter--------------");

        //얘를 호출하는 경로 알아오기
        String path = request.getRequestURI(); //servlet 다시 필요함?

        log.info(path);

        if(path.startsWith("/api/")) { //이걸로 시작하면 전부 api 호출하는 것 -> 얘로 들어오면 token 체크 해주기
            //check token
            String authToken = request.getHeader("Authorization");

            if(authToken == null) { //야 너 토큰없어 토큰 갖고와
                log.info("authToken is null..............................");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;
            }

            //jwt 검사 (예외처리 되어있기 때문에 여기서도 예외처리 해줘야함)
            //맨 앞에 인증 타입 Bearer 토큰 짤라내야함
            String tokenStr = authToken.substring(7); //Bearer + 공백 -> 7

            try {
                jwtUtil.validateToken(tokenStr);
            }catch (ExpiredJwtException ex) { //유효기간 만료 됐을 때 예외 처리

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "EXPIRED API TOKEN.. TOO OLD";
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;

            }catch (JwtException jex) { //그냥 니 토큰 뭔가 이상함~ 예외처리

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "YOUR ACCESS TOKEN IS INVALID";
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;

            }

            filterChain.doFilter(request, response);

        }else {
            log.info("===========================TokenChekFilter===========================");
            //정상적인 애니까 다음단계로 진행하세요~ 하는 애
            filterChain.doFilter(request, response);
        }



    }
}
