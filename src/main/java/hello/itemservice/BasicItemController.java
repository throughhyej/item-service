package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemMemoryRepository;
import hello.itemservice.domain.ItemRepository;
import hello.itemservice.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/basic/items/v1")
public class BasicItemController {

    @Autowired MessageSource ms;

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
        return "basic/v1/items";
    }

    @GetMapping("/{itemId}")
    public String getItemInfo(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v1/item";
    }

    @GetMapping("/{itemId}/edit")
    public String getUpdateForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemDto itemDto, Model model) {
        Item item = itemRepository.update(itemId, itemDto);
        model.addAttribute("item", item);
        return "redirect:/basic/items/v1/{itemId}";
    }

    @GetMapping("/add")
    public String getAddForm(Model model) {
        model.addAttribute("item", new Item());
        return "basic/v1/addForm";
    }

    // @PostMapping("/add")
    public String saveItemV1(@RequestParam String itemName, int price, int quantity) {
        Item saveItem = itemRepository.save(new Item(itemName, price, quantity));
        return "basic/v1/item";
    }

    // @PostMapping("/add")
    public String saveItemV2(@ModelAttribute("item") Item item) {
        Item saveItem = itemRepository.save(item);
        return "basic/v1/item";
    }

    // @PostMapping("/add")
    public String saveItemV3(@ModelAttribute Item item) {
        Item saveItem = itemRepository.save(item);
        // model.addAttribute("item", item); // @ModelAttribute 선언으로 생략 가능 + model
        return "basic/v1/item"; // 등록 후 새로고침 시 문제 발생 => PRG로 해결 (saveItemV5)
    }

    // @PostMapping("/add")
    public String saveItemV4(Item item) {
        /* @ModalAttribute 생략 */
        Item saveItem = itemRepository.save(item);
        return "basic/v1/item";
    }

    // @PostMapping("/add")
    public String saveItemV5(@ModelAttribute Item item) {
        Item saveItem = itemRepository.save(item);
        return "redirect:/basic/v1/items/" + item.getId(); // PRG -> URL encoding 문제 발생 가능성 있음
    }

    @PostMapping("/add")
    public String saveItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", ms.getMessage("validation.item.name", null, "Item name is required.", null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", ms.getMessage("validation.item.price", null, "price range (1000 <= price <= 1,000,000)", null));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            errors.put("quantity", ms.getMessage("validation.item.quantity", null, "quantity range (quantity <= 9,999)", null));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if (result < 10000) {
                errors.put("globalError", ms.getMessage("validation.item.globalError", new Object[] {result}, "price * quantity = {0} (price * quantity <= 10,000)", null));
            }
        }

        if (!errors.isEmpty()) {
            log.info("## errors : {} ", errors);
            model.addAttribute("errors", errors);
            return "basic/v1/addForm";
        }

        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // uri 치환
        redirectAttributes.addAttribute("status", true); // Query String
        return "redirect:/basic/items/v1/{itemId}"; // PRG: URL encoding 문제 해결됨
    }

}
