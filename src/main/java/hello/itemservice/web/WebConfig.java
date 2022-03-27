package hello.itemservice.web;

import hello.itemservice.exception.resolver.MyExceptionResolver;
import hello.itemservice.exception.resolver.UserHandlerExceptionResolver;
import hello.itemservice.web.argumentResolver.LoginArgumentResolver;
import hello.itemservice.web.filter.LogServletFilter;
import hello.itemservice.web.filter.LoginFilter;
import hello.itemservice.web.interceptor.LogInterceptor;
import hello.itemservice.web.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver());
    }

    /** 순서: HTTP -> WAS -> Filter -> Servlet -> Interceptor -> Controller **/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);
        /** Spring interceptor 예제: implements WebMvcConfigurer 필수 **/
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/*.css", "/*.ico");

        registry.addInterceptor(new LoginInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/members/*", "/*.css", "/*.ico", "/*err*", "/error-page/*", "/members/add", "/login", "/");
    }

//    @Bean
    public FilterRegistrationBean logFilter() {
        /** Servlet Filter 예제 **/
        FilterRegistrationBean<Filter> logFilterBean = new FilterRegistrationBean<>();
        logFilterBean.setFilter(new LogServletFilter());
        logFilterBean.setOrder(1);
        logFilterBean.addUrlPatterns("/*");
        logFilterBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        // request와 error 일 때만 호출이 됨
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
