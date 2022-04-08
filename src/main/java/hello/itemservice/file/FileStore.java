package hello.itemservice.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String filePath;

    public  String getFilePullPath(String fileName) {
        return filePath + fileName;
    }

    public UploadFile storeFile(MultipartFile multipart) throws IOException {
        if (multipart.isEmpty()) return null;

        String originalFileName = multipart.getOriginalFilename();
        String storeFileName = UUID.randomUUID().toString().concat(originalFileName.substring(originalFileName.lastIndexOf(".")));

        multipart.transferTo(new File(getFilePullPath(storeFileName)));
        return new UploadFile(originalFileName, storeFileName);
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multiparts) throws IOException {
        List<UploadFile> fileList = new ArrayList<>();
        for (MultipartFile multipart : multiparts) {
            if (!multipart.isEmpty()) {
                fileList.add(storeFile(multipart));
            }
        }
        return fileList;
    }

}
