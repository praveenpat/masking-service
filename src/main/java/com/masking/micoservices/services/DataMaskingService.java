package com.masking.micoservices.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class DataMaskingService {
	

	
	List<Masker> maskers;
	
	@Autowired
	MaskerConfig maskerConfig;
	
	@Autowired
	MaskedResultRepository repo;
	

	 public MaskedResult mask(MaskingRequest request) {
		 
		  MaskedResult result=new MaskedResult();
		  result.setRequestId(request.getRequestId());
		  result.setOriginalUtterance(request.getUserQuery());
		  
		  MaskedResult cachedRequest = getById(request.getRequestId());
		  List<MaskedAttributes> maskedAttributesList=cachedRequest!=null && cachedRequest.getMaskedAttributes()!=null? cachedRequest.getMaskedAttributes():new ArrayList<>();
		  
		  String maskedUtterance=request.getUserQuery();
		  
		  for(Masker m:maskerConfig.getMaskers()) {
			  
			  maskedUtterance=maskUtterance(maskedUtterance, m, maskedAttributesList);
		  }
		  
		result.setMaskedUtterance(maskedUtterance);
		result.setMaskedAttributes(maskedAttributesList);
		
		if(!CollectionUtils.isEmpty(maskedAttributesList)) {
			saveToRepo(result);
		}
		 return result;
	 }
	 
	 
	 
	 public MaskedResult redact(MaskingRequest request) {
		 
		  MaskedResult result=new MaskedResult();
		  result.setRequestId(request.getRequestId());
		  result.setOriginalUtterance(request.getUserQuery());
		  List<MaskedAttributes> maskedAttributesList=new ArrayList<>();
		  
		  String maskedUtterance=request.getUserQuery();
		  
		  for(Masker m:maskerConfig.getMaskers()) {
			  
			  maskedUtterance=redactUtterance(maskedUtterance, m, maskedAttributesList);
		  }
		  
		result.setMaskedUtterance(maskedUtterance);
		result.setMaskedAttributes(maskedAttributesList);
		 return result;
	 }
	 
	 
	 private String maskUtterance(String utterance,Masker masker,final List<MaskedAttributes> maskedAttributesList) {
		 
		 Matcher matcher=masker.getPattern().matcher(utterance);
		 StringBuffer maskedUtterance=new StringBuffer();
	     
		 while(matcher.find()) {
			    System.out.println("Masking :-- " + matcher.group());
			    String replacementValue= getExistingMaskedValue(matcher.group(), maskedAttributesList);
			    System.out.println("Replacement Value :-- " + replacementValue);
			    if(StringUtils.hasText(replacementValue)) {
			    		matcher.appendReplacement(maskedUtterance,replacementValue);
			    }
			    else {
				 	MaskedAttributes attribute=new MaskedAttributes();
		            attribute.setOriginalValue(matcher.group());
		            attribute.setReplacedValue(masker.getReplacementString(attribute.getOriginalValue(),maskedAttributesList));
		            matcher.appendReplacement(maskedUtterance,attribute.getReplacedValue());
		            maskedAttributesList.add(attribute);
			    }
		 }
		 
		 matcher.appendTail(maskedUtterance);
		 
		 return maskedUtterance.toString();
		 
		 
	 }
	  
	 
	 private String redactUtterance(String utterance,Masker masker,final List<MaskedAttributes> maskedAttributesList) {
		 
		 Matcher matcher=masker.getPattern().matcher(utterance);
		 StringBuffer maskedUtterance=new StringBuffer();
	     
		 while(matcher.find()) {
			 	MaskedAttributes attribute=new MaskedAttributes();
	            attribute.setOriginalValue(matcher.group());
	            attribute.setReplacedValue(masker.getRedactedString(attribute.getOriginalValue()));
	            matcher.appendReplacement(maskedUtterance,attribute.getReplacedValue());
	            maskedAttributesList.add(attribute);
		 }
		 
		 matcher.appendTail(maskedUtterance);
		 
		 return maskedUtterance.toString();
		 
		 
	 }
	 
	 
	 
	 
	 
	 public String getExistingMaskedValue(String stringValue, List<MaskedAttributes> maskedAttributes) {
			
			System.out.println("********Hare*****"+stringValue);
			
			if(!CollectionUtils.isEmpty(maskedAttributes)){
				
				for(MaskedAttributes attr:maskedAttributes)
					
					if(attr.getOriginalValue().replaceAll("-", "").equalsIgnoreCase(stringValue.replaceAll("-", ""))) {
						
						return attr.getReplacedValue();
						
					}
				}
				
			
			
			return null;
			
		}
	
	
	 
	 
	 public String getUnmaskedValue(String stringValue, MaskedResult maskedResult) {
			
			System.out.println("********Hare*****"+stringValue);
			
			if(!CollectionUtils.isEmpty(maskedResult.getMaskedAttributes())){
				
				for(MaskedAttributes attr:maskedResult.getMaskedAttributes())
					
					if(attr.getReplacedValue().replaceAll("-", "").equalsIgnoreCase(stringValue.replaceAll("-", ""))) {
						
						return attr.getOriginalValue();
						
					}
				}
				
			
			
			return stringValue;
			
		}


	public String unMask(String maskedUtterance, MaskedResult maskedResults) {
		
		String initialUnmaskedUtterance=maskedUtterance; 
		
		MaskedResult maskedResultFromCache = getById(maskedResults.getRequestId());
		if (maskedResultFromCache!=null) {
			
			if(!CollectionUtils.isEmpty(maskedResults.getMaskedAttributes()) && !CollectionUtils.isEmpty(maskedResultFromCache.getMaskedAttributes())) {
				
				maskedResults.setMaskedAttributes(ListUtils.union(maskedResults.getMaskedAttributes(), maskedResultFromCache.getMaskedAttributes()));
			}
			else if(!CollectionUtils.isEmpty(maskedResults.getMaskedAttributes()) && CollectionUtils.isEmpty(maskedResults.getMaskedAttributes()) ) {
				maskedResults.setMaskedAttributes(maskedResultFromCache.getMaskedAttributes());
			}
			
		}
		
		
		for(Masker masker:maskerConfig.getMaskers()) {
			 //System.out.println(masker.getMaskerType() + "Initail masked Utterance :" + initialUnmaskedUtterance);
			 Matcher matcher=masker.getPattern().matcher(initialUnmaskedUtterance);
			 StringBuffer unmaskedIntermediateUtterance=new StringBuffer();
			 while(matcher.find()) {	
				 System.out.println("Found:" + matcher.group()); 
				 
		            matcher.appendReplacement(unmaskedIntermediateUtterance,getUnmaskedValue(matcher.group(), maskedResults));
		   	 }
			 
			 matcher.appendTail(unmaskedIntermediateUtterance);
			 System.out.println(unmaskedIntermediateUtterance);
			 initialUnmaskedUtterance=unmaskedIntermediateUtterance.toString();
		  }
		
		
	     
		 
		 
		 return initialUnmaskedUtterance.toString();
		
		
	}
	
	
	public void saveToRepo(MaskedResult result) {
		
		repo.save(result);
		
	}
	
	public MaskedResult getById(String id) {
		
		Optional<MaskedResult> result= repo.findById(id);
		
		if (result.isPresent()) {
			return result.get();
		}
		
		return null;
	}
	
	
	/* public static void main(String[] args) {
	 
	 DataMaskingService maskingService=new DataMaskingService();
	 
	 MaskerConfig maskerConfig=new MaskerConfig();
	
	 
	 List<Masker> maskers = new ArrayList<>();
	 
	 Masker m1=new Masker();
	 m1.setRegex(ssnRegex);
	 m1.setMaskerType("SSN");
	 m1.setReplacementString("856-45-6718");
	 maskers.add(m1);
	 
	 Masker m2=new Masker();
	 m2.setRegex(northAmericaPhoneRegex);
	 m2.setMaskerType("NA_PHONENUMBER");
	 m2.setReplacementString("302-776-1129");
	 maskers.add(m2);
	 
	 maskerConfig.setMaskers(maskers);
	 maskingService.maskerConfig=maskerConfig;
	 
	 
	 String utterance="Here is my mobile number 302-690-1099 and my SSN is 005-21-2718 and my house number is 302-554-1139";
	 
	 MaskedResult maskedResult = maskingService.mask(utterance);
	 
	 System.out.println(maskedResult.getMaskedUtterance());
	 
	 maskedResult.getMaskedAttributes().forEach(val->{
		 
		 System.out.println("Orig: " + val.getOriginalValue() +" masked :" + val.getReplacedValue());
	 });
	 
	 
	 
	 
	
	}*/
	
	 
}



 

