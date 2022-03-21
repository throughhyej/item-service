package hello.itemservice.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private final static String LOG_UUID = "uuid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        /** Handler Adaptor 호출 전 **/
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_UUID, uuid);

        if (handler instanceof HandlerMethod) {
            // 호출된 컨트롤러 메서드의 모든 정보가 담겨있음
            HandlerMethod handlerMethod = (HandlerMethod) handler;
        }

        log.info("### interceptor :: REQ START @@ UUID: [{}], dispatcherType: [{}], URI: [{}], handler: [{}] ###", uuid, request.getDispatcherType(), request.getRequestURI(), handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        /** Handler Adaptor > Controller 호출 후 : 예외 발생 시 호출되지 않음 **/
        log.info("### interceptor ::  REQ MID @@ mv => {}", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        /** View까지 반환 후 호출됨 : 예외 발생해도 무조건 호출됨 **/
        String uuid = String.valueOf(request.getAttribute(LOG_UUID));
        log.info("### interceptor :: RES END @@ UUID: [{}], dispatcherType: [{}], URI: [{}], handler: [{}] ###", uuid, request.getDispatcherType(), request.getRequestURI(), handler);
        if (ex != null) log.error("###  interceptor :: AFTER COMPLETION ERR => ", ex);
    }
}
