package com.example.ImagePostingApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String showImages(Model model) {
        model.addAttribute("images", imageService.getAllImages());
        return "index"; // Display all images
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "redirect:/images"; // Handle empty file scenario
        }

        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileData(file.getBytes()); // Save the file data directly in the database

        imageService.saveImage(image); // Save the image
        return "redirect:/images";
    }

    @PostMapping("/delete/{id}")
    public String deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return "redirect:/images";
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam Long imageId, @RequestParam String content) {
        Image image = imageService.getAllImages().stream()
                .filter(i -> i.getId().equals(imageId)).findFirst().orElse(null);
        if (image != null) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setImage(image);
            commentService.saveComment(comment);
        }
        return "redirect:/images";
    }
    
    
//    Original
    @PostMapping("/like/{id}")
    public String likeImage(@PathVariable Long id) {
        imageService.incrementLikeCount(id);
        return "redirect:/images";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image image = imageService.getAllImages().stream()
                .filter(i -> i.getId().equals(id)).findFirst().orElse(null);

        if (image != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG
                    .body(image.getFileData());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "redirect:/images"; // Redirect back to the images page after deletion
    }
    
//    // Extra
//    @PostMapping("/like/{id}")
//    @ResponseBody
//    public ResponseEntity<Map<String, Integer>> likeImage(@PathVariable Long id) {
//        imageService.incrementLikeCount(id); // Increment like count
//        Image image = imageService.getImageById(id); // Fetch the updated image
//        Map<String, Integer> response = new HashMap<>();
//        response.put("likeCount", image.getLikeCount()); // Return the updated like count
//
//        return ResponseEntity.ok(response);
//    }
    
}
