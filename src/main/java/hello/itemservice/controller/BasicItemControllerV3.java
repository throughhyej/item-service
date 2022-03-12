package hello.itemservice.controller;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemMemoryRepository;
import hello.itemservice.domain.ItemRepository;
import hello.itemservice.dto.ItemDto;
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
@RequestMapping("/basic/items/v3")
public class BasicItemControllerV3 {

    @Autowired MessageSource ms;

    private final ItemRepository itemRepository;

    // @Autowired // 생성자 하나뿐이어서 생략
    // @RequiredArgsConstructor 설정으로 아래 생성자 주석처리 가능 (lombok이 자동으로 처리)
    public BasicItemControllerV3(ItemMemoryRepository itemRepository) {
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
        return "basic/v3/items";
    }

    @GetMapping("/{itemId}")
    public String getItemInfo(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v3/item";
    }

    @GetMapping("/{itemId}/edit")
    public String getUpdateForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemDto itemDto, Model model) {
        Item item = itemRepository.update(itemId, itemDto);
        model.addAttribute("item", item);
        return "redirect:/basic/items/v3/{itemId}";
    }

    @GetMapping("/add")
    public String getAddFormV1(Model model) {
        model.addAttribute("item", new Item());
        return "basic/v3/addForm";
    }

    @PostMapping("/add")
    public String saveItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // @Validated로 domain에 붙은 validation 어노테이션이 작동

        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                bindingResult.reject("validation.item.globalError", new Object[] {10000, result}, "가격 * 수량의 곱은 10,000원 이상이어야 합니다. 현재 값 = " + result);
            }
        }

        /** 전방에 위치시켜 빈값일 때, 선처리 가능 **/
        if (bindingResult.hasErrors()) {
            log.info("## errors : {} ", bindingResult);
            return "basic/v3/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v3/{itemId}"; // PRG: URL encoding 문제 해결됨
    }


}
