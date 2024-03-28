package com.AmericanBoutique.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class fileController {

    @GetMapping("/upload")
    public String handleFileUpload() {
            return "file";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestPart("avatar") MultipartFile file) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            System.out.println("------------------> File Name: "+ fileName);
            // Process the file (e.g., save it to a directory)
            // Return a response or redirect to another page
            return "redirect:/upload";
        } else {
            // Handle empty file
            return "redirect:/error";
        }
    }

}
