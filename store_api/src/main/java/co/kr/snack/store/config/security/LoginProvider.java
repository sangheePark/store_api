package co.kr.snack.store.config.security;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import co.kr.snack.store.config.exception.AuthorizedException;
import co.kr.snack.store.config.properties.SecurityProperties;
import co.kr.snack.store.config.security.entity.UserContext;
import co.kr.snack.store.domain.AccountDTO;
import co.kr.snack.store.domain.AccountDTO.Account;
import co.kr.snack.store.enums.AccountStatus;
import co.kr.snack.store.enums.AccountType;
import co.kr.snack.store.enums.ApplicationExceptionType;
import co.kr.snack.store.enums.AuthType;
import co.kr.snack.store.enums.YesNoType;
import co.kr.snack.store.service.AccountService;
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
public class LoginProvider implements AuthenticationProvider {

    @Autowired
    private AccountService accountService;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private HttpServletRequest request;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String ip = Optional.ofNullable(request.getHeader("x-real-ip")).orElse("0:0:0:0:0:0:0:1");
        
        //비밀번호 없을 경우
        if (!Optional.ofNullable(password).isPresent()) {
            throw new AuthorizedException(ApplicationExceptionType.USER_INVALID); 
        }
        
        Account accountUser = accountService.getByEmail(email);
        //계정 정보 없을 경우
        if (!Optional.ofNullable(accountUser).isPresent()) {
            accountUser = Account.builder().email(email).authType(AuthType.ANONYMOUS).accountType(AccountType.ANONYMOUS).build();
//            accountService.addLoginHistory(AccountDTO.LoginHistory.accountToHistory(accountUser, ip, YesNoType.NO, ApplicationExceptionType.USER_INVALID));
            throw new AuthorizedException(ApplicationExceptionType.USER_INVALID);
        }

        //계정이 비활성 또는 잠김 
        if (accountUser.getStatus() != AccountStatus.NORMAL) {
            ApplicationExceptionType type = accountUser.getStatus() == AccountStatus.DEACTIVATION ? ApplicationExceptionType.ACCOUNT_DEACTIVATION : ApplicationExceptionType.ACCOUNT_LOCK;
//            accountService.addLoginHistory(AccountDTO.LoginHistory.accountToHistory(accountUser, ip, YesNoType.NO, type));
            throw new AuthorizedException(type);
        }
        
        //비밀번호 5회 이상 틀린 경우 계정 잠금
        if (accountUser.getLoginFailCount() >= securityProperties.getMaxErrorCount()){
//            accountService.addLoginHistory(AccountDTO.LoginHistory.accountToHistory(accountUser, ip, YesNoType.NO, ApplicationExceptionType.ACCOUNT_LOCK));
            accountService.lockAccount(accountUser);
            throw new AuthorizedException(ApplicationExceptionType.ACCOUNT_LOCK);
        }
        
        String decodePassword = new String(Base64.getDecoder().decode(password));
        log.debug("Base64.decode:" + decodePassword);
        
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(decodePassword);
        log.debug("encodePassword:" + encodePassword);
        
        //비밀 번호 틀린 경우
        if (!encoder.matches(decodePassword, accountUser.getPassword())) {
            accountService.savePasswordFailCount(accountUser.getId());
//            accountService.addLoginHistory(AccountDTO.LoginHistory.accountToHistory(accountUser, ip, YesNoType.NO, ApplicationExceptionType.USER_INVALID));
            throw new AuthorizedException(ApplicationExceptionType.USER_INVALID);
        }

        //계정에 권한 정보가 없을 경우
        if (null == accountUser.getAuthType()) {
//            accountService.addLoginHistory(AccountDTO.LoginHistory.accountToHistory(accountUser, ip, YesNoType.NO, ApplicationExceptionType.ACCESS_DENIED));
            throw new AuthorizedException(ApplicationExceptionType.ACCESS_DENIED);
        }
        
        List<GrantedAuthority> authorities = Lists.newArrayList(new SimpleGrantedAuthority(accountUser.getAuthType().getCode()));
        UserContext userContext = UserContext.byAccount(accountUser);
        log.debug("LoginProvider : authenticate");
        
        accountService.saveLoginTime(accountUser.getId());
//        accountService.addLoginHistory(AccountDTO.LoginHistory.accountToHistory(accountUser, ip, YesNoType.YES, ApplicationExceptionType.SUCCESS));
        return new UsernamePasswordAuthenticationToken(userContext, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
