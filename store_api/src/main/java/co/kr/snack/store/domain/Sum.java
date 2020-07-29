package co.kr.snack.store.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * <b>페이지 처리 결과(통계)</b>
 * <pre>
 * <b>Description:</b>
 * 통계 결과: data
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
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Sum<T> {
    @NonNull
    private T data;
}