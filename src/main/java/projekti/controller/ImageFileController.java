package projekti.controller;

import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projekti.model.ImageFile;
import projekti.model.Person;
import projekti.repository.ImageFileRepository;
import projekti.repository.PersonRepository;

@Controller
public class ImageFileController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImageFileRepository imageFileRepository;

    @PostMapping("/files")
    public String saveImage(
            @RequestParam("file") MultipartFile postFile,
            Authentication authentication) throws IOException {

        String contentType = postFile.getContentType();
        Person person = personRepository.findByUsername(authentication.getName());

        if (contentType != null && person != null) {
            if (contentType.equals("image/jpeg") || contentType.equals("image/png")) {

                ImageFile file = new ImageFile();

                file.setName(postFile.getOriginalFilename());
                file.setContentType(postFile.getContentType());
                file.setContentLength(postFile.getSize());
                file.setContent(postFile.getBytes());

                // Check for existing profile picture
                ImageFile oldImageFile = person.getImageFile();

                ImageFile savedFile = imageFileRepository.save(file);
                person.setImageFile(savedFile);
                personRepository.save(person);

                // If there was an existing picture, delete it
                if (oldImageFile != null) {
                    imageFileRepository.deleteById(oldImageFile.getId());
                }
            }
        }

        return "redirect:index";
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Optional<ImageFile> optionalFile = imageFileRepository.findById(id);

        if (optionalFile.isPresent()) {
            ImageFile file = optionalFile.get();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
            headers.setContentLength(file.getContentLength());
            headers.add("Content-Disposition", "attachment; filename=" + file.getName());

            return new ResponseEntity<>(file.getContent(), headers, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
