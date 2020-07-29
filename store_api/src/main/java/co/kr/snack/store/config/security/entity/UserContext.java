package co.kr.snack.store.config.security.entity;

import co.kr.snack.store.domain.AccountDTO.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@ToString
public class UserContext {
    private String id;
    private String name;
    private String role;
    @Setter
    private String menuId;
    
    @Builder
    public UserContext(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static UserContext byAccount(Account account) {
        return UserContext.builder().id(account.getId()).name(account.getName()).role(account.getAuthType().getCode()).build();
    }
}
