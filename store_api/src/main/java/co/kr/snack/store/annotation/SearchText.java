package co.kr.snack.store.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <b>검색 필터 _ 특수문자 변경</b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.12.11, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.12.11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchText {
}
