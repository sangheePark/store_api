package co.kr.snack.store.config.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * <b></b>
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
public class LoginRequest {
    private String username;
    private String password;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
