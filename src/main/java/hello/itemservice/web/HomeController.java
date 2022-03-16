package hello.itemservice.web;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/")
    public String homeSession(HttpServletRequest request, Model model) {

        Member member = (Member) sessionManager.getSession(request);
        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

}
