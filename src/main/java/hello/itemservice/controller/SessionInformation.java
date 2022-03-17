package hello.itemservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInformation {

    @GetMapping("/session-info")
    public void sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.getAttributeNames().asIterator()
                .forEachRemaining(sessionName ->
                        log.info("::: session name => {}, value => {}", sessionName, session.getAttribute(sessionName))
                );

        log.info("::: session 아이디 ::: => {}", session.getId());
        log.info("::: session 유효시간 (초) ::: => {}", session.getMaxInactiveInterval());
        log.info("::: session 생성시간 ::: => {}", new Date(session.getCreationTime()));
        log.info("::: session 마지막 서버 접근 시간 (sessionId(jsessionId)로 조회된 경우 갱신) ::: => {}", new Date(session.getLastAccessedTime()));
        log.info("::: 신규 여부 (sessionId(jsessionId)로 조회된 경우 false) ::: => {}", session.isNew());

    }

}
