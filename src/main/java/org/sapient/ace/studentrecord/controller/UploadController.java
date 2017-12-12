package org.sapient.ace.studentrecord.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.sapient.ace.studentrecord.exception.DataException;
import org.sapient.ace.studentrecord.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

@Controller
public class UploadController {
	
	private final UploadService uploadService;
	
	@Inject
	public UploadController(final UploadService uploadService) {
		this.uploadService = uploadService;
	}
	
	
	@GetMapping
	public String index() {
		return "upload";
	}
	
	 @PostMapping("/upload") 
	    public String singleFileUpload(@RequestParam("file") MultipartFile file,
	                                   RedirectAttributes redirectAttributes) {

	        if (file.isEmpty()) {
	            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
	            return "redirect:uploadStatus";
	        }

	            try {
					uploadService.uploadOperation(file);
					redirectAttributes.addFlashAttribute("message",
							"You successfully uploaded '" + file.getOriginalFilename() + "'");
				} catch (DataException | ParserConfigurationException | SAXException | IOException e) {
					e.printStackTrace();
				}


	        return "redirect:/uploadStatus";
	    }

	    @GetMapping("/uploadStatus")
	    public String uploadStatus() {
	        return "uploadStatus";
	    }

}
