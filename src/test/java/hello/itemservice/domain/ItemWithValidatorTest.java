package hello.itemservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ItemWithValidatorTest {

    @Autowired
    private MessageSource ms;

    @Test
    public void validator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        
        Item item = new Item();
        item.setItemName("");
        item.setPrice(999);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> validate = validator.validate(item);
        for (ConstraintViolation<Item> itemConstraintViolation : validate) {
            System.out.println("itemConstraintViolation = " + itemConstraintViolation);
        }
    }

    @Getter @Setter
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

        public Item() {}
        public Item(String itemName, Integer price, Integer quantity) {
            this.itemName = itemName;
            this.price = price;
            this.quantity = quantity;
        }
    }

}
