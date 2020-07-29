package co.kr.snack.store.domain;

import java.util.List;

import lombok.Getter;

/**
 * 
 * <b>UI 사용 코드</b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.12.10, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.12.10
 */
@Getter
public class Code {
    private String value;
    private String label;
    private String parentValue;
    
    /**
     * ETC
     */
    private String a;
    private String b;
    private String c;
    private String d;
    
    private List<String> arrValue;
}
