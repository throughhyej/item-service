package hello.itemservice.file;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileRepository {

    private final Map<Long, FileItem> store = new ConcurrentHashMap<>();
    private long sequence = 0L;

    public FileItem save(FileItem item) {
        item.setId(sequence++);
        store.put(item.getId(), item);
        return item;
    }

    public FileItem findById(Long id) {
        return store.get(id);
    }



}
