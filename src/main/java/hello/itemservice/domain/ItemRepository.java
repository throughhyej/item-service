package hello.itemservice.domain;

import hello.itemservice.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findAll();
    Item findById(Long id);
    Item save(Item item);
    Item update(Long id, ItemDto item);

    void clearStore();

}
