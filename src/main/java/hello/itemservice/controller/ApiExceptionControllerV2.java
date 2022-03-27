package hello.itemservice.controller;

import hello.itemservice.exception.BadRequestException;
import hello.itemservice.exception.UserException;
import hello.itemservice.exceptionHandler.ExceptionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionControllerV2 {

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


    @GetMapping("/api/members/v2/{id}")
    public Member getMember(@PathVariable String id) {

        if ("ex".equals(id)) throw new RuntimeException("잘못된 사용자");
        if ("bad".equals(id)) throw new IllegalArgumentException("잘못된 요청으로 임의 처리: 500 -> 400");
        if ("user-ex".equals(id)) throw new UserException("사용자 오류");

        return new Member(id, "hello > " + id);
    }

    @Setter @Getter
    @AllArgsConstructor
    public static class Member {
        private String id;
        private String name;
    }

}
