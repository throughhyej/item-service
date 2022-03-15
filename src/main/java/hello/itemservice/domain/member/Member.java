package hello.itemservice.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Setter @Getter @ToString
public class Member {

    private Long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    public Member(String loginId, String name, String password) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }
}
