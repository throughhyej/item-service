package hello.itemservice.web.argumentResolver;

import hello.itemservice.domain.member.Member;
import hello.itemservice.web.session.HttpServletSessionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /* 지원여부: true 시 resolverArgument 실행
         * Login 어노테이션이 있는지, Member.class 파라미터가 존재하는지 만족 여부
         * 내부에 캐시 같은 것 존재해서 항상 호출되진 않음?
         */
        log.info("@@@@@ LoginArgumentResolver supportsParameter");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
//        boolean isMemberClass = parameter.getParameterType().isAssignableFrom(Member.class);
        boolean isMemberClass = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && isMemberClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("@@@@@ LoginArgumentResolver resolveArgument");

        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = httpRequest.getSession(false);

        if (session == null) return null;

        // return null 혹은 멤버 객체
        return session.getAttribute(HttpServletSessionConstants.SERVLET_SESSION);
    }

}
