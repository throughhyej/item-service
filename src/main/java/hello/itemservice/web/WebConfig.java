package hello.itemservice.web;

import hello.itemservice.web.filter.LogServletFilter;
import hello.itemservice.web.filter.LoginFilter;
import hello.itemservice.web.interceptor.LogInterceptor;
import hello.itemservice.web.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** 순서: HTTP -> WAS -> Filter -> Servlet -> Interceptor -> Controller **/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);
        /** Spring interceptor 예제: implements WebMvcConfigurer 필수 **/
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/*.css", "/*.ico", "/error");

        registry.addInterceptor(new LoginInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/*.css", "/*.ico", "/error", "/members/add", "/login", "/");
    }

//    @Bean
    public FilterRegistrationBean logFilter() {
        /** Servlet Filter 예제 **/
        FilterRegistrationBean<Filter> logFilterBean = new FilterRegistrationBean<>();
        logFilterBean.setFilter(new LogServletFilter());
        logFilterBean.setOrder(1);
        logFilterBean.addUrlPatterns("/*");
        return logFilterBean;
    }

//    @Bean
    public FilterRegistrationBean loginFilter() {
        /** Servlet Filter 로그인 **/
        FilterRegistrationBean<Filter> loginFilter = new FilterRegistrationBean<>();
        loginFilter.setFilter(new LoginFilter());
        loginFilter.setOrder(2);
        loginFilter.addUrlPatterns("/*");
        return loginFilter;
    }

}
