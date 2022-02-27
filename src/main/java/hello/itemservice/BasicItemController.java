package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemMemoryRepository;
import hello.itemservice.domain.ItemRepository;
import hello.itemservice.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;
    public BasicItemController(ItemMemoryRepository itemRepository) {
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
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String getItemInfo(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String getUpdateForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemDto itemDto, Model model) {
        Item item = itemRepository.update(itemId, itemDto);
        model.addAttribute("item", item);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/add")
    public String getAddForm() {
        return "basic/addForm";
    }

    // @PostMapping("/add")
    public String saveItemV1(@RequestParam String itemName, int price, int quantity) {
        Item saveItem = itemRepository.save(new Item(itemName, price, quantity));
        return "basic/item";
    }

    // @PostMapping("/add")
    public String saveItemV2(@ModelAttribute("item") Item item) {
        Item saveItem = itemRepository.save(item);
        return "basic/item";
    }

    // @PostMapping("/add")
    public String saveItemV3(@ModelAttribute Item item) {
        Item saveItem = itemRepository.save(item);
        // model.addAttribute("item", item); // @ModelAttribute 선언으로 생략 가능 + model
        return "basic/item"; // 등록 후 새로고침 시 문제 발생 => PRG로 해결 (saveItemV5)
    }

    // @PostMapping("/add")
    public String saveItemV4(Item item) {
        /* @ModalAttribute 생략 */
        Item saveItem = itemRepository.save(item);
        return "basic/item";
    }

    // @PostMapping("/add")
    public String saveItemV5(@ModelAttribute Item item) {
        Item saveItem = itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); // PRG -> URL encoding 문제 발생 가능성 있음
    }


    @PostMapping("/add")
    public String saveItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

}
