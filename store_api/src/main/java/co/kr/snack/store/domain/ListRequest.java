package co.kr.snack.store.domain;

import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <b>Page 결과</b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.05.29, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.05.29
 */
@Getter
@Setter
public class ListRequest<T> {
    
    @Valid
    private List<T> list;
}