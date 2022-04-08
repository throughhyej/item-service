package hello.itemservice.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class FileUploadServletControllerV1 {

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
            log.info("::: for parts :::");
            log.info("::: part.getName() => {}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("::: for part headersNames => {} ::: ", headerName);
                String headerValue = part.getHeader(headerName);
                log.info("::: part header {} => {}", headerName, headerValue);
            }

            // 편의 메서드
            log.info("::: part.getSubmittedFileName() => {}:::", part.getSubmittedFileName());
            log.info("::: part bodysize => {}", part.getSize());

            //  바이너리 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("::: 바이너리 데이터 body => {} :::", body);
        }


        return "file/upload-form";
    }

}
