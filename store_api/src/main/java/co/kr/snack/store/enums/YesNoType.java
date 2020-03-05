package co.kr.snack.store.enums;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * <b>Yes or No code</b>
 * <pre>
 * <b>Description:</b>
    YES("Y", true),
    NO("N", false);
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.05.09, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.05.09
 */
@Getter
@RequiredArgsConstructor
public enum YesNoType  implements CodeEnum {
    YES("Y", true),
    NO("N", false);
    
    @NonNull
    @JsonValue
    private String code;
    @NonNull
    private Boolean is;
    
    @Override
    public String getCode() {
        return this.code;
    }
    
    @Override
    public String getLabel() {
        return this.name();
    }
    
    @MappedTypes(YesNoType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<YesNoType>{
        public TypeHandler(){
            super(YesNoType.class);
        }
    }

    @Override
    public CodeMap map() {
        return CodeMap.builder().code(this.code).label(this.code).build();
    }
    
    public static YesNoType codeOf(String code){
        return YesNoType.YES.getCode().equals(code) ? YesNoType.YES : YesNoType.NO;
    }
}
