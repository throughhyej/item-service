package hello.itemservice.exception.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ServletExceptionController {

    /** 서블릿은 에러가 WAS까지 전달되거나, response.sendError()가 호출되었을 때 설정된 오류 페이지를 찾는다.
     * 이 때, 클라이언트 모르게 errorpage를 보여주기 위해 필터부터 다시 호출한다.
     * WAS -> Filter -> Servlet -> Interceptor -> Controller
     **/

    @GetMapping("/throw-error")
    public void throwException() {
        throw new RuntimeException();
    }

    @GetMapping("/error-404")
    public void responseError404(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "404 에러: NOT FOUND");
    }

    @GetMapping("/error-500")
    public void responseError500(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

}
