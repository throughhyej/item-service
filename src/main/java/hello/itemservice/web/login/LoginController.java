package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.member.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private static final String COOKIE_NAME = "memberId";

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "/login/login" ;
        }

        Member login = loginService.login(loginForm.getLoginId(), loginForm.getPwd());

        if (login == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 불일치");
            return "/login/login" ;
        }

        // Cookie 이용 (다음의 보안 문제로 사용하지 않음)
        // 1. 쿠키값은 임의로 변경이 가능하다.
        // 2. 탈취 가능 (중요한 정보 담지 말기)
        // => 대안 (토큰: 서버에서 토큰 만료시간 짧게 지정) : session
        Cookie cookie = new Cookie(COOKIE_NAME, String.valueOf(login.getId()));
        response.addCookie(cookie);

        log.info("### 로그인 성공 ###");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
