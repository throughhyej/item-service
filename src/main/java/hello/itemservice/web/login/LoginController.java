package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.member.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/login";
    }

    @PostMapping
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/login/login" ;
        }

        Member login = loginService.login(loginForm.getLoginId(), loginForm.getPwd());

        if (login == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 불일치");
            return "/login/login" ;
        }

        // TODO:


        log.info("### 로그인 성공 ###");
        return "redirect:/";
    }
}
