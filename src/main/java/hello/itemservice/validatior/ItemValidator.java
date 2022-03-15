package hello.itemservice.validatior;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        // item == clazz, subItem == class 체크
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        /** 검증 로직 시작 **/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "validation", "상품 이름은 필수입니다.");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // validation.item.price
            errors.rejectValue("price", "validation", new Object[] {1000, 1000000}, "가격은 10,000 ~ 1,000,000 까지 허용합니다.");
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            // validation.item.quantity
            errors.rejectValue("quantity", "validation", new Object[] {9999}, "수량은 최대 9.999 까지 허용합니다.");
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                errors.reject("validation.item.globalError", new Object[] {10000, result}, "가격 * 수량의 곱은 10,000원 이상이어야 합니다. 현재 값 = " + result);
            }
        }

    }
}
