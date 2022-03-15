package hello.itemservice.controller;

import hello.itemservice.domain.*;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemMemoryRepository;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
//@RequiredArgsConstructor
@RequestMapping("/basic/items/v4")
public class BasicItemControllerV4 {

    @Autowired MessageSource ms;

    private final ItemRepository itemRepository;

    // @Autowired // 생성자 하나뿐이어서 생략
    // @RequiredArgsConstructor 설정으로 아래 생성자 주석처리 가능 (lombok이 자동으로 처리)
    public BasicItemControllerV4(ItemMemoryRepository itemRepository) {
        this.itemRepository = itemRepository;
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
        return "basic/v4/items";
    }

    @GetMapping("/{itemId}")
    public String getItemInfo(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v4/item";
    }

    @GetMapping("/{itemId}/edit")
    public String getUpdateForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @Validated @ModelAttribute("item") UpdateItemFrom form, BindingResult bindingResult, Model model) {

        Item item = new ItemV2(form.getItemName(), form.getPrice(), form.getQuantity());

        // DTO 사용이 관리해야할 소스가 중복 (entity에 메시지 정의시)
        this.objectErrorValidator(item, bindingResult);

        /** 전방에 위치시켜 빈값일 때, 선처리 가능 **/
        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v4/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/basic/items/v4/{itemId}";
    }

    @GetMapping("/add")
    public String getAddFormV1(Model model) {
        model.addAttribute("item", new Item());
        return "basic/v4/addForm";
    }

    @PostMapping("/add")
    public String saveItem(@Validated @ModelAttribute("item") SaveItemForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Item item = new ItemV2(form.getItemName(), form.getPrice(), form.getQuantity());

        // @Validated로 domain에 붙은 validation 어노테이션이 작동
        this.objectErrorValidator(item, bindingResult);

        /** 전방에 위치시켜 빈값일 때, 선처리 가능 **/
        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v4/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v4/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

    private void objectErrorValidator(Item item, BindingResult bindingResult) {
        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                bindingResult.reject("validation.item.globalError", new Object[] {10000, result}, "가격 * 수량의 곱은 10,000원 이상이어야 합니다. 현재 값 = " + result);
            }
        }
    }

}
