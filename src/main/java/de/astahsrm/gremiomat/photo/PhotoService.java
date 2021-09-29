package de.astahsrm.gremiomat.photo;

import java.util.Optional;

public interface PhotoService {

    public Photo save(Photo photo);

    public Optional<Photo> getPhotoById(long id);

    public void delPhoto(Photo photo);

}
