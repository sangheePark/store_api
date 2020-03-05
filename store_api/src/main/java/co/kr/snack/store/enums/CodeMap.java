package co.kr.snack.store.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CodeMap {
    @NonNull
    private String code;
    @NonNull
    private String label;
    
    @Builder
    private CodeMap(String code, String label) {
        this.code = code;
        this.label = label;
    };
    
    public static CodeMap empty() {
        return CodeMap.builder().code(null).label(null).build();
    }
}
