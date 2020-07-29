package co.kr.snack.store.config.security.entity;

import co.kr.snack.store.enums.AuthType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
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
 * - 2019.11.05, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.11.05
 */
@Getter
@ToString
@Slf4j
public class Requester {
    private String id;
    private String name;
    private String ip;
    private String systemIp;
    private String menuId;
    private AuthType role;
    
    @Builder
    public Requester (String id, String name, String ip, String systemIp, String menuId, AuthType role) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.systemIp = systemIp;
        this.menuId = menuId;
        this.role = role;
    }
    
    public Requester print() {
        log.debug("REQUEST:" + this.toString());
        return this;
    }
}
