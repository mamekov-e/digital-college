package com.example.digitalcollege.controller;

import com.example.digitalcollege.model.ImageModel;
import com.example.digitalcollege.payload.response.MessageResponse;
import com.example.digitalcollege.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("api/image")
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @GetMapping("/profile-image")
    public ResponseEntity<ImageModel> getUserImage(Principal principal) {
        ImageModel imageModel = imageUploadService.getImageToUser(principal);
        
        return new ResponseEntity<>(imageModel, HttpStatus.OK);
    }
}
