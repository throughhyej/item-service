package hello.itemservice.controller;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemMemoryRepository;
import hello.itemservice.domain.ItemRepository;
import hello.itemservice.dto.ItemDto;
import hello.itemservice.validatior.ItemValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
//@RequiredArgsConstructor
@RequestMapping("/basic/items/v2")
public class BasicItemControllerV2 {

    @Autowired MessageSource ms;

    private final ItemRepository itemRepository;
    private final ItemValidator validator;

    // @Autowired // 생성자 하나뿐이어서 생략
    // @RequiredArgsConstructor 설정으로 아래 생성자 주석처리 가능 (lombok이 자동으로 처리)
    public BasicItemControllerV2(ItemMemoryRepository itemRepository, ItemValidator validator) {
        this.itemRepository = itemRepository;
        this.validator = validator;
    }

    @PostConstruct
    public void init() {
        /* 테스트 데이터 세팅 */
        itemRepository.save(new Item("itemA", 5000, 10));
        itemRepository.save(new Item("itemB", 7000, 19));
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/v2/items";
    }

    @GetMapping("/{itemId}")
    public String getItemInfo(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v2/item";
    }

    @GetMapping("/{itemId}/edit")
    public String getUpdateForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemDto itemDto, Model model) {
        Item item = itemRepository.update(itemId, itemDto);
        model.addAttribute("item", item);
        return "redirect:/basic/items/v2/{itemId}";
    }

    @GetMapping("/add")
    public String getAddFormV1(Model model) {
        model.addAttribute("item", new Item());
        return "basic/v2/addForm";
    }

    // @PostMapping("/add")
    public String saveItemV1(@RequestParam String itemName, int price, int quantity) {
        Item saveItem = itemRepository.save(new Item(itemName, price, quantity));
        return "basic/v2/item";
    }

    // @PostMapping("/add")
    public String saveItemV2(@ModelAttribute("item") Item item) {
        Item saveItem = itemRepository.save(item);
        return "basic/v2/item";
    }

    // @PostMapping("/add")
    public String saveItemV3(@ModelAttribute Item item) {
        Item saveItem = itemRepository.save(item);
        // model.addAttribute("item", item); // @ModelAttribute 선언으로 생략 가능 + model
        return "basic/v2/item"; // 등록 후 새로고침 시 문제 발생 => PRG로 해결 (saveItemV5)
    }

    // @PostMapping("/add")
    public String saveItemV4(Item item) {
        /* @ModalAttribute 생략 */
        Item saveItem = itemRepository.save(item);
        return "basic/v2/item";
    }

    // @PostMapping("/add")
    public String saveItemV5(@ModelAttribute Item item) {
        Item saveItem = itemRepository.save(item);
        return "redirect:/basic/v2/items/" + item.getId(); // PRG -> URL encoding 문제 발생 가능성 있음
    }

//    @PostMapping("/add")
    public String saveItemV6(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /** BindingResult의 addError(new FieldError())를 사용 -> 필드값 유지가 안 되는 이슈 발생
         *  saveItemV7 -> 다른 생성자를 사용함으로써 값 유지 기능 제공
         * **/

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", ms.getMessage("validation.item.itemName", null, "Item name is required.", null)));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", ms.getMessage("validation.item.price", null, "price range (1000 <= price <= 1,000,000)", null)));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", ms.getMessage("validation.item.quantity", null, "quantity range (quantity <= 9,999)", null)));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                bindingResult.addError(new ObjectError("item", ms.getMessage("validation.item.globalError", new Object[] {result}, "price * quantity = {0} (price * quantity <= 10,000)", null)));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v2/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v2/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

//    @PostMapping("/add")
    public String saveItemV7(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, ms.getMessage("validation.item.itemName", null, "Item name is required.", null)));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, ms.getMessage("validation.item.price", null, "price range (1000 <= price <= 1,000,000)", null)));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, ms.getMessage("validation.item.quantity", null, "quantity range (quantity <= 9,999)", null)));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                bindingResult.addError(new ObjectError("item", ms.getMessage("validation.item.globalError", new Object[] {result}, "price * quantity = {0} (price * quantity <= 10,000)", null)));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v2/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v2/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

//    @PostMapping("/add")
    public String saveItemV8(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /* 에러 다국어 처리 변경
         * 소스 통일화 시키기 위해 기존 messageSource 및 default value 사용이 나아보임
         * defaultValue: 운영 시 도움은 되겠지만, 수정할 부분이 하나 더 추가됨
         **/

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[] {"validation.item.itemName"}, null, "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[] {"validation.item.price"}, new Object[] {1000, 1000000}, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[] {"validation.item.quantity"}, new Object[] {9999}, "수량은 최대 9.999 까지 허용합니다."));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                bindingResult.addError(new ObjectError("item", new String[] {"validation.item.globalError"}, new Object[] {10000, result}, "가격 * 수량의 곱은 10,000원 이상이어야 합니다. 현재 값 = " + result));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v2/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v2/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

//    @PostMapping("/add")
    public String saveItemV9(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /** 전방에 위치시켜 빈값일 때, 선처리 가능 **/
        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v2/addForm";
        }

//        if (!StringUtils.hasText(item.getItemName())) {
//            // validation.item.itemName
//            bindingResult.rejectValue("itemName", "validation", "상품 이름은 필수입니다.");
//        }
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "validation", "상품 이름은 필수입니다.");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // validation.item.price
            bindingResult.rejectValue("price", "validation", new Object[] {1000, 1000000}, "가격은 10,000 ~ 1,000,000 까지 허용합니다.");
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            // validation.item.quantity
            bindingResult.rejectValue("quantity", "validation", new Object[] {9999}, "수량은 최대 9.999 까지 허용합니다.");
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                bindingResult.reject("validation.item.globalError", new Object[] {10000, result}, "가격 * 수량의 곱은 10,000원 이상이어야 합니다. 현재 값 = " + result);
            }
        }

        /*
        위치 이동
        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v2/addForm";
        }
        */

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v2/{itemId}"; // PRG: URL encoding 문제 해결됨
    }


    @PostMapping("/add")
    public String saveItemV10(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 코드 분리
        if( validator.supports(item.getClass()) ) {
            validator.validate(item, bindingResult);
        }

        /** 전방에 위치시켜 빈값일 때, 선처리 가능 **/
        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v2/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v2/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

}
