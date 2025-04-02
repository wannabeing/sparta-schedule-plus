package org.example.spartascheduleplus.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.spartascheduleplus.dto.api.ErrorResponseDto;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Order(1)
public class LoginFilter implements Filter {

    // ObjectMapper(Jackson 라이브러리)가 LocalDateTime 인식 가능하게 설정
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain
    ) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpRequest.getRequestURI(); // 사용자 요청 URI
        String method = httpRequest.getMethod(); // 사용자 요청 Method


        // 로그인이 필요한 URI 경우
        if(!isPermittedRequest(method, requestURI)){

            HttpSession session = httpRequest.getSession(false); // 기존 세션 불러오기

            if(session == null || session.getAttribute("loginUser") == null){
                // 에러 응답객체 생성
                ErrorResponseDto responseDto = new ErrorResponseDto(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        requestURI,
                        "로그인이 필요합니다.",
                        null
                );

                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 🚀 로그인 없이 접근 가능한지 확인하는 메서드
     * @param method 사용자 요청 메서드
     * @param uri 사용자 요청 URI
     * @return 유효할 경우 true 반환
     */
    private boolean isPermittedRequest(String method, String uri) {
        String[] permittedGetRequest = {"/", "/*", "/schedule", "/schedule/*", "/user/*"};
        String[] permittedPostRequest = {"/signup", "/user/login", "/user/logout"};

        // ✅ GET 요청
        if (method.equals("GET") && PatternMatchUtils.simpleMatch(
                permittedGetRequest, uri)) {
            return true;
        }

        // ✅ POST 요청 (아닐 경우, false 반환)
        return method.equals("POST") && PatternMatchUtils.simpleMatch(
                permittedPostRequest, uri);
    }
}
