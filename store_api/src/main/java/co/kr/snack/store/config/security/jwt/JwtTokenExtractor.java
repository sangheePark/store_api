package co.kr.snack.store.config.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.spotv.adserver.enums.ApplicationExceptionType;

import net.spotv.adserver.config.exception.AuthorizedException;
import net.spotv.adserver.config.properties.SecurityProperties;

/**
 * 
 * <b>JWT Token Getting</b>
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
@Component
public class JwtTokenExtractor {

    @Autowired
    private SecurityProperties jwtProperties;

    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthorizedException(ApplicationExceptionType.HEADER_INVALID);
        }

        if (header.length() < jwtProperties.getPrefix().length()) {
            throw new AuthorizedException(ApplicationExceptionType.HEADER_INVALID);
        }

        return header.substring(jwtProperties.getPrefix().length(), header.length());
    }
}
