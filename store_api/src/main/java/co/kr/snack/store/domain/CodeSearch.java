package co.kr.snack.store.domain;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.google.common.base.CaseFormat;

import co.kr.snack.store.annotation.SearchText;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * - 2019.12.20, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.12.20
 */
@NoArgsConstructor
@Data
@ToString
@Slf4j
public class CodeSearch<T> {
    
    private String sortBy;
    private boolean descending;
    
    @Valid
    @NotNull
    private T param;
    
    public T setParam(T param) {
        this.param = param;
        Class<?> clazz = this.param.getClass();
        Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(SearchText.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(this.param);
                        if (Optional.ofNullable(value).isPresent()) {
                            field.set(this.param, value.replaceAll("_", "\\\\_"));
                        }
                        
                    } catch (Exception e) {
                        log.error("SearchText: Replace Error.");
                    }
                });
        return this.param;
    }
    
    public String getSortBy() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.sortBy);
    }
}
