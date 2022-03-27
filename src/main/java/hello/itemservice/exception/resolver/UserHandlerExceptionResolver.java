package hello.itemservice.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.itemservice.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 정상 리턴 처리: 서블릿까지 익셉션 전달하지 않음
            if (ex instanceof UserException) {
                log.info("### ex instanceof UserException");
                String acceptHeader = request.getHeader("Accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

//                if (MediaType.APPLICATION_JSON.equals(acceptHeader)) {
                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("ex", ex.getClass());
                    resultMap.put("msg", ex.getMessage());
                    String result = objectMapper.writeValueAsString(resultMap);

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    return new ModelAndView();
                }else {
                    return new ModelAndView("/error/4xx");
                }
            }

        }catch (Exception exception) {
            log.error("### UserHandlerExceptionResolver exception ", exception.getMessage());
        }

        return null;
    }
}
