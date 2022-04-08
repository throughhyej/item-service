package hello.itemservice.file;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class FileUpDownloadController {

    private final FileRepository fileRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm itemForm) {
        return "/file/item-form";
    }

    @PostMapping("/items/new")
    public String uploadFile(@ModelAttribute ItemForm itemForm, RedirectAttributes redirect) throws IOException {
        MultipartFile multipart = itemForm.getFile();
        UploadFile uploadFile = fileStore.storeFile(multipart);

        List<MultipartFile> files = itemForm.getFiles();
        List<UploadFile> uploadFiles = fileStore.storeFiles(files);

        FileItem fileItem = new FileItem(itemForm.getItemName(), uploadFile, uploadFiles);
        FileItem saved = fileRepository.save(fileItem);

        redirect.addAttribute("itemId", saved.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String view(@PathVariable Long id, Model model) {
        FileItem query = fileRepository.findById(id);
        model.addAttribute("item", query);
        return "/file/item-view";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource viewImg(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFilePullPath(fileName));
    }

    @GetMapping("/attach/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws MalformedURLException {
        FileItem file = fileRepository.findById(id);
        String originalName = file.getAttachFile().getUploadFileName();
        String storeName = file.getAttachFile().getStoreFileName();

        String contentDisposition = "attachment; fileName=\"" + originalName + "\"";

        UrlResource urlResource = new UrlResource("file:" + fileStore.getFilePullPath(storeName));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

}
