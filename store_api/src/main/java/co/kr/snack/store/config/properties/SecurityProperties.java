package co.kr.snack.store.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

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
@Configuration
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    /** JWT 유효 시간 **/
    private Integer expireTime;
    /** JWT 재발행 유효 시간 **/
    private Integer refreshExpireTime;
    /** JWT 유효 시간 단위 **/
    private Integer timeUnit;
    /** JWT 발생자 정보 **/
    private String issuer;
    /** JWT Signing Key **/
    private String signingKey;
    /** JWT Refresh Signing Key **/
    private String refreshSigningKey;
    /** JWT Header Key **/
    private String header;
    /** JWT Value Key **/
    private String prefix;
    /** Basic Auth Value Key **/
    private String basicPrefix;
    /** JWT 발행 URL **/
    private String loginUrl;
    /** JWT 재발행 URL **/
    private String refreshTokenUrl;
    /** API Authetication URL **/
    private String rootUrl;
    /** Authetication Ignore URL **/
    private String ignoreUrl;
    private int maxErrorCount;
    /** 로그인 사용 여부 **/
    private String useLogin;
    /** 접근 제어 사용 여부 **/
    private String useAccessDenied;
    /** 공통 BASE URI **/
    private String commonUri;
}
