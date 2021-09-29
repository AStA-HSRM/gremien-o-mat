package de.astahsrm.gremiomat.photo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public Optional<Photo> getPhotoById(long id) {
        return photoRepository.findById(id);
    }

    @Override
    public void delPhoto(Photo photo) {
        photoRepository.delete(photo);
    }

}
