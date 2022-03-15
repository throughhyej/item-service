package hello.itemservice.web.member;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    public final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member member = new Member("hyej", "테스터", "1111");
        Member saveMember = memberRepository.save(member);
        log.info("::: test data ::: => {}", saveMember);
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member) {
        return "members/addForm";
    }

    @PostMapping("/add")
    public String join(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("::: sign up error ::: => {}", bindingResult);
            return "redirect:/members/add";
        }
        memberRepository.save(member);
        return "redirect:/";
    }

}
