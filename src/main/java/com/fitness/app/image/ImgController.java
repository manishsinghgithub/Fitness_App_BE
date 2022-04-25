package com.fitness.app.image;



import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImgController {

	
	@Autowired
	private ImgService imgService;
	
	
	
	
	// save file to database
   @PutMapping("/uploadFile/{id}")
   public Doc saveFile(@RequestParam MultipartFile file, @PathVariable String id) throws Exception
   {
	   return imgService.saveImage(file, id);
   }
   
   
   @GetMapping("/downloadFile/{id}")
   public ResponseEntity<?> getImage(@PathVariable String id) throws IOException
   {
	   Doc docFile=imgService.getImage(id);
	   
	   return  ResponseEntity.ok()
			   .contentType(MediaType.parseMediaType(docFile.getFileType()))
			   .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\""+docFile.getFileName()+"\"")
			   .body(new ByteArrayResource(docFile.getData()));
	   
	   //
			   
   }
   
  
}
