package hello.itemservice.domain.login;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    public final MemberRepository memberRepository;

    public Member login(String loginId, String pwd) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(pwd))
                .orElse(null);
    }

}
