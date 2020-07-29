package co.kr.snack.store.enums;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * <b>계정 상태 코드</b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.11.20, anseowls: 최초작성 
 * </pre>
 * @author anseowls (anseowls@gmail.com)
 * @Version 1.0, 2019.11.20
 */
@Getter
@RequiredArgsConstructor
public enum AccountStatus implements CodeEnum {
	
	NORMAL("1", "정상"),
	DEACTIVATION("2", "비활성화"),
    LOCK("3", "잠김");

    @NonNull
    @JsonValue
    private String code;
    @NonNull
    private String label;
    
    @MappedTypes(AccountStatus.class)
    public static class TypeHandler extends CodeEnumTypeHandler<AccountStatus>{
        public TypeHandler(){
            super(AccountStatus.class);
        }
    }

    @Override
    public CodeMap map() {
        return CodeMap.builder().code(this.code).label(this.label).build();
    }
    
    public static AccountStatus codeOf(String expression){
        return Arrays.stream(values())
                .filter(v -> expression.equals(v.code))
                .findFirst()
                .orElse(AccountStatus.NORMAL);
    }
}
