package co.kr.snack.store.config.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import co.kr.snack.store.config.properties.SecurityProperties;
import co.kr.snack.store.config.security.jwt.token.RawAccessJwtToken;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <b></b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2020.02.06, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2020.02.06
 */
@Slf4j
public class JwtFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationFailureHandler failureHandler;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final SecurityProperties jwtProperties;

    @Autowired
    public JwtFilter(AuthenticationFailureHandler failureHandler, JwtTokenExtractor jwtTokenExtractor,
            RequestMatcher matcher, SecurityProperties jwtProperties) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.jwtTokenExtractor = jwtTokenExtractor;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        String tokenPayload = request.getHeader(jwtProperties.getHeader());
        RawAccessJwtToken token = new RawAccessJwtToken(jwtTokenExtractor.extract(tokenPayload));

        log.debug("RawAccessJwtToken: " + token.toString());
        log.debug("JwtFilter : attemptAuthentication");
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
//        log.debug("JWT:Authentication:" + new Gson().toJson(authResult));
        log.debug("JwtFilter : successfulAuthentication");
        
        /**
         * 초기화
         */
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
