package hello.itemservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class FileUploadSpringController {

    @Value("${file.dir}")
    private String file_path;

    @GetMapping("/upload")
    public String getFileForm() {
        return "file/upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName
            , @RequestParam MultipartFile file
            , HttpServletRequest request) throws IOException {
        log.info("::: request ::: => {}", request);
        log.info("::: itemName ::: => {}", itemName);
        log.info("::: multipart file ::: => {}", file);

        if (!file.isEmpty()) {
            String fullPath = file_path + file.getOriginalFilename();
            log.info("::: FILE FULL PATH ::: => {}", fullPath);
            file.transferTo(new File(fullPath));
        }

        return "file/upload-form";
    }


}
