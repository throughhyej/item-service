package hello.itemservice.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class ExceptionResult {
    private String code;
    private String msg;
}
