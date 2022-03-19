package hello.itemservice.web.filter;

import hello.itemservice.web.session.HttpServletSessionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = new String[] {"/", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("@@@ Login Filter START @@@");
            if (!checkUrl(requestURI)) {
                log.info("@@@ 미인증 사용자 redirect @@@");
                HttpSession session = httpRequest.getSession();
                if (session == null || session.getAttribute(HttpServletSessionConstants.SERVLET_SESSION) == null) {
                    httpResponse.sendRedirect("/login?redirectURI=/basic/items/v4");
                    return ;
                }

            }
            chain.doFilter(request, response);
        } catch (Exception ex) {
            throw ex;
        } finally {
            log.info("@@@ Login Filter END @@@");
        }

    }

    private boolean checkUrl(String requestURI) {
        /* WHITE_LIST: 미인증 사용자가 접근 가능한 uri 체크 */
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
