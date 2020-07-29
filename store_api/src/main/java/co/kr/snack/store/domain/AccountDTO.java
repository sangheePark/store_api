package co.kr.snack.store.domain;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import co.kr.snack.store.enums.AccountStatus;
import co.kr.snack.store.enums.AccountType;
import co.kr.snack.store.enums.ApplicationExceptionType;
import co.kr.snack.store.enums.AuthType;
import co.kr.snack.store.enums.YesNoType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <b>계정 관련 DTO</b>
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
@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class AccountDTO {

    public static final String DEFAULT_TOKEN = "99";
    /**
     * 시스템
     */
    public static final String DEFAULT_MEMBER_ID = "0";
    
    @Getter
    @NoArgsConstructor
    public static class Account {
        private String id;
        private String email;
        @JsonIgnore
        private String password;
        private String setPassword;
        private String name;
        private String companyName;
        private AuthType authType;
        private AccountType accountType;
        private AccountStatus status;
        private String title;
        private String memo;
        private String phone;
        private int loginFailCount;
        private String lastLoginTime;
        private String addTime;
        private String addMemberId;
        private String updateTime;
        private String updateMemberId;
        private String token;
        private String refreshToken;
        
        @Builder
        public Account(String id, String email, String name, String password, AuthType authType, AccountType accountType, AccountStatus status) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.password = password;
            this.authType = authType;
            this.accountType = accountType;
            this.status = status;
        }

        public boolean isLogin() {
            return AccountDTO.DEFAULT_TOKEN.equals(this.token);
        }
    }
    
    @Getter
    @NoArgsConstructor
    public static class LockAccount{
        private String id;
        private String updateMemberId;
        private String status;
        
        @Builder
        public LockAccount (String id) {
            this.id = id;
            this.updateMemberId = id;
            this.status = AccountStatus.LOCK.getCode();
        }
        
        public static LockAccount accountTo(Account account) {
            return LockAccount.builder().id(account.getId()).build();
        }
    }
    
    @Getter
    @NoArgsConstructor
    public static class LoginHistoryFilter {
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        private AuthType authType;
        private AccountType accountType;
    }
    
    @Getter
    @NoArgsConstructor
    public static class LoginHistory {
        private String rnum;
        private String memberId;
        private AccountType accountType;
        private AuthType authType;
        private String ip;
        private String time;
        private YesNoType isSuccess;
        private ApplicationExceptionType reason;
        
        @Builder
        public LoginHistory (String memberId, AuthType authType,  AccountType accountType, String ip, YesNoType isSuccess, ApplicationExceptionType reason) {
            this.memberId = memberId;
            this.authType = authType;
            this.accountType = accountType;
            this.ip = ip;
            this.isSuccess = isSuccess;
            this.reason = reason;
        }

        public static LoginHistory accountToHistory(Account accountUser, String ip, YesNoType isSuccess) {
            return LoginHistory.builder()
                    .memberId(accountUser.getId())
                    .authType(accountUser.getAuthType())
                    .accountType(accountUser.getAccountType())
                    .ip(ip)
                    .isSuccess(isSuccess)
                    .build();
        }
        
        public static LoginHistory accountToHistory(Account accountUser, String ip, YesNoType isSuccess, ApplicationExceptionType reason) {
            return LoginHistory.builder()
                    .memberId(accountUser.getEmail())
                    .accountType(accountUser.getAccountType())
                    .authType(accountUser.getAuthType())
                    .ip(ip)
                    .isSuccess(isSuccess)
                    .reason(reason)
                    .build();
        }
    }
    
    @Getter
    @NoArgsConstructor
    public static class Logout {
        @NotNull
        private String id;
        private String token = AccountDTO.DEFAULT_TOKEN;
        private String refreshToken = AccountDTO.DEFAULT_TOKEN;
        
        @Builder
        public Logout (String id) {
            this.id = id;
            this.token = AccountDTO.DEFAULT_TOKEN;
            this.refreshToken = AccountDTO.DEFAULT_TOKEN;
        }
        
        public static Map<String, String> toTokenParam (Logout logout){
            Map<String, String> param = Maps.newHashMap();
            param.put("id", logout.getId());
            param.put("token", logout.getToken());
            param.put("refreshToken", logout.getRefreshToken());
            
            return param;
        }
    }

    @ToString
    @Getter
    public static class AccountUser {
        private String id;
        private String email;
        private String name;
        private String setPassword;
        private AccountStatus status;
        private String companyName;
        private String phone;
        private String memo;
        private String title;
        private AuthType authType;
        private AccountType accountType;
        private List<Menu> menus;
        
        @Builder
        public AccountUser (String id, String email, String name, AccountStatus status, String setPassword, AuthType authType, AccountType accountType, String companyName, String phone, String memo, String title, List<Menu> menus) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.status = status;
            this.setPassword = setPassword;
            this.authType = authType;
            this.accountType = accountType;
            this.companyName = companyName;
            this.phone = phone;
            this.memo = memo;
            this.title = title;
            this.menus = menus;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Menu {
        private String id;
        private String parentId;
        private String code;
        private String name;
        private String type;
        private String url;
        private String iconUrl;
        private String method;
        private String sort;
        private String isUse;
    }
    
    
    
    
    
    @Getter
    @NoArgsConstructor
    public static class AccessLog {
        private String accessType;
        private String userId;
        private String menuId;
        private String url;
        
        @Builder
        public AccessLog (String userId, String menuId, String url) {
            this.accessType = "A";
            this.userId = userId;
            this.menuId = menuId;
            this.url = url;
        }
    }
    
    @Getter
    @NoArgsConstructor
    public static class PasswordUpdate {
        @NotEmpty
        private String id; 
        @NotEmpty
        @Setter
        private String password;
        @NotEmpty
        private String passwordConfirm;
        @NotEmpty
        private String oldPassword;
        @NotEmpty
        @Setter
        private String updateMemberId;
        
        public void decode() {
            this.oldPassword = new String(Base64.getDecoder().decode(this.oldPassword));
            this.password = new String(Base64.getDecoder().decode(this.password));
            this.passwordConfirm = new String(Base64.getDecoder().decode(this.passwordConfirm));
        }
    }
    
    @Data
    public static class SavePassword {
        private String id;
        private String password;
    }
    
    /////
    
    
    
    @Getter
    public static class Filter {
        @NotEmpty
        private String name; 
        @NotEmpty
        private String mdn;
    }
    
    @Getter
    @ToString
    public static class AuthFilter {
        /** 사용자 ID **/
        private String id;
        /** URI **/
        private String uri;
        /** 메서드 유형 **/
        private String method;
        
        @Builder
        public AuthFilter(String id, String uri, String method) {
            this.id = id;
            this.uri = uri;
            this.method = method;
        }

        public AuthFilter print() {
            log.debug(this.toString());
            return this;
        }
    }
    
    @Getter
    public static class Auth {
        /** 메뉴 ID **/
        private String menuId;
        /** URI **/
        private String uri;
    } 
}
