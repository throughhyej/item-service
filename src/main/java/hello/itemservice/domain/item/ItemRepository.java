package hello.itemservice.domain.item;

import hello.itemservice.dto.ItemDto;

import java.util.List;

public interface ItemRepository {

    List<Item> findAll();
    Item findById(Long id);
    Item save(Item item);
    Item update(Long id, ItemDto item);
    Item update(Long id, Item item);

    void clearStore();

}
