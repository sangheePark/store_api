package co.kr.snack.store.config.exception;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import co.kr.snack.store.enums.ApplicationExceptionType;


/**
 * 
 * <b>Exception 처리</b>
 * 
 * <pre>
 * <b>Description:</b>
 * 내부 오류(500), 인증 오류(401), 요청 유효성(400), 접근 권한 없음(403)
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.11.05, snack: 최초작성
 * </pre>
 * 
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.11.05
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 일반 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, Exception e) {
//        e.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionResponse.builder().error(HttpStatus.INTERNAL_SERVER_ERROR.value() + "")
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build().print(e));
    }

    // 인증 오류
    @ExceptionHandler(AuthorizedException.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, AuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.ofExceptionResponse().print(e));
    }

    // 접근권한 오류
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder().error(ApplicationExceptionType.ACCESS_DENIED.getCode())
                        .message("FORBIDDEN").build().print(e));
    }

    // Runtime Exception
    @ExceptionHandler(WebBadRequestException.class)
    public ResponseEntity<?> handDefaultException(WebBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .error(HttpStatus.BAD_REQUEST.value() + "").message(e.getMessage()).build().print(e));
    }

//     Runtime Exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handDefaultException(HttpMessageNotReadableException e) {

        List<String> codes = Lists.newArrayList(StringUtils
                .substringAfter(StringUtils.substringBefore(e.getRootCause().getMessage(), "]"), "[").split(","));
        String field = StringUtils.substringAfter(StringUtils.substringBefore(e.getMessage(), "\"])"), "[\"");

        if (field.contains("[")) {
            field = StringUtils.substringAfter(field, "[\"");
        }

        HashMap<String, Object> info = Maps.newHashMap();
        info.put(field, codes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .error(HttpStatus.BAD_REQUEST.value() + "").message("BAD_REQUEST").info(info).build().print(e));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handDefaultException(ConstraintViolationException e) {

        String code = StringUtils.substringAfter(e.getMessage(), ":");
        String field = StringUtils.substringBefore(StringUtils.substringAfter(e.getMessage(), "."), ":");

        HashMap<String, Object> info = Maps.newHashMap();
        info.put(field, code);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .error(HttpStatus.BAD_REQUEST.value() + "").message("BAD_REQUEST").info(info).build().print(e));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handDefaultException(BindException e) {

        List<FieldError> errorFields = e.getBindingResult().getFieldErrors();
        HashMap<String, Object> info = Maps.newHashMap();
        for (FieldError fieldError : errorFields) {
            String field = fieldError.getField();
            String code = fieldError.getDefaultMessage();
            List<String> codes = Lists.newArrayList(code.split(","));
            info.put(field, codes);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .error(HttpStatus.BAD_REQUEST.value() + "").message("BAD_REQUEST").info(info).build().print(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handDefaultException(MethodArgumentNotValidException e) {

        HashMap<String, List<String>> info = Maps.newHashMap();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String field = StringUtils.substringAfter(StringUtils.substringBefore(error.toString(), ":"), "d '")
                    .replace("'", "");
            String msg = error.getDefaultMessage();
            if (info.containsKey(field)) {
                info.get(field).add(msg);
            } else {
                List<String> newList = Lists.newArrayList();
                newList.add(msg);
                info.put(field, newList);
            }
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .error(HttpStatus.BAD_REQUEST.value() + "").message("BAD_REQUEST").info(info).build().print(e));
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadException(MaxUploadSizeExceededException e, HttpServletRequest request, HttpServletResponse response){
        HashMap<String, String> info = Maps.newHashMap();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .error(HttpStatus.BAD_REQUEST.value() + "").message("BAD_REQUEST").info(info).build().print(e));
    }
}
