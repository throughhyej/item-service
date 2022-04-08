package hello.itemservice.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
public class FileItem {

    private Long id;
    private final String itemName;
    private final UploadFile attachFile;
    private final List<UploadFile> attachImg;

}
