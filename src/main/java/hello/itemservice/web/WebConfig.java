package hello.itemservice.web;

import hello.itemservice.web.filter.LogServletFilter;
import hello.itemservice.web.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        /** Servlet Filter 예제 **/
        FilterRegistrationBean<Filter> logFilterBean = new FilterRegistrationBean<>();
        logFilterBean.setFilter(new LogServletFilter());
        logFilterBean.setOrder(1);
        logFilterBean.addUrlPatterns("/*");
        return logFilterBean;
    }

    @Bean
    public FilterRegistrationBean loginFilter() {
        /** Servlet Filter 로그인 **/
        FilterRegistrationBean<Filter> loginFilter = new FilterRegistrationBean<>();
        loginFilter.setFilter(new LoginFilter());
        loginFilter.setOrder(2);
        loginFilter.addUrlPatterns("/*");
        return loginFilter;
    }

}
