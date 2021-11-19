package de.astahsrm.gremiomat.photo;

import java.io.IOException;
import java.util.Optional;

public interface PhotoService {

    public Photo save(Photo photo) throws IOException;

    public Optional<Photo> getPhotoById(long id);

    public void delPhoto(Photo photo);

    public byte[] cropImageSquare(byte[] image, String format) throws IOException;

}
