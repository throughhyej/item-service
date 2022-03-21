package hello.itemservice.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
        log.info("### Servlet filter init ###");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("### REQ [{}], type: {} => {} ### ", uuid, request.getDispatcherType(), httpReq.getRequestURI());
            chain.doFilter(request, response);
        } catch (Exception ex) {
            throw ex;
        } finally {
            log.info("### RES [{}], type: {} => {} ###", uuid, request.getDispatcherType(), httpReq.getRequestURI());
        }

    }

    @Override
    public void destroy() {
//        Filter.super.destroy();
        log.info("### Servlet filter destroy ###");
    }
}