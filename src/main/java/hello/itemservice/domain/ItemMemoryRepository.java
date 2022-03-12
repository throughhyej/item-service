package hello.itemservice.domain;

import hello.itemservice.dto.ItemDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemMemoryRepository implements ItemRepository{

    private static Map<Long, Item> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public List<Item> findAll() {
        return store.values().stream().collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id) {
        return store.get(id);
    }

    @Override
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long id, ItemDto item) {
        Item findItem = this.updateLogin(id, new Item(item.getItemName(), item.getPrice(), item.getQuantity()));
        return store.put(id, findItem);
    }

    @Override
    public Item update(Long id, Item item) {
        Item findItem = this.updateLogin(id, item);
        return store.put(id, findItem);
    }

    private Item updateLogin(Long id, Item item) {
        Item findItem = store.get(id);
        findItem.setItemName(item.getItemName());
        findItem.setPrice(item.getPrice());
        findItem.setQuantity(item.getQuantity());
        return findItem;
    }

    public void clearStore() {
        store.clear();
    }
}
