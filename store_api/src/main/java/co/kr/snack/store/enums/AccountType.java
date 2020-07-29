package co.kr.snack.store.enums;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * <b>계정 유형</b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.11.21, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.11.21
 */
@Getter
@RequiredArgsConstructor
public enum AccountType implements CodeEnum {
	
	ADMIN("A", "Admin 계정"),
	EXTERNAL("E", "외부 계정"),
    ANONYMOUS("N", "N/A");
    
    @NonNull
    @JsonValue
    private String code;
    @NonNull
    private String label;
    
    @MappedTypes(AccountType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<AccountType>{
        public TypeHandler(){
            super(AccountType.class);
        }
    }

    @Override
    public CodeMap map() {
        return CodeMap.builder().code(this.code).label(this.label).build();
    }
}
