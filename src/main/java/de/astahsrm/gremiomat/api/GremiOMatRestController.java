package de.astahsrm.gremiomat.api;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.astahsrm.gremiomat.photo.Photo;
import de.astahsrm.gremiomat.photo.PhotoService;

@RestController
@RequestMapping("/api/v1")
public class GremiOMatRestController {

    @Autowired
    private PhotoService photoService;

    @GetMapping(value = "/photo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getFoto(@PathVariable("id") long id) {
        Optional<Photo> pOpt = photoService.getPhotoById(id);
        if (pOpt.isPresent()) {
            Photo photo = pOpt.get();
            return ResponseEntity.ok().header("Content-Type", photo.getMimeType())
                    .body(photo.getBytes());
        }
        return ResponseEntity.badRequest().build();
    }
}
