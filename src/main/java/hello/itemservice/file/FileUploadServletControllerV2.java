package hello.itemservice.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class FileUploadServletControllerV2 {

    @Value("${file.dir}")
    private String file_path;

    @GetMapping("/upload")
    public String getFileForm() {
        return "file/upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(HttpServletRequest request) throws ServletException, IOException {
        log.info("::: request => {} :::", request);

        String itemName = request.getParameter("itemName");
        log.info("::: fileName -> {} :::", itemName);

        Collection<Part> parts = request.getParts();
        log.info("::: parts => {} :::", parts);

        for (Part part : parts) {
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = file_path + part.getSubmittedFileName();
                log.info("::: FILE FULL PATH ::: => {}", fullPath);
                part.write(fullPath);
            }
        }

        return "file/upload-form";
    }

}
