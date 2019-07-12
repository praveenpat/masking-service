package com.masking.micoservices.services;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Generic Replacement Masker that replaces the original values with dummy values that with the same format.
 * @author praveen
 *
 */

public class Masker {
	
	String maskerType;
	String regex;
	String replacementString;
	String replacementValues;
	List<String> replacementList;
	Pattern pattern;
	
	String redactionRegex;
	String redactionPattern;
	
	


	public String getRedactionRegex() {
		return redactionRegex;
	}


	public void setRedactionRegex(String redactionRegex) {
		this.redactionRegex = redactionRegex;
	}


	public String getRedactionPattern() {
		return redactionPattern;
	}


	public void setRedactionPattern(String redactionPattern) {
		this.redactionPattern = redactionPattern;
	}


	public String getReplacementValues() {
		return replacementValues;
	}


	public void setReplacementValues(String replacementValues) {
		
		
		
		this.replacementValues = replacementValues;
	}
	
	
	
	
	
	public String getMaskerType() {
		return maskerType;
	}


	public void setMaskerType(String maskerType) {
		this.maskerType = maskerType;
	}


	public Pattern getPattern() {
		
		if(pattern==null) {
			pattern=Pattern.compile(this.regex);
		}
		return pattern;
	}
	
	
	
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getReplacementString() {
		return replacementString;
	}
	public void setReplacementString(String replacementString) {
		this.replacementString = replacementString;
	}
	
	
	
	
	
	/**
	 *  Return random replacement string from replacement list that not already present in masked attributes to avoid duplicates, 
	 *  if list is empty returns default replacementString
	 * @param originalvalue
	 * @param maskedAttributesList
	 */
	public String getReplacementString(String originalvalue,List<MaskedAttributes> maskedAttributesList) {
		
		if(CollectionUtils.isEmpty(replacementList)) {
			
			if(StringUtils.hasText(replacementValues)) {
				String[] split = this.replacementValues.split("\\|");
				if (split!=null && split.length>0) {
					
					replacementList=Arrays.asList(split);
				}
				
				System.out.println(replacementList);
			}
		}
		
		if(!CollectionUtils.isEmpty(replacementList)) {
			
			Random random=new Random();
			String replacementValueFromList= replacementList.get(random.nextInt(replacementList.size()));
			System.out.println(replacementValueFromList);
			for(MaskedAttributes attr:maskedAttributesList) {
					
					if(attr.getReplacedValue().equalsIgnoreCase(replacementValueFromList)) {
						replacementValueFromList = replacementList.get(random.nextInt(replacementList.size()));
						
					}
				}
			
			return replacementValueFromList;
			
		}else {
			
			return replacementString;
			
		}
		
		
		
		
		
		
	}
	
	
	
	public String getRedactedString(String originalvalue) {
		if(!StringUtils.isEmpty(originalvalue)) {
			
			return originalvalue.replaceAll(redactionRegex, redactionPattern);
			
		}
		else return originalvalue;
		
		
	}

	

}
