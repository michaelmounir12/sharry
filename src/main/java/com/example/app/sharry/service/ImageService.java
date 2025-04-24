package com.example.app.sharry.service;

import com.example.app.sharry.dao.ImageRepository;
import com.example.app.sharry.dao.UserRepository;
import com.example.app.sharry.dto.ImageTransformRequest;
import com.example.app.sharry.model.Image;
import com.example.app.sharry.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.example.app.sharry.service.Transformations.*;

@Service
public class ImageService {

    private UserRepository R_User;
    private S3Service s3Service;
    private ImageRepository R_Image;

    @Autowired
    public ImageService(UserRepository r_User, S3Service s3Service, ImageRepository r_Image) {
        R_User = r_User;
        this.s3Service = s3Service;
        R_Image = r_Image;
    }
    public Image setImagePrivacy(Authentication auth,Long id)throws Exception{
        Image img = R_Image.getById(id);
        if(img==null) throw new Exception("image not found");
        if(!(img.getUser().getUsername().equals(auth.getName())) ) throw new Exception("not authorized");
        img.setPrivate(false);
        return R_Image.save(img);

    }
    public Image retrieveImage(Authentication auth,Long id) throws Exception{
        Image img = R_Image.getById(id);
        if(img==null) throw new Exception("image not found");
        if( (auth==null || !(img.getUser().getUsername().equals(auth.getName()))) && img.isPrivate()) throw new Exception("not authorized");
        return img;
    }
    public Page<Image> getPaginatedImages(Authentication auth, int page, int limit) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by("uploadedAt").descending());
        return R_Image.findAllByUser_Username(auth.getName(), pageable);
    }

    public Image uploadImage(Authentication auth, MultipartFile file)throws Exception{
        if(file==null || file.isEmpty()) throw new Exception("no file provided");
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new Exception("image type is invalid");
        }
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new Exception("image size excedded");
        }
        BufferedImage img;
        try {
            img = ImageIO.read(file.getInputStream());
            if (img == null) {
                throw new Exception("not a valid image");
            }
        } catch (IOException e) {
           throw new Exception("Unable to read image.");
        }
        User user = R_User.findByUsername(auth.getName());

        Image image = new Image();
        image.setMediaUrl(s3Service.uploadFile(file));
        image.setOriginalFileName(file.getOriginalFilename());
        image.setUser(user);
        return R_Image.save(image);





    }
    public Image transformImage(Long id, ImageTransformRequest request, Authentication auth) throws Exception {
        Image img = R_Image.getById(id);
        if (img == null || !img.getUser().getUsername().equals(auth.getName())) {
            throw new Exception("Unauthorized or not found");
        }

        // Download image from mediaUrl (local file, S3, etc.)
        BufferedImage original = s3Service.getImage(S3Service.extractKeyFromUrl(img.getMediaUrl()));

        BufferedImage transformed = applyTransformations(original, request.getTransformations());

        String format = request.getTransformations().getFormat();
        String fileName = "transformed_" + UUID.randomUUID() + "." + format;

        MultipartFile multipartImage = convertBufferedImageToMultipartFile(transformed, format, fileName);

        String newMediaUrl = s3Service.uploadFile(multipartImage); // Assuming it returns the S3 URL

        img.setMediaUrl(newMediaUrl);

        return R_Image.save(img);
    }
    public MultipartFile convertBufferedImageToMultipartFile(BufferedImage image, String format, String fileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] bytes = baos.toByteArray();

        return new MockMultipartFile(fileName, fileName, "image/" + format, bytes);
    }
    private BufferedImage applyTransformations(BufferedImage img, ImageTransformRequest.Transformations t) {
        BufferedImage result = img;

        if (t.getResize() != null) {
            result = resize(result,t.getResize().getWidth(), t.getResize().getHeight());
        }

        if (t.getCrop() != null) {
            result = crop(result, t.getCrop().getX(), t.getCrop().getY(), t.getCrop().getWidth(), t.getCrop().getHeight());
        }

        if (t.getRotate() != null) {
            result = rotate(result, t.getRotate());
        }

        if (t.getFilters() != null) {
            if (t.getFilters().isGrayscale()) {
                result = applyGrayscale(result);
            }
            if (t.getFilters().isSepia()) {
                result = applySepia(result);
            }
        }

        return result;
    }
    



}
