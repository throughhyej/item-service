package hello.itemservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ItemDto {

    @NotBlank(message = "빈값일 수 없습니다.")
    private String itemName;

    @NotNull
    @Range(min=10000, max=1000000)
    private Integer price;

    @NotNull
    @Range(max=9999)
    private Integer quantity;

    public ItemDto () {}
    public ItemDto (String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
