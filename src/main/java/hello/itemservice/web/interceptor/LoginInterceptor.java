package hello.itemservice.web.interceptor;

import hello.itemservice.web.session.HttpServletSessionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String redirectURI = request.getRequestURI();
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(HttpServletSessionConstants.SERVLET_SESSION) == null) {
            log.info("### 미인증 사용자 처리 ###");
//            response.sendRedirect("/login?redirectURI=" + redirectURI);
//            return false;
        }



        return true;
    }

}
