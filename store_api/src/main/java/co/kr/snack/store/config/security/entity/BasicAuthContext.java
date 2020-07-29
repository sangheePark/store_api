package co.kr.snack.store.config.security.entity;

import co.kr.snack.store.domain.AccountDTO.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class BasicAuthContext {
    private String id;
    private String name;
    
    @Builder
    public BasicAuthContext(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BasicAuthContext byAccount(Account account) {
        return BasicAuthContext.builder().id(account.getId()).name(account.getName()).build();
    }
}
