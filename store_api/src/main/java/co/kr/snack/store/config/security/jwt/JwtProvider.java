package co.kr.snack.store.config.security.jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import co.kr.snack.store.config.properties.SecurityProperties;
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
@Component
public class JwtProvider implements AuthenticationProvider {

    @Autowired
    private SecurityProperties jwtProperties;

    @Autowired
    private AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        //token validation
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtProperties.getSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        String id = jwsClaims.getBody().getId();
        String role = jwsClaims.getBody().getAudience();
        
//        log.debug("Claims-id:"  + id);
//        log.debug("Claims-subject:"  + subject);
//        log.debug("Claims:"  + new Gson().toJson(jwsClaims.getBody()));
        Account accountUser = accountService.get(id);
        if (!Optional.ofNullable(accountUser).isPresent()) {
            throw new AuthorizedException(ApplicationExceptionType.USER_INVALID);
        }

        log.debug("accountUser.getToken():"  + accountUser.getToken());
        log.debug("RawAccessJwtToken:"  + new Gson().toJson(rawAccessToken));
        if (!accountUser.getToken().equals(rawAccessToken.getToken().trim())) {
            throw new AuthorizedException(ApplicationExceptionType.OTHER_USER_LOGIN);
        }

        if (accountUser.getStatus() == AccountStatus.DEACTIVATION) {
            throw new AuthorizedException(ApplicationExceptionType.ACCOUNT_DEACTIVATION);
        }
        
        if (accountUser.getStatus() == AccountStatus.LOCK) {
            throw new AuthorizedException(ApplicationExceptionType.ACCOUNT_LOCK);
        }
        
        UserContext context = UserContext.builder().id(id).name(subject).role(role).build();
        log.debug("JwtProvider : authenticate");
        
        return new JwtAuthenticationToken(context, authentication.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
