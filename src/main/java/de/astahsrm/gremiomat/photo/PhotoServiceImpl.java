package de.astahsrm.gremiomat.photo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public Photo save(Photo photo) throws IOException {
        photo.setBytes(cropImageSquare(photo.getBytes(), photo.getMimeType()));
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

    @Override
    public byte[] cropImageSquare(byte[] image, String format) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return image;
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage croppedImage = originalImage.getSubimage(xc - (squareSize / 2), yc - (squareSize / 2), squareSize, squareSize);
        ImageIO.write(croppedImage, format.replace("image/", ""), baos);
        
        return baos.toByteArray();
    }

}
