package hello.itemservice.session;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.session.SessionManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SessionManagerTest {

    MemberRepository memberRepository = new MemberRepository();
    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Member member = memberRepository.save(new Member("hyejeyh", "mock data", "1111"));
        Member findMember = memberRepository.findByLoginId(member.getLoginId()).get();

        // 클라이언트 -> 서버
        // 세션 생성
        if (findMember != null) {
            if (findMember.getPassword().equals(member.getPassword())) {
                sessionManager.createSession(findMember, response);
            }
        }

        // 클라이언트 <- 서버
        request.setCookies(response.getCookies());

        // 세션 조회
        Object session = sessionManager.getSession(request);
        Assertions.assertThat(session).isEqualTo(findMember);

        // 세션 만료
        sessionManager.expire(request);
        Object sessionAfterExpired = sessionManager.getSession(request);
        Assertions.assertThat(sessionAfterExpired).isNull();
    }

}
