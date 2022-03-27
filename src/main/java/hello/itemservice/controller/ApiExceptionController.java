package hello.itemservice.controller;

import hello.itemservice.exception.BadRequestException;
import hello.itemservice.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        // data type mismatch exception 발생 시, 스프링이 자동으로 500 -> 400 에러로 변경해준다.
        // DefaultHandlerExceptionResolver 사용 (스프링 내부 예외 처리)
        return "OK";
    }

    @GetMapping("/api/response-status/ex2")
    public void getException2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @GetMapping("/api/response-status/ex1")
    public void getException1() {
        // HTTP 응답코드 변경 처리
        throw new BadRequestException();
    }

    @GetMapping("/api/members/{id}")
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
