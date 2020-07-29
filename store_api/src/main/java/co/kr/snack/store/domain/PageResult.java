package co.kr.snack.store.domain;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * 
 * <b>페이지 처리 결과</b>
 * <pre>
 * <b>Description:</b>
 * 조회 결과: list, 통계 결과: sum
 * 
 * ex) new PageResult<ExObject>(mapper.findAllCount(), mapper.findAll())
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.05.09, snack: 최초작성 
 * - 2019.05.28, snack: page 정보 계산 수정 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.05.09
 */
@Data
public class PageResult<T> {

    private int page;
    private int size;
    private int totalPages;
    private Long totalElements;
    private List<T> list;
    private Sum<?> sum;
    
    public PageResult(Long totalElements, List<T> list, PageSearch<?> pageSearch, Sum<?> sum) {
        this.page = pageSearch.getPage();
        this.size = pageSearch.getSize();
        this.totalPages = (int) Math.ceil(totalElements.doubleValue()/Integer.valueOf(size).doubleValue());
        this.totalElements = totalElements;
        this.list = list;
        this.sum = sum;
    }

    public PageResult(Long totalElements, List<T> list, PageSearch<?> pageSearch) {
        this.page = pageSearch.getPage();
        this.size = pageSearch.getSize();
        this.totalPages = (int) Math.ceil(totalElements.doubleValue()/Integer.valueOf(size).doubleValue());
        this.totalElements = totalElements;
        this.list = list;
    }
    
    public int getCount() {
        return Optional.ofNullable(this.list).orElse(Lists.newArrayList()).size();
    }
}
