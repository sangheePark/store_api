package co.kr.snack.store.config.security.jwt.token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.kr.snack.store.config.properties.SecurityProperties;
import co.kr.snack.store.config.security.entity.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <b>JWT Token Factory</b>
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
@Component
public class JwtTokenFactory {

    @Autowired
    private SecurityProperties jwtProperties;

    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getId())) {
            throw new IllegalArgumentException("Cannot create JWT Token without id");
        }

        Claims claims = Jwts.claims()
                .setSubject(userContext.getName())
                .setId(userContext.getId());
        claims.setAudience(userContext.getRole());

        LocalDateTime currentTime = LocalDateTime.now();
        String token = Jwts.builder().setClaims(claims)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes((jwtProperties.getExpireTime() * jwtProperties.getTimeUnit())).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSigningKey()).compact();
//        log.debug("ACCESS_TOKEN_EXPIRE_TIME:" + (Date.from(currentTime.plusMinutes((jwtProperties.getExpireTime() * jwtProperties.getTimeUnit())).atZone(ZoneId.systemDefault()).toInstant())));
        log.debug("ACCESS_TOKEN_EXPIRE_TIME:" + (jwtProperties.getExpireTime() * jwtProperties.getTimeUnit()));
        
        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getId())) {
            throw new IllegalArgumentException("Cannot create JWT Token without id");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        Claims claims = Jwts.claims().setSubject(userContext.getName());
        claims.setId(userContext.getId());

        String token = Jwts.builder().setClaims(claims)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes((jwtProperties.getRefreshExpireTime() * jwtProperties.getTimeUnit())).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getRefreshSigningKey()).compact();

//        log.debug("REFRESH_TOKEN_EXPIRE_TIME:" + (jwtProperties.getRefreshExpireTime() * jwtProperties.getTimeUnit()));
        log.debug("REFRESH_TOKEN_EXPIRE_TIME:" + (Date.from(currentTime.plusMinutes((jwtProperties.getRefreshExpireTime() * jwtProperties.getTimeUnit())).atZone(ZoneId.systemDefault()).toInstant())));
        
        
        
        return new AccessJwtToken(token, claims);
    }
}
