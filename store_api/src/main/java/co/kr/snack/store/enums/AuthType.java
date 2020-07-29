package co.kr.snack.store.enums;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * <b>권한 코드</b>
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
@RequiredArgsConstructor
public enum AuthType implements CodeEnum {
    SUPER_ADMIN("00", "Super Admin"), 
    ADMIN("02", "Admin"),
    AD_MASTER("04", "Ad Master"),
    SALES_MANAGER("06", "Sales Manager"),
    CLIENT("10", "광고주"),
    AGENCY("20", "대행사"),
    MEDIA_REP("30", "미디어 랩사"),
    MEDIA("40", "매체사"),
    ANONYMOUS("99", "권한없음");

    @NonNull
    @JsonValue
    private String code;
    @NonNull
    private String label;
    
    @MappedTypes(AuthType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<AuthType>{
        public TypeHandler(){
            super(AuthType.class);
        }
    }

    @Override
    public CodeMap map() {
        return CodeMap.builder().code(this.code).label(this.label).build();
    }
    
    public static AuthType codeOf(String expression){
        return Arrays.stream(values())
                .filter(v -> expression.equals(v.code))
                .findFirst()
                .orElse(AuthType.ANONYMOUS);
    }
}
