package co.kr.snack.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import co.kr.snack.store.config.exception.AuthorizedException;
import co.kr.snack.store.config.exception.WebBadRequestException;
import co.kr.snack.store.config.properties.SecurityProperties;
import co.kr.snack.store.config.security.entity.Requester;
import co.kr.snack.store.config.security.entity.UserContext;
import co.kr.snack.store.config.security.jwt.token.JwtToken;
import co.kr.snack.store.config.security.jwt.token.JwtTokenFactory;
import co.kr.snack.store.domain.AccountDTO;
import co.kr.snack.store.domain.AccountDTO.AccessLog;
import co.kr.snack.store.domain.AccountDTO.Account;
import co.kr.snack.store.domain.AccountDTO.AccountUser;
import co.kr.snack.store.domain.AccountDTO.Auth;
import co.kr.snack.store.domain.AccountDTO.AuthFilter;
import co.kr.snack.store.domain.AccountDTO.Menu;
import co.kr.snack.store.domain.AccountDTO.SavePassword;
import co.kr.snack.store.domain.PageResult;
import co.kr.snack.store.domain.PageSearch;
import co.kr.snack.store.enums.ApplicationExceptionType;
import io.jsonwebtoken.Jwts;
//import net.spotv.adserver.config.exception.AuthorizedException;
//import net.spotv.adserver.config.exception.WebBadRequestException;
//import net.spotv.adserver.config.properties.SecurityProperties;
//import net.spotv.adserver.config.security.entity.Requester;
//import net.spotv.adserver.config.security.entity.UserContext;
//import net.spotv.adserver.config.security.jwt.token.JwtToken;
//import net.spotv.adserver.config.security.jwt.token.JwtTokenFactory;
//import net.spotv.adserver.domain.AccountDTO;
//import net.spotv.adserver.domain.PageResult;
//import net.spotv.adserver.domain.AccountDTO.*;
//import net.spotv.adserver.enums.ApplicationExceptionType;
//import net.spotv.adserver.domain.PageSearch;
//import net.spotv.adserver.mapper.AccountMapper;

/**
 * 
 * <b>Account Service</b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.11.05, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.11.05
 */
@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;
    
    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private SecurityProperties jwtProperties;

    /**
     * 계정 검색 
     * @param id
     * @return
     */
    public Account get(String id) {
        return accountMapper.findById(id);
    }
    
    /**
     * 계정 검색 (email)
     * @param id
     * @return
     */
    public Account getByEmail(String email) {
        return accountMapper.findByEmail(email);
    }

    /**
     * 로그인 시간 저장
     * @param id
     */
    public void saveLoginTime(String id) {
        accountMapper.saveLoginTime(id);
    }

    /**
     * 로그인 이력 등록
     * @param model
     */
    public void addLoginHistory(LoginHistory model) {
        accountMapper.addLoginHistory(model);
    }

    /**
     * 로그인 실패 Count 
     * @param id
     */
    public void savePasswordFailCount(String id) {
        accountMapper.savePasswordFailCount(id);
    }
    
    /**
     * 로그인 인증 토큰 저장
     * @param param
     */
    public void saveToken(Map<String, String> param) {
        accountMapper.saveToken(param);
    }

    /**
     * 로그아웃 
     * @param param
     */
    public void logout(Map<String, String> param) {
        accountMapper.saveToken(param);
    }
    
    /**
     * 로그인 사용자 정보
     * @param user
     * @return
     */
    public AccountUser account(Requester user) {
        Account findAccount = accountMapper.findById(user.getId());
        if (null == findAccount) {
            throw new WebBadRequestException("아이디가 올바르지 않습니다. 다시 입력해주세요.");
        }
        
        List<Menu> menuList = accountMapper.findMenuByAuthId(findAccount.getAuthType().getCode());
        return AccountUser.builder()
                .id(user.getId())
                .name(user.getName())
                .email(findAccount.getEmail())
                .title(findAccount.getTitle())
                .memo(findAccount.getMemo())
                .phone(findAccount.getPhone())
                .companyName(findAccount.getCompanyName())
                .menus(menuList)
                .setPassword(findAccount.getSetPassword())
                .status(findAccount.getStatus())
                .authType(findAccount.getAuthType())
                .accountType(findAccount.getAccountType())
                .build();
    }
    
    /**
     * 로그인 이력 조회
     * @param filter
     * @return
     */
    public PageResult<LoginHistory> loginHistory(PageSearch<LoginHistoryFilter> filter) {
        return new PageResult<LoginHistory>(accountMapper.countLoginHistory(filter), accountMapper.findLoginHistory(filter), filter);
    }

    /**
     * 비밀번호 변경
     * @param model
     */
    public void savePassword(@Valid PasswordUpdate model) {
        Account findAccount = accountMapper.findById(model.getId());
        if (null == findAccount) {
            throw new WebBadRequestException("account.NOT_FOUND");
        }
        
        model.decode();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(model.getOldPassword(), findAccount.getPassword())) {
            throw new WebBadRequestException("account.IS_NOT_MATCH_PASSWORD");
        }
        if (!model.getPassword().equals(model.getPasswordConfirm())) {
            throw new WebBadRequestException("account.IS_NOT_MATCH_NEW_PASSWORD");
        }
        
        String newPassword = encoder.encode(model.getPassword());
        model.setPassword(newPassword);
        accountMapper.savePassword(model);
    }
    
    /**
     * 임시비밀번호 상태 변경
     * @param model
     * @return
     */
    public void saveSetPassword(SavePassword model) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        Account findAccount = accountMapper.findById(model.getId());
        if (encoder.matches(model.getPassword(), findAccount.getPassword())) {
            throw new WebBadRequestException("account.NEW_PASSWORD");
        }
        
        String encodePassword = encoder.encode(model.getPassword());
        model.setPassword(encodePassword);
        
        accountMapper.saveSetPassword(model);
    } 

    /**
     * 토큰 재발급
     * @param id
     * @param refreshToken
     * @return
     */
    public Map<String, String> refreshToken(Account account) {
        Account findAccount = accountMapper.findById(account.getId());
        if (null == findAccount) {
//            throw new WebBadRequestException("account.NOT_FOUND");
            throw new AuthorizedException(ApplicationExceptionType.REFRESH_TOKEN_EXPIRED);
        }
        
        try {
            Jwts.parser().setSigningKey(jwtProperties.getRefreshSigningKey()).parseClaimsJws(account.getRefreshToken());
        } catch (Exception e) {

            Map<String, String> param = Maps.newHashMap();
            param.put("id", account.getId());
            param.put("token", AccountDTO.DEFAULT_TOKEN);
            param.put("refreshToken", AccountDTO.DEFAULT_TOKEN);
            accountMapper.saveToken(param);
            e.printStackTrace();
            throw new AuthorizedException(ApplicationExceptionType.REFRESH_TOKEN_EXPIRED);
        }
        
        UserContext context = UserContext.builder().id(account.getId()).name(findAccount.getName()).role(findAccount.getAuthType().getCode()).build();
        JwtToken newAccessToken = tokenFactory.createAccessJwtToken(context);
        JwtToken newRefreshToken = tokenFactory.createRefreshToken(context);
        
        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", newAccessToken.getToken());
        tokenMap.put("refreshToken", newRefreshToken.getToken());
        tokenMap.put("id", account.getId());
        this.saveToken(tokenMap);
        return tokenMap;
    }
    
    /**
     * 권한 목록
     * @param filter
     * @return
     */
    public List<Auth> getAuth(AuthFilter filter) {
        return accountMapper.findAuth(filter);
    }

    /**
     * 계정 상태 변경
     * @param accountUser
     */
    public void lockAccount(Account accountUser) {
        accountMapper.lockAccount(LockAccount.accountTo(accountUser));
    }

    
    
  //올드  
    
    
    
    public void addLog(AccessLog model) {
        accountMapper.addLog(model);
    }
}
