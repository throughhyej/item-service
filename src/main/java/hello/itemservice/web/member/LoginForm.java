package hello.itemservice.web.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Setter @Getter @ToString
public class LoginForm {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String pwd;

}
