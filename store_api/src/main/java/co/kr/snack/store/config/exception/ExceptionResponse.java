package co.kr.snack.store.config.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
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
 * - 2020.02.06, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2020.02.06
 */
@Getter
@Slf4j
public class ExceptionResponse {
    /** 에러 코드 **/
    private String error;
    /** 오류 deubg id **/
    private String cause;
    /** 오류 메세지 **/
    private String message;
    /** 오류 발생 일시 **/
    private Date timestamp;
    /** 오류 정보 **/
    private HashMap<String, ?> info;
    
    @Builder
    public ExceptionResponse (String error, String message, HashMap<String, ?> info) {
        this.error = error;
        this.cause = UUID.randomUUID().toString();
        this.message = message;
        this.info = info;
        this.timestamp = new Date();
    }
    
    public ExceptionResponse print(Exception e) {
        log.error("EXCEPTION_CAUSE:" + this.cause);
        e.printStackTrace();
        return this;
    }
    public ExceptionResponse print() {
        log.error("EXCEPTION_CAUSE:" + this.cause);
        return this;
    }
}
