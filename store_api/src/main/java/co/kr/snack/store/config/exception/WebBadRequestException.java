package co.kr.snack.store.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * <b>요청 유효성(400)</b>
 * 
 * <pre>
 * <b>Description:</b>
 * throw new WebBadRequestException("사용자가 존재 하지않습니다.");
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.05.09, snack: 최초작성
 * </pre>
 * 
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.05.09
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "web_bad_request")
public class WebBadRequestException extends RuntimeException {
    public static final String NOT_HAVE_PARAM = "NOT_HAVE_PARAM";
    public static final String HAS_UNIT = "HAS_UNIT";
    private static final long serialVersionUID = -8611613398434475325L;

    public WebBadRequestException(String msg) {
        super(msg);
    }
}
