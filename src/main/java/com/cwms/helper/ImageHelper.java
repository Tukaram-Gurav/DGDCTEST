package com.cwms.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.cwms.entities.RepresentParty;

@Component
public class ImageHelper 
{

	
	public String getRepresentativeImage(RepresentParty representative) {
	    String dataURL = "";
	    try {
	        String nsdlStatusDocsPath = representative.getSignImagePath();
	        Path filePath = Paths.get(nsdlStatusDocsPath);

	        // Check if the file exists
	        if (Files.exists(filePath)) {
	            byte[] imageBytes = Files.readAllBytes(filePath);

	            // Encode the image bytes to base64
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	            // Construct a data URL for the image
	            dataURL = "data:image/jpeg;base64," + base64Image;
	        }
	    } catch (IOException e) {
	        // Log the error or handle it as needed
	        System.err.println("Error reading image file: " + e.getMessage());
	    }
	    return dataURL;
	}

	public String getRepresentativeImagePhoto(RepresentParty representative) {
	    String dataURL = "";
	    try {
	        String nsdlStatusDocsPath = representative.getImagePath();
	        Path filePath = Paths.get(nsdlStatusDocsPath);

	        // Check if the file exists
	        if (Files.exists(filePath)) {
	            byte[] imageBytes = Files.readAllBytes(filePath);

	            // Encode the image bytes to base64
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	            // Construct a data URL for the image
	            dataURL = "data:image/jpeg;base64," + base64Image;
	        }
	    } catch (IOException e) {
	        // Log the error or handle it as needed
	        System.err.println("Error reading image file: " + e.getMessage());
	    }
	    return dataURL;
	}

	
	
	
	
	
	public String getPhoto(String path) {
	    String dataURL = "";
	    try {
	       
	        Path filePath = Paths.get(path);

	        // Check if the file exists
	        if (Files.exists(filePath)) {
	            byte[] imageBytes = Files.readAllBytes(filePath);

	            // Encode the image bytes to base64
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	            // Construct a data URL for the image
	            dataURL = "data:image/jpeg;base64," + base64Image;
	        }
	    } catch (IOException e) {
	        // Log the error or handle it as needed
	        System.err.println("Error reading image file: " + e.getMessage());
	    }
	    return dataURL;
	}

	
	
	
	
	
	
	
	
	
}
