package hello.itemservice.exceptionHandler;

import hello.itemservice.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
/** @ExceptionHandler 공통 처리: 적용 대상을 지정하지 않아 모든 Controller의 Exception 처리 **/
// @RestControllerAdvice(annotations = RestController.class)
// @RestControllerAdvice("hello.itemservice.controller")
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 주석 시, 200 결과
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResult illegalExceptionHandler(IllegalArgumentException ex) {
        log.info("### ExceptionHandler illegalExceptionHandler ");
        return new ExceptionResult("BAD", ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResult> userExceptionHandler(UserException ex) {
        log.info("### ExceptionHandler UserException ");
        ExceptionResult result = new ExceptionResult("USER-EX", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ExceptionResult exception(Exception ex) {
        // 공통 혹은 놓친 exception을 처리하기 위함
        // 우선순위에 의해 상세한 위의 에러 처리가 선동작한다.
        log.info("### ExceptionHandler Exception ");
        return new ExceptionResult("EX", "내부 오류");
    }

}
