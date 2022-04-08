package hello.itemservice.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class ItemForm {

    private Long id;
    private String itemName;
    private MultipartFile file;
    private List<MultipartFile> files;

}
