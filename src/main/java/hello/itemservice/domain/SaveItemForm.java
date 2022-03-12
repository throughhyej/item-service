package hello.itemservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class SaveItemForm {

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min=10000, max=1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;

}
