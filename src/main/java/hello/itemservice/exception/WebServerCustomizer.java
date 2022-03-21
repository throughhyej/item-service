package hello.itemservice.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    /** WAS까지 에러가 전달되어 올 때, 호출할 Controller 지정 **/
    
    public static final String ERROR_404_CONTROLLER = "/error-page/404";
    public static final String ERROR_500_CONTROLLER = "/error-page/500";

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, ERROR_404_CONTROLLER);
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_500_CONTROLLER);
        ErrorPage runtimeEx = new ErrorPage(RuntimeException.class, ERROR_500_CONTROLLER);

        factory.addErrorPages(errorPage404, errorPage500, runtimeEx);

    }

}
