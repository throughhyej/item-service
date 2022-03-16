package hello.itemservice.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionManager {

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    private static final String MY_SESSION = "mySessionId";

    // 세션 생성
    public void createSession(Object object, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, object);

        Cookie cookie = new Cookie(MY_SESSION, sessionId);
        response.addCookie(cookie);
    }

    // 세션 조회
    public Object getSession(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return sessionStore.get(Arrays.stream(request.getCookies())
                .filter(cookie -> MY_SESSION.equals(cookie.getName()))
                .findAny().get().getValue());
    }

    // 세션 만료
    public void expire(HttpServletRequest request) {
        if (request.getCookies() != null) {
            sessionStore.remove(Arrays.stream(request.getCookies())
                    .filter(cookie -> MY_SESSION.equals(cookie.getName())).findAny().get().getValue());
            log.info("::: 세션 만료 :::");
        }
    }

}
