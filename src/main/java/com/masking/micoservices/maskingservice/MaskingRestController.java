package com.masking.micoservices.maskingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.masking.micoservices.services.DataMaskingService;
import com.masking.micoservices.services.MaskedResult;
import com.masking.micoservices.services.MaskerConfig;
import com.masking.micoservices.services.MaskingRequest;
import com.masking.micoservices.services.UnMaskingRequest;


@RestController
public class MaskingRestController {
	
	@Autowired
	MaskerConfig maskingConfig;
	
	@Autowired
	DataMaskingService maskingService;
	
	@GetMapping("/showMaskingConfig")
	public MaskerConfig showMaskingConfig(){
		
		return maskingConfig;
		
	}
	
	
	@GetMapping("/getStoredResult/{id}")
	public MaskedResult getStoredResult(@PathVariable String id){
		
		return maskingService.getById(id);
		
	}
	
	
	@PostMapping("/mask")
	public MaskedResult handleMaskingRequest(@RequestBody MaskingRequest request) {
		
		return this.maskingService.mask(request);
		
	}

	
	@PostMapping(path = "/unmaskText", produces=MediaType.TEXT_PLAIN_VALUE )
	public String handleUnMaskingRequest(@RequestBody UnMaskingRequest request) {
		
		System.out.println("recieved unmaskText request with id :" + request.getMaskedResult().getRequestId() + request.getMaskedResult().getMaskedAttributes());
		request.getMaskedResult().getMaskedAttributes().forEach(m->{
			System.out.println(m.toString());
		});
		
		return this.maskingService.unMask(request.getTextToUnmask(),request.getMaskedResult());
		
	}
	
	
	@PostMapping("/redact")
	public MaskedResult handleRedactRequest(@RequestBody MaskingRequest request) {
		
		return this.maskingService.redact(request);
		
	}
}
