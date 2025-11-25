package com.example.demo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.DatabaseFileService;


@RestController
@RequestMapping("/api/v1/evidences")
public class EvidencesController {
    private static Logger logger = LoggerFactory
        .getLogger(EvidencesController.class.getName());

    public final DatabaseFileService fileservice = new DatabaseFileService(){

    };

    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<?> upload(@RequestPart("evidence") MultipartFile file){
        String filename = file.getOriginalFilename();
        String fileExt = file.getContentType();
       
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("vc foi mó idiota");
        }
        logger.info("LALALALALALALA" + filename + ">>" + fileExt);

        return ResponseEntity.ok(Map.of(
            "nome", filename,
            "tipo", fileExt
        ));
    }

}
