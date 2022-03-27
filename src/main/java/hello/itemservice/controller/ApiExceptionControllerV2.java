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
