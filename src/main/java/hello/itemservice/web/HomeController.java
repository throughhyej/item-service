package hello.itemservice.web;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.session.HttpServletSessionConstants;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @RequestMapping("/")
//    public String home() {
//        return "home";
//    }

//    @RequestMapping("/")
    public String homeCookie(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) return "home";

        Member member = memberRepository.findById(memberId);
        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @RequestMapping("/")
    public String homeSession(HttpServletRequest request, Model model) {

        Member member = (Member) sessionManager.getSession(request);
        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @RequestMapping("/")
    public String homeServletSession(HttpServletRequest request, Model model) {

        // session 없을 경우, 새로 생성하지 못하게 false 설정
        HttpSession session = request.getSession(false);
        Member member = null;

        if (session != null)
            member = (Member) session.getAttribute(HttpServletSessionConstants.SERVLET_SESSION);

        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

    @RequestMapping("/")
    public String homeSpringSession(@SessionAttribute(name = HttpServletSessionConstants.SERVLET_SESSION, required = false) Member member, Model model) {

        /* session 찾을 때, Spring에서 제공하는 @SessionAttribute 사용
         * session을 신규 생성하지 않음
         */

        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

}
