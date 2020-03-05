package co.kr.snack.store.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.kr.snack.store.enums.ApplicationExceptionType;


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
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "unauthorized")
public class AuthorizedException extends AuthenticationException {
    private ApplicationExceptionType authorizedExceptionType;
    private static final long serialVersionUID = -8611613398434475325L;

    public AuthorizedException(String msg) {
        super(msg);
        this.authorizedExceptionType = ApplicationExceptionType.ETC;
    }

    public AuthorizedException(ApplicationExceptionType authorizedExceptionType) {
        super(authorizedExceptionType.getLabel());
        this.authorizedExceptionType = authorizedExceptionType;
    }

    public ExceptionResponse ofExceptionResponse() {
        return ExceptionResponse.builder().error(this.authorizedExceptionType.getCode()).message(this.getMessage())
                .build().print(this);
    }
}
