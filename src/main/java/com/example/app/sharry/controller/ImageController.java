package com.example.app.sharry.controller;

import com.example.app.sharry.dto.ImageDto;
import com.example.app.sharry.dto.ImageTransformRequest;
import com.example.app.sharry.model.Image;
import com.example.app.sharry.responses.ApiResponse;
import com.example.app.sharry.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/sharry")
public class ImageController {
    @Autowired
    private ImageService imgService;
    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    private ImageService imageService;

    @PostMapping(path = "/images", produces = "application/json")
    public ResponseEntity<Object> uploadImage(Authentication auth,
                                              @RequestParam(value = "file", required = false) MultipartFile file)throws Exception{
           Image img = imgService.uploadImage(auth,file);
           apiResponse.setMessage("image uploaded successfully");
           apiResponse.setData(img);
           return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);


    }
    @PostMapping(path="/images/{id}/setPrivacy",produces="application/json")
    public ResponseEntity<Object> setPrivacy(Authentication auth,@PathVariable Long id) throws Exception{
        Image img = imageService.setImagePrivacy(auth,id);
        ImageDto imageDto = new ImageDto(img);
        apiResponse.setData(imageDto);
        apiResponse.setData(imageDto);

        apiResponse.setMessage("image privacy changed");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @GetMapping(path="/images/{id}", produces = "application/json")
    public ResponseEntity<Object> retrieveImage(Authentication auth,@PathVariable Long id)throws Exception{
        Image img = imgService.retrieveImage(auth, id);
        ImageDto imageDto = new ImageDto(img);
        apiResponse.setData(imageDto);

        apiResponse.setMessage("retrieved image successfully");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);

    }
    @PostMapping("/images/{id}/transform")
    public ResponseEntity<Object> transformImage(
            @PathVariable Long id,
            @RequestBody ImageTransformRequest request,
            Authentication auth) throws Exception {

        Image transformedImage = imgService.transformImage(id, request, auth);
        ImageDto dto = new ImageDto(transformedImage);

        apiResponse.setData(dto);
        apiResponse.setMessage("Image transformed successfully");

        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @GetMapping("/images")
    public ResponseEntity<Object> getPaginatedImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication auth) {

        Page<Image> images = imgService.getPaginatedImages(auth, page, limit);
        List<ImageDto> dtos = images.getContent().stream().map(ImageDto::new).toList();

        apiResponse.setData(dtos);
        apiResponse.setMessage("Fetched paginated images");

        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }


}
