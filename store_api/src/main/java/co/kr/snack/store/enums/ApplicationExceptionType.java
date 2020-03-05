package co.kr.snack.store.enums;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
@Getter
@RequiredArgsConstructor
public enum ApplicationExceptionType  implements CodeEnum {
    SUCCESS("200", "정상"),
    ACCESS_DENIED("40301", "접근 권한이 없습니다."),
    TOKEN_INVALID("40101", "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED("40102", "토큰이 만료되었습니다."),
    METHOD_NOT_SUPPORTED("40103", "지원하지 않는 유형입니다."),
    HEADER_INVALID("40104", "지원하지 않는 유형입니다."),
    BASIC_AUTH_INVALID("40105", "지원하지 않는 유형입니다."),
    USER_INVALID("40106", "아이디 혹은 비밀번호가 올바르지 않습니다. 다시 입력해주세요."),
    ACCOUNT_LOCK("40108", "당신의 계정은 잠긴 상태입니다."),
    OTHER_USER_LOGIN("40109", "다른 사용자가 로그인하였습니다."),
    REFRESH_TOKEN_EXPIRED("40110", "세션이 만료되었습니다."), 
    ACCOUNT_DEACTIVATION("40111", "당신의 계정은 비활성 상태입니다."),
    ETC("99", "기타");
    
    @NonNull
    @JsonValue
    private String code;
    
    @NonNull
    private String label;
    
    @Override
    public String getCode() {
        return this.code;
    }
    
    @Override
    public String getLabel() {
        return this.label;
    }
    
    @MappedTypes(ApplicationExceptionType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<ApplicationExceptionType>{
        public TypeHandler(){
            super(ApplicationExceptionType.class);
        }
    }

    @Override
    public CodeMap map() {
        return CodeMap.builder().code(this.code).label(this.label).build();
    }
}
