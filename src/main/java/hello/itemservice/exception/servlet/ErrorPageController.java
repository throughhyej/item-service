package hello.itemservice.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

//    @RequestMapping("/error-page/{errorCode}")
//    public String getErrorPage(HttpServletRequest request) {
//        printErrorMes(request);
//        /** html 리턴 **/
//        return request.getRequestURI();
//    }

    /** produces: accept 값에 따라 우선순위를 가짐 **/
    @RequestMapping(value = "/error-page/{errorCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAPIErrorPage(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> returnVal = new HashMap<>();

        Exception exception = (Exception) request.getAttribute(ERROR_EXCEPTION);
        returnVal.put("errorCode", request.getAttribute(ERROR_STATUS_CODE));
        returnVal.put("errorMsg", exception.getMessage());
        Integer statudCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return new ResponseEntity<>(returnVal, HttpStatus.valueOf(statudCode));
    }

    private void printErrorMes(HttpServletRequest request) {
        log.info(" ERROR_EXCEPTION => {}", request.getAttribute(ERROR_EXCEPTION));
        log.info(" ERROR_EXCEPTION_TYPE => {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info(" ERROR_MESSAGE => {}", request.getAttribute(ERROR_MESSAGE));
        log.info(" ERROR_REQUEST_URI => {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info(" ERROR_SERVLET_NAME => {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info(" ERROR_STATUS_CODE => {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info(" request.getDispatcherType() => {}", request.getDispatcherType());
    }

}
