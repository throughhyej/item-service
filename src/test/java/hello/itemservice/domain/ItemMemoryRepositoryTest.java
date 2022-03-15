package hello.itemservice.domain;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemMemoryRepository;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.dto.ItemDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ItemMemoryRepositoryTest {

    ItemRepository itemRepository = new ItemMemoryRepository();

    @BeforeEach
    void beforeEach() {
       itemRepository.clearStore();
    }

    @Test
    void findAll() {
        Item itemA = new Item("itemA", 7000, 8);
        Item itemB = new Item("itemA", 8000, 7);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        List<Item> items = itemRepository.findAll();
        Assertions.assertThat(items.size()).isNotEqualTo(0);
        Assertions.assertThat(items.size()).isEqualTo(2);
        Assertions.assertThat(items).contains(itemA, itemB);
    }

    @Test
    void findById() {
        Item itemA = new Item("itemA", 7000, 8);
        Item itemB = new Item("itemA", 8000, 7);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        Item findItem1 = itemRepository.findById(itemB.getId());

        Assertions.assertThat(itemB).isEqualTo(findItem1);
    }

    @Test
    void save() {
        Item itemA = new Item("itemA", 7000, 8);
        Item saveItem = itemRepository.save(itemA);

        Assertions.assertThat(itemA.getItemName()).isEqualTo(saveItem.getItemName());
    }

    @Test
    void update() {
        Item itemA = new Item("itemA", 7000, 8);
        Item itemB = new Item("itemA", 8000, 7);

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        Item findItem = itemRepository.findById(itemA.getId());
        ItemDto itemDto = new ItemDto("itemC", 10000, 100);

        Item updateItem = itemRepository.update(findItem.getId(), itemDto);
        Assertions.assertThat(findItem.getId()).isEqualTo(updateItem.getId());

    }
}