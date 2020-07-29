package co.kr.snack.store.domain;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.google.common.base.CaseFormat;

import co.kr.snack.store.annotation.SearchText;
import co.kr.snack.store.config.security.entity.Requester;
import co.kr.snack.store.enums.YesNoType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <b>페이지 처리 요청</b>
 * <pre>
 * <b>Description:</b>
 * 필터: param
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.11.05, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.11.05
 */
@NoArgsConstructor
@Data
@ToString
@Slf4j
public class PageSearch<T> {

    private Integer page;
    private Integer rowsPerPage;
    private String sortBy;
    private boolean descending;
    private String xlsYn;
    private Requester requestUser;
    private YesNoType isMask = YesNoType.YES;
    
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
                        log.debug("xxxxxx:" + value);
                        if (Optional.ofNullable(value).isPresent()) {
                        	value = value.replaceAll("\\\\", "\\\\\\\\");
                        	value = value.replaceAll("_", "\\\\_");
                        	value = value.replaceAll("%", "\\\\%");
                        	field.set(this.param, value);
                            log.debug("oooooo:" + field.get(this.param));
                        }
                        
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        log.error("SearchText: Replace Error.");
                    }
                });
        return this.param;
    }

    public int getPage() {
        return Optional.ofNullable(this.page).orElse(1);
    }

    public int getSize() {
        return Optional.ofNullable(this.rowsPerPage).orElse(10);
    }

    public int getStart() {
        return (this.getPage() - 1) * this.getSize();
    }

    public String getOrderType() {
    	return this.descending ? "DESC" : "ASC";
    }
    
    public String getSortBy() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.sortBy);
    }
    
    public PageSearch( Integer page, Integer rowsPerPage, String sortBy, boolean descending, T param){
        this.page = page;
        this.rowsPerPage = rowsPerPage;
        this.sortBy = sortBy;
        this.descending = descending;
        this.param = param;
    }
}
