package co.kr.snack.store.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.common.collect.Lists;

import co.kr.snack.store.config.properties.SecurityProperties;
import co.kr.snack.store.config.security.CorsFilterImpl;
import co.kr.snack.store.config.security.LoginFilter;
import co.kr.snack.store.config.security.LoginProvider;
import co.kr.snack.store.config.security.jwt.JwtFilter;
import co.kr.snack.store.config.security.jwt.JwtProvider;
import co.kr.snack.store.config.security.jwt.JwtTokenExtractor;
import co.kr.snack.store.enums.YesNoType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    private LoginProvider loginProvider;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtTokenExtractor jwtTokenExtractor;

    @Autowired
    private SecurityProperties securityProperties;
    
    @Value("${spring.profiles.active}")
    private String active;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<String> defaultPermitAllEndpointList = Arrays.asList(
                securityProperties.getLoginUrl()
                , securityProperties.getRefreshTokenUrl());
        
        List<String> apiEndPointList = Arrays.asList(securityProperties.getRootUrl());
        if (null == securityProperties.getUseLogin() || securityProperties.getUseLogin().equals(YesNoType.NO.getCode())) {
            apiEndPointList = Arrays.asList(securityProperties.getIgnoreUrl());
        }

        List<String> permitAllEndpointList = Lists.newArrayList(defaultPermitAllEndpointList);
        
        http
                .csrf().disable() // We don't need CSRF for JWT based authentication
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()])).permitAll()
                .and()
                .authorizeRequests().antMatchers(apiEndPointList.toArray(new String[apiEndPointList.size()])).authenticated() // Protected API End-points
                .and()
                .addFilter(new CorsFilterImpl())
                .addFilterBefore(
                        buildLoginFilter(securityProperties.getLoginUrl()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        buildJwtFilter(permitAllEndpointList, apiEndPointList),
                        UsernamePasswordAuthenticationFilter.class);
        
        if (null != securityProperties.getUseAccessDenied() && securityProperties.getUseAccessDenied().equals(YesNoType.YES.getCode())) {
            http
                    .authorizeRequests()
                    .antMatchers(apiEndPointList.toArray(new String[apiEndPointList.size()]))
                    .authenticated()
                    .accessDecisionManager(accessDecisionManager())
                    .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(loginProvider);
        auth.authenticationProvider(jwtProvider);
    }
    
    @Override
    public void configure(WebSecurity webSecurity) {
        if (null == securityProperties.getUseLogin() || securityProperties.getUseLogin().equals(YesNoType.NO.getCode())) {
            log.error("Security OFF::" + active);
            webSecurity.ignoring().antMatchers(securityProperties.getIgnoreUrl());
        }
    }

    protected LoginFilter buildLoginFilter(String loginEntryPoint) throws Exception {
        LoginFilter filter = new LoginFilter(loginEntryPoint, successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    protected JwtFilter buildJwtFilter(List<String> pathsToSkip, List<String> pattern) throws Exception {
        UrlMatcher matcher = new UrlMatcher(pathsToSkip, pattern);
        JwtFilter filter = new JwtFilter(failureHandler, jwtTokenExtractor, matcher, securityProperties);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(
            new WebExpressionVoter(),
            new RoleVoter(),
            new AuthenticatedVoter());
        
        /**
         * 로그인 사용시에만 사용
         */
        if (null != securityProperties.getUseLogin() && securityProperties.getUseLogin().equals(YesNoType.YES.getCode())) {
            decisionVoters = Arrays.asList(
                    new WebExpressionVoter(),
                    new RoleVoter(),
                    new AuthenticatedVoter(), methodVoter);
        }
        
        return new UnanimousBased(decisionVoters);
    }
}