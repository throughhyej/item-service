package hello.itemservice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public Member getMember(@PathVariable String id) {

        if ("ex".equals(id)) throw new RuntimeException("잘못된 사용자");
        return new Member(id, "hello > " + id);
    }


    @Setter @Getter
    @AllArgsConstructor
    public static class Member {
        private String id;
        private String name;
    }

}
