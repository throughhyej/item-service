package hello.itemservice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
/** v3: ObjectError 처리: 실무에서는 잘 사용하지 않음 -> Controller에 대체 로직 추가 **/
//@ScriptAssert(lang="javascript", script="_this.price * _this.quantity >= 10000", message="가격과 수량의 곱이 10000원 이상 필수")
public class Item {

    private Long id;

    @NotBlank(message = "빈값일 수 없습니다.")
    private String itemName;

    @NotNull
    @Range(min=10000, max=1000000)
    private Integer price;

    @NotNull
    @Range(max=9999)
    private Integer quantity;

    public Item () {}
    public Item (String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
