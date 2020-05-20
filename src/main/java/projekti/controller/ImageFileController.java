package projekti.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projekti.model.ImageFile;
import projekti.repository.ImageFileRepository;

@Controller
public class ImageFileController {

    @Autowired
    private ImageFileRepository imageFileRepository;

    @PostMapping("/files")
    public String saveImage(@RequestParam("file") MultipartFile postFile) throws IOException {
        String contentType = postFile.getContentType();
        if (contentType != null) {
            if (contentType.equals("image/jpeg") || contentType.equals("image/png")) {
                ImageFile file = new ImageFile();

                file.setName(postFile.getOriginalFilename());
                file.setContentType(postFile.getContentType());
                file.setContentLength(postFile.getSize());
                file.setContent(postFile.getBytes());

                imageFileRepository.save(file);
            }
        }

        return "redirect:index";
    }
}
